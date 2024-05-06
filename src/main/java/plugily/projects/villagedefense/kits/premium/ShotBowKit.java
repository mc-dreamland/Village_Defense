/*
 *  Village Defense - Protect villagers from hordes of zombies
 *  Copyright (c) 2023 Plugily Projects - maintained by Tigerpanzer_02 and contributors
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package plugily.projects.villagedefense.kits.premium;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import plugily.projects.minigamesbox.classic.handlers.language.MessageBuilder;
import plugily.projects.minigamesbox.classic.kits.basekits.PremiumKit;
import plugily.projects.minigamesbox.classic.user.User;
import plugily.projects.minigamesbox.classic.utils.helper.ArmorHelper;
import plugily.projects.minigamesbox.classic.utils.helper.WeaponHelper;
import plugily.projects.minigamesbox.classic.utils.version.VersionUtils;
import plugily.projects.minigamesbox.classic.utils.version.events.api.PlugilyPlayerInteractEvent;

import java.util.List;


/**
 * Created by Tom on 27/08/2014.
 */
public class ShotBowKit extends PremiumKit implements Listener {

  public ShotBowKit() {
    setName(new MessageBuilder("KIT_CONTENT_SHOT_BOW_NAME").asKey().build());
    setKey("ShotBow");
    List<String> description = getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_SHOT_BOW_DESCRIPTION");
    setDescription(description);
    getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    getPlugin().getKitRegistry().registerKit(this);
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return getPlugin().getPermissionsManager().hasPermissionString("KIT_PREMIUM_UNLOCK", player) || player.hasPermission("villagedefense.kit.shotbow");
  }

  @Override
  public void giveKitItems(Player player) {
    ItemStack weaponEnchanted = WeaponHelper.getEnchantedBow(new Enchantment[]{Enchantment.ARROW_DAMAGE, Enchantment.ARROW_FIRE}, new int[]{5, 1});
    player.getInventory().addItem(weaponEnchanted);
    player.getInventory().addItem(new ItemStack(getMaterial(), 64));
    player.getInventory().addItem(new ItemStack(getMaterial(), 64));
    ArmorHelper.setColouredArmor(Color.YELLOW, player);
    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
    player.getInventory().addItem(new ItemStack(Material.SADDLE));
  }

  @Override
  public Material getMaterial() {
    return Material.ARROW;
  }

  @Override
  public void reStock(Player player) {
    player.getInventory().addItem(new ItemStack(getMaterial(), 64));
  }

  @EventHandler
  public void onBowInteract(PlugilyPlayerInteractEvent e) {
    if(!(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.PHYSICAL)) {
      return;
    }

    ItemStack stack = VersionUtils.getItemInHand(e.getPlayer());
    if(stack == null || stack.getType() != Material.BOW)
      return;

    if(!e.getPlayer().getInventory().contains(getMaterial()))
      return;

    User user = getPlugin().getUserManager().getUser(e.getPlayer());
    if(user.isSpectator() || !(user.getKit() instanceof ShotBowKit)) {
      return;
    }
    if(!user.checkCanCastCooldownAndMessage("shotbow")) {
      return;
    }
    for(int i = 0; i < 4; i++) {
      Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
        Arrow pr = e.getPlayer().launchProjectile(Arrow.class);
        pr.setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
        pr.setBounce(false);
        pr.setShooter(e.getPlayer());
        pr.setCritical(true);

        org.bukkit.inventory.PlayerInventory inv = e.getPlayer().getInventory();

        if(inv.contains(getMaterial())) {
          inv.removeItem(new ItemStack(getMaterial(), 1));
        }
      }, 2L * (2 * i));
    }
    e.setCancelled(true);
    user.setCooldown("shotbow", getKitsConfig().getInt("Kit-Cooldown.Shot-Bow", 5));
  }

}

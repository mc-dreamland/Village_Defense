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

package plugily.projects.villagedefense.kits.level;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugily.projects.minigamesbox.classic.handlers.language.MessageBuilder;
import plugily.projects.minigamesbox.classic.kits.basekits.LevelKit;
import plugily.projects.minigamesbox.classic.utils.helper.ArmorHelper;
import plugily.projects.minigamesbox.classic.utils.helper.WeaponHelper;
import plugily.projects.minigamesbox.classic.utils.version.VersionUtils;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.utils.Utils;

import java.util.List;

/**
 * Created by Tom on 19/07/2015.
 */
public class WorkerKit extends LevelKit implements Listener {

  public WorkerKit() {
    setLevel(getKitsConfig().getInt("Required-Level.Worker"));
    setName(new MessageBuilder("KIT_CONTENT_WORKER_NAME").asKey().build());
    setKey("Worker");
    List<String> description = getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_WORKER_DESCRIPTION");
    setDescription(description);
    getPlugin().getKitRegistry().registerKit(this);
    getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return getPlugin().getUserManager().getUser(player).getStatistic("LEVEL") >= getLevel() || player.hasPermission("villagedefense.kit.worker");
  }

  @Override
  public void giveKitItems(Player player) {
    ArmorHelper.setColouredArmor(Color.PURPLE, player);
    ItemStack weaponItem = new ItemStack(XMaterial.WOODEN_SWORD.parseMaterial());
    ItemMeta itemMeta = weaponItem.getItemMeta();
    itemMeta.setUnbreakable(true);
    weaponItem.setItemMeta(itemMeta);
    player.getInventory().addItem(weaponItem);

    ItemStack bowItem = new ItemStack(XMaterial.BOW.parseMaterial());
    ItemMeta bowItemItemMeta = bowItem.getItemMeta();
    itemMeta.setUnbreakable(true);
    bowItem.setItemMeta(bowItemItemMeta);
    player.getInventory().addItem(bowItem);

    player.getInventory().addItem(new ItemStack(XMaterial.ARROW.parseMaterial(), 64));
    player.getInventory().addItem(new ItemStack(XMaterial.COOKED_BEEF.parseMaterial(), 10));
    player.getInventory().addItem(new ItemStack(getMaterial(), 2));
  }

  @Override
  public Material getMaterial() {
    return Utils.getCachedDoor(null);
  }

  @Override
  public void reStock(Player player) {
    player.getInventory().addItem(new ItemStack(getMaterial()));
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDoorPlace(BlockPlaceEvent event) {
    Arena arena = (Arena) getPlugin().getArenaRegistry().getArena(event.getPlayer());
    if(arena == null) {
      return;
    }
    if(getPlugin().getUserManager().getUser(event.getPlayer()).isSpectator() || !arena.getMapRestorerManager().getGameDoorLocations()
        .containsKey(event.getBlock().getLocation())) {
      event.setCancelled(true);
      return;
    }
    if(VersionUtils.getItemInHand(event.getPlayer()).getType() != Utils.getCachedDoor(event.getBlock())) {
      event.setCancelled(true);
      return;
    }
    //to override world guard protection
    event.setCancelled(false);
    new MessageBuilder("KIT_CONTENT_WORKER_GAME_ITEM_CHAT").asKey().player(event.getPlayer()).sendPlayer();
  }

}

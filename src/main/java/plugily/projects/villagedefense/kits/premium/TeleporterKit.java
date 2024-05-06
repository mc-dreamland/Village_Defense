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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import plugily.projects.minigamesbox.classic.handlers.language.MessageBuilder;
import plugily.projects.minigamesbox.classic.kits.basekits.PremiumKit;
import plugily.projects.minigamesbox.classic.utils.helper.ArmorHelper;
import plugily.projects.minigamesbox.classic.utils.helper.ItemBuilder;
import plugily.projects.minigamesbox.classic.utils.helper.ItemUtils;
import plugily.projects.minigamesbox.classic.utils.helper.WeaponHelper;
import plugily.projects.minigamesbox.classic.utils.misc.complement.ComplementAccessor;
import plugily.projects.minigamesbox.classic.utils.version.VersionUtils;
import plugily.projects.minigamesbox.classic.utils.version.events.api.PlugilyPlayerInteractEvent;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;
import plugily.projects.minigamesbox.inventory.normal.NormalFastInv;
import plugily.projects.villagedefense.arena.Arena;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 18/08/2014.
 */
public class TeleporterKit extends PremiumKit implements Listener {

  public TeleporterKit() {
    setName(new MessageBuilder("KIT_CONTENT_TELEPORTER_NAME").asKey().build());
    setKey("Teleporter");
    List<String> description = getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_TELEPORTER_DESCRIPTION");
    setDescription(description);
    getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    getPlugin().getKitRegistry().registerKit(this);
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return getPlugin().getPermissionsManager().hasPermissionString("KIT_PREMIUM_UNLOCK", player) || player.hasPermission("villagedefense.kit.teleporter");
  }

  @Override
  public void giveKitItems(Player player) {
    ArmorHelper.setArmor(player, ArmorHelper.ArmorType.GOLD);
    ItemStack weaponItem = new ItemStack(XMaterial.STONE_SWORD.parseMaterial());
    ItemMeta itemMeta = weaponItem.getItemMeta();
    itemMeta.setUnbreakable(true);
    weaponItem.setItemMeta(itemMeta);
    weaponItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
    player.getInventory().addItem(weaponItem);

    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
    player.getInventory().addItem(new ItemStack(Material.SADDLE));
    player.getInventory().addItem(new ItemBuilder(Material.GHAST_TEAR)
        .name(new MessageBuilder("KIT_CONTENT_TELEPORTER_GAME_ITEM_NAME").asKey().build())
        .lore(getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_TELEPORTER_GAME_ITEM_DESCRIPTION"))
        .build());
  }

  @Override
  public Material getMaterial() {
    return Material.ENDER_PEARL;
  }

  @Override
  public void reStock(Player player) {
    //no restock items for this kit
  }

  @EventHandler
  public void onRightClick(PlugilyPlayerInteractEvent e) {
    if(!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
      return;
    }
    Player player = e.getPlayer();
    Arena arena = (Arena) getPlugin().getArenaRegistry().getArena(player);
    if(arena == null) {
      return;
    }

    ItemStack stack = VersionUtils.getItemInHand(player);
    if(!ItemUtils.isItemStackNamed(stack))
      return;

    if(!ChatColor.stripColor(ComplementAccessor.getComplement().getDisplayName(stack.getItemMeta())).equalsIgnoreCase(ChatColor.stripColor(new MessageBuilder("KIT_CONTENT_TELEPORTER_GAME_ITEM_NAME").asKey().build()))) {
      return;
    }
    if(!(getPlugin().getUserManager().getUser(player).getKit() instanceof TeleporterKit)) {
      return;
    }
    int slots = arena.getVillagers().size();
    for(Player arenaPlayer : arena.getPlayers()) {
      if(getPlugin().getUserManager().getUser(arenaPlayer).isSpectator()) {
        continue;
      }
      slots++;
    }
    slots = getPlugin().getBukkitHelper().serializeInt(slots);
    prepareTeleporterGui(player, arena, slots);
  }

  private void prepareTeleporterGui(Player player, Arena arena, int slots) {
    NormalFastInv gui = new NormalFastInv(slots, new MessageBuilder("KIT_CONTENT_TELEPORTER_GAME_ITEM_GUI").asKey().build());
    gui.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    for(Player arenaPlayer : arena.getPlayers()) {
      if(getPlugin().getUserManager().getUser(arenaPlayer).isSpectator()) {
        continue;
      }
      ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
      SkullMeta meta = (SkullMeta) skull.getItemMeta();
      meta = VersionUtils.setPlayerHead(player, meta);
      ComplementAccessor.getComplement().setDisplayName(meta, arenaPlayer.getName());
      ComplementAccessor.getComplement().setLore(meta, Collections.singletonList(""));
      skull.setItemMeta(meta);
      gui.addItem(skull, onClick -> {
        new MessageBuilder("KIT_CONTENT_TELEPORTER_TELEPORT_PLAYER").asKey().arena(arena).player(arenaPlayer).sendPlayer();
        VersionUtils.teleport(player, arenaPlayer.getLocation());
        VersionUtils.playSound(player.getLocation(), "ENTITY_ENDERMAN_TELEPORT");
        VersionUtils.sendParticles("PORTAL", arena.getPlayers(), player.getLocation(), 30);
        player.closeInventory();
      });
    }
    for(Villager villager : arena.getVillagers()) {
      gui.addItem(new ItemBuilder(new ItemStack(Material.EMERALD))
          .name(villager.getCustomName())
          .lore(villager.getUniqueId().toString())
          .build(), onClick -> {
        VersionUtils.teleport(player, villager.getLocation());
        VersionUtils.playSound(player.getLocation(), "ENTITY_ENDERMAN_TELEPORT");
        VersionUtils.sendParticles("PORTAL", arena.getPlayers(), player.getLocation(), 30);
        new MessageBuilder("KIT_CONTENT_TELEPORTER_TELEPORT_VILLAGER").asKey().player(player).sendPlayer();
      });
    }
    gui.open(player);
  }

}

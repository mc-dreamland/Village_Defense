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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugily.projects.minigamesbox.classic.handlers.language.MessageBuilder;
import plugily.projects.minigamesbox.classic.kits.basekits.LevelKit;
import plugily.projects.minigamesbox.classic.utils.helper.ArmorHelper;
import plugily.projects.minigamesbox.classic.utils.helper.WeaponHelper;
import plugily.projects.minigamesbox.classic.utils.version.VersionUtils;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;

import java.util.List;

/**
 * Created by Tom on 19/08/2014.
 */
public class MediumTankKit extends LevelKit {

  public MediumTankKit() {
    setName(new MessageBuilder("KIT_CONTENT_MEDIUM_TANK_NAME").asKey().build());
    setKey("MediumTank");
    List<String> description = getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_MEDIUM_TANK_DESCRIPTION");
    setDescription(description);
    setLevel(getKitsConfig().getInt("Required-Level.MediumTank"));
    getPlugin().getKitRegistry().registerKit(this);
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return getPlugin().getUserManager().getUser(player).getStatistic("LEVEL") >= this.getLevel() || player.hasPermission("villagedefense.kit.mediumtank");
  }

  @Override
  public void giveKitItems(Player player) {
    ItemStack weaponItem = new ItemStack(XMaterial.WOODEN_SWORD.parseMaterial());
    ItemMeta itemMeta = weaponItem.getItemMeta();
    itemMeta.setUnbreakable(true);
    weaponItem.setItemMeta(itemMeta);
    player.getInventory().addItem(weaponItem);
    player.getInventory().addItem(new ItemStack(XMaterial.COOKED_PORKCHOP.parseMaterial(), 8));
    ArmorHelper.setArmor(player, ArmorHelper.ArmorType.IRON);
    VersionUtils.setMaxHealth(player, 32.0);
    player.setHealth(32.0);
  }

  @Override
  public Material getMaterial() {
    return Material.IRON_CHESTPLATE;
  }

  @Override
  public void reStock(Player player) {
  }
}

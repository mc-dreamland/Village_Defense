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

package plugily.projects.villagedefense.kits.free;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugily.projects.minigamesbox.classic.handlers.language.MessageBuilder;
import plugily.projects.minigamesbox.classic.kits.basekits.FreeKit;
import plugily.projects.minigamesbox.classic.utils.helper.ArmorHelper;
import plugily.projects.minigamesbox.classic.utils.helper.WeaponHelper;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XItemStack;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;

import java.util.List;

/**
 * Created by Tom on 14/08/2014.
 */
public class KnightKit extends FreeKit {

  public KnightKit() {
    setName(new MessageBuilder("KIT_CONTENT_KNIGHT_NAME").asKey().build());
    setKey("Knight");
    List<String> description = getPlugin().getLanguageManager().getLanguageListFromKey("KIT_CONTENT_KNIGHT_DESCRIPTION");
    setDescription(description);
    getPlugin().getKitRegistry().registerKit(this);
    getPlugin().getKitRegistry().setDefaultKit(this);
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return true;
  }

  @Override
  public void giveKitItems(Player player) {
    ItemStack weaponItem = new ItemStack(XMaterial.WOODEN_SWORD.parseMaterial());
    ItemMeta itemMeta = weaponItem.getItemMeta();
    itemMeta.setUnbreakable(true);
    weaponItem.setItemMeta(itemMeta);
    player.getInventory().addItem(weaponItem);
    ArmorHelper.setArmor(player, ArmorHelper.ArmorType.LEATHER);
    player.getInventory().addItem(new ItemStack(XMaterial.COOKED_PORKCHOP.parseMaterial(), 8));

  }

  @Override
  public Material getMaterial() {
    return XMaterial.WOODEN_SWORD.parseMaterial();
  }

  @Override
  public void reStock(Player player) {
    //no restock items for this kit
  }
}

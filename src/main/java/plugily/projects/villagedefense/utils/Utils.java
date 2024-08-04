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

package plugily.projects.villagedefense.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugily.projects.minigamesbox.classic.utils.helper.MaterialUtils;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;

/**
 * Created by Tom on 29/07/2014.
 */
public class Utils {

  private Utils() {
  }

  public static ItemStack[] getFullInventory(Player player) {
    PlayerInventory inventory = player.getInventory();

    // 获取主要库存内容（不包括盔甲和副手）
    ItemStack[] mainInventory = inventory.getContents();

    // 获取盔甲栏内容
    ItemStack[] armorContents = inventory.getArmorContents();

    // 获取副手内容
    ItemStack offHandItem = inventory.getItemInOffHand();

    // 创建一个数组来保存所有的物品
    ItemStack[] fullInventory = new ItemStack[mainInventory.length + armorContents.length + 1];

    // 将主要库存内容复制到新数组
    System.arraycopy(mainInventory, 0, fullInventory, 0, mainInventory.length);

    // 将盔甲内容复制到新数组
    System.arraycopy(armorContents, 0, fullInventory, mainInventory.length, armorContents.length);

    // 将副手物品添加到新数组的最后一个位置
    fullInventory[fullInventory.length - 1] = offHandItem;

    return fullInventory;
  }

  public static Material getCachedDoor(Block block) {
    //material can not be cached as we allow other door types
    if(block == null) {
      return XMaterial.OAK_DOOR.parseMaterial();
    }
    return (MaterialUtils.isDoor(block.getType()) ? block.getType() : Material.AIR);
  }
}

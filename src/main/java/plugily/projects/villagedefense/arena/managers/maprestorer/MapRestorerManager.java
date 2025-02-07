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

package plugily.projects.villagedefense.arena.managers.maprestorer;

import org.bukkit.Location;
import org.bukkit.TreeSpecies;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Wolf;
import org.bukkit.material.Door;
import plugily.projects.minigamesbox.classic.arena.managers.PluginMapRestorerManager;
import plugily.projects.minigamesbox.classic.utils.version.xseries.XMaterial;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.utils.Utils;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Plajer
 * <p>
 * Created at 14.02.2019
 */
@SuppressWarnings("deprecation")
public class MapRestorerManager extends PluginMapRestorerManager {

  protected final Map<Location, Byte> doorBlocks = new LinkedHashMap<>();
  public final Arena arena;

  public MapRestorerManager(Arena arena) {
    super(arena);
    this.arena = arena;
  }

  public final void addDoor(Location location, byte data) {
    doorBlocks.put(location, data);
  }

  public final Map<Location, Byte> getGameDoorLocations() {
    return doorBlocks;
  }

  @Override
  public void fullyRestoreArena() {
    super.fullyRestoreArena();
    arena.setWave(1);
    restoreDoors();
    clearEnemiesFromArena();
    clearGolemsFromArena();
    clearVillagersFromArena();
    clearWolvesFromArena();
    clearDroppedEntities();
  }

  public final void clearEnemiesFromArena() {
    arena.getEnemySpawnManager().applyIdle(0);
    arena.getEnemies().forEach(Entity::remove);
    arena.getEnemies().clear();
  }

  public final void clearDroppedEntities() {
    for(Entity entity : arena.getPlugin().getBukkitHelper().getNearbyEntities(arena.getStartLocation(), 200)) {
      if(entity.getType() == EntityType.EXPERIENCE_ORB || entity.getType() == EntityType.DROPPED_ITEM) {
        entity.remove();
      }
    }
  }

  public final void clearGolemsFromArena() {
    List<IronGolem> ironGolems = new ArrayList<>(arena.getIronGolems());
    ironGolems.forEach(arena::removeIronGolem);
  }

  public final void clearVillagersFromArena() {
    arena.getVillagers().forEach(Entity::remove);
    arena.getVillagers().clear();
  }

  public final void clearWolvesFromArena() {
    List<Wolf> wolves = new ArrayList<>(arena.getWolves());
    wolves.forEach(arena::removeWolf);
  }

  public void restoreDoors() {
    int i = 0;
    for(Map.Entry<Location, Byte> entry : doorBlocks.entrySet()) {
      Block block = entry.getKey().getBlock();
      Byte doorData = entry.getValue();
      try {
        if(block.getType() != XMaterial.AIR.parseMaterial()) {
          i++;
          continue;
        }
        if(doorData == (byte) 8) {
          restoreTopHalfDoorPart(block);
          i++;
          continue;
        }
        restoreBottomHalfDoorPart(block, doorData);
        i++;
      } catch(Exception ex) {
        arena.getPlugin().getDebugger().debug(Level.WARNING, "Door has failed to load for arena {0} message {1} type {2} skipping!", arena.getId(), ex.getMessage(), ex.getCause());
      }
    }
    if(i != doorBlocks.size()) {
      arena.getPlugin().getDebugger().debug(Level.WARNING, "Failed to load doors for {0}! Expected {1} got {2}", arena.getId(), doorBlocks.size(), i);
    }
  }

  public void restoreTopHalfDoorPart(Block block) {
    block.setType(Utils.getCachedDoor(block));
    BlockState doorBlockState = block.getState();
    Door doorBlockData = new Door(TreeSpecies.GENERIC, arena.getPlugin().getBukkitHelper().getFacingByByte((byte) 8));

    doorBlockData.setTopHalf(true);
    doorBlockData.setFacingDirection(doorBlockData.getFacing());

    doorBlockState.setType(doorBlockData.getItemType());
    doorBlockState.setData(doorBlockData);

    doorBlockState.update(true);
  }

  public void restoreBottomHalfDoorPart(Block block, byte doorData) {
    block.setType(Utils.getCachedDoor(block));
    BlockState doorBlockState = block.getState();
    Door doorBlockData = new Door(TreeSpecies.GENERIC, arena.getPlugin().getBukkitHelper().getFacingByByte(doorData));

    doorBlockData.setTopHalf(false);
    doorBlockData.setFacingDirection(doorBlockData.getFacing());

    doorBlockState.setType(doorBlockData.getItemType());
    doorBlockState.setData(doorBlockData);

    doorBlockState.update(true);
  }

}

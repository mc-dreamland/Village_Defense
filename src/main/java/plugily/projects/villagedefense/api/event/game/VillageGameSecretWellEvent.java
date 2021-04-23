package plugily.projects.villagedefense.api.event.game;

import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import plugily.projects.villagedefense.api.event.VillageEvent;
import plugily.projects.villagedefense.arena.Arena;

/**
 * Called when the player drops the item to the secret well
 */
public class VillageGameSecretWellEvent extends VillageEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled = false;
    private final Item item;

    public VillageGameSecretWellEvent(Arena arena, Item item) {
        super(arena);
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Item getItem() {
        return item;
    }
}

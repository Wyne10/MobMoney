package org.bigcraft.mobmoney.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player is about to be credited a money drop from a mob kill.
 * <p>
 * The amount ultimately granted is {@code baseDrop * multiplier}, computed by
 * the caller after this event fires; listeners can adjust either value or
 * cancel the event to prevent any money from being granted.
 */
public class MoneyDropEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled = false;
    private Double baseDrop;
    private Double multiplier;

    /**
     * @param baseDrop the base currency amount before {@code multiplier} is applied
     * @param multiplier the multiplier applied on top of {@code baseDrop}
     */
    public MoneyDropEvent(@NotNull Player player, Double baseDrop, Double multiplier) {
        super(player);
        this.baseDrop = baseDrop;
        this.multiplier = multiplier;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public Double getBaseDrop() {
        return baseDrop;
    }

    public void setBaseDrop(Double baseDrop) {
        this.baseDrop = baseDrop;
    }

    public Double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}

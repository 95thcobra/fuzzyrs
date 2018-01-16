package com.rs.world.item;

import com.rs.player.Player;
import com.rs.world.WorldTile;

@SuppressWarnings("serial")
public class FloorItem extends Item {

    private WorldTile tile;
    private Player owner;
    private boolean invisible;
    private boolean grave;

    public FloorItem(final int id) {
        super(id);
    }

    public FloorItem(final Item item, final WorldTile tile, final Player owner,
                     final boolean underGrave, final boolean invisible) {
        super(item.getId(), item.getAmount());
        this.tile = tile;
        this.owner = owner;
        grave = underGrave;
        this.invisible = invisible;
    }

    @Override
    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public WorldTile getTile() {
        return tile;
    }

    public boolean isGrave() {
        return grave;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(final boolean invisible) {
        this.invisible = invisible;
    }

    public Player getOwner() {
        return owner;
    }

}

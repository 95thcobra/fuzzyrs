package com.rs.world.item;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.cache.loaders.ItemsEquipIds;

import java.io.Serializable;

/**
 * Represents a single item.
 * <p>
 *
 * @author Graham / edited by Dragonkk(Alex)
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -6485003878697568087L;
    protected int amount;
    private short id;

    public Item(final int id) {
        this(id, 1);
    }

    public Item(final int id, final int amount) {
        this(id, amount, false);
    }

    public Item(final int id, final int amount, final boolean amt0) {
        this.id = (short) id;
        this.amount = amount;
        if (this.amount <= 0 && !amt0) {
            this.amount = 1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = (short) id;
    }

    @Override
    public Item clone() {
        return new Item(id, amount);
    }

    public ItemDefinitions getDefinitions() {
        return ItemDefinitions.getItemDefinitions(id);
    }

    public int getEquipId() {
        return ItemsEquipIds.getEquipId(id);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public String getName() {
        return getDefinitions().getName();
    }

}

package com.me.steel.Domain.Enums;

/**
 * An item that can be added to the ship.
 */
public interface Item
{
    /**
     * Retrieves the name of this item.
     */
    String getName();

    /**
     * Retrieves a simple representation of the item's name, useful for creating
     * a convention for file names.
     */
    String getSimpleName();

    float getAttackPower();

    float getMagicPower();
    
    float getDefensePower();
}

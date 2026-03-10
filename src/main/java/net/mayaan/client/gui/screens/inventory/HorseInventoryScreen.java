/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.EntityTypeTags;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.animal.equine.AbstractHorse;
import net.mayaan.world.entity.animal.equine.Llama;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.HorseInventoryMenu;
import org.jspecify.annotations.Nullable;

public class HorseInventoryScreen
extends AbstractMountInventoryScreen<HorseInventoryMenu> {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    private static final Identifier CHEST_SLOTS_SPRITE = Identifier.withDefaultNamespace("container/horse/chest_slots");
    private static final Identifier HORSE_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/horse.png");

    public HorseInventoryScreen(HorseInventoryMenu menu, Inventory inventory, AbstractHorse horse, int inventoryColumns) {
        super(menu, inventory, horse.getDisplayName(), inventoryColumns, horse);
    }

    @Override
    protected Identifier getBackgroundTextureLocation() {
        return HORSE_INVENTORY_LOCATION;
    }

    @Override
    protected Identifier getSlotSpriteLocation() {
        return SLOT_SPRITE;
    }

    @Override
    protected @Nullable Identifier getChestSlotsSpriteLocation() {
        return CHEST_SLOTS_SPRITE;
    }

    @Override
    protected boolean shouldRenderSaddleSlot() {
        return this.mount.canUseSlot(EquipmentSlot.SADDLE) && this.mount.is(EntityTypeTags.CAN_EQUIP_SADDLE);
    }

    @Override
    protected boolean shouldRenderArmorSlot() {
        return this.mount.canUseSlot(EquipmentSlot.BODY) && (this.mount.is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || this.mount instanceof Llama);
    }
}


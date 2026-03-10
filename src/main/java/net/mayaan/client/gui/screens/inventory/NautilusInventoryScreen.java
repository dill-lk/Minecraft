/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.inventory;

import net.mayaan.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.animal.nautilus.AbstractNautilus;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.NautilusInventoryMenu;
import org.jspecify.annotations.Nullable;

public class NautilusInventoryScreen
extends AbstractMountInventoryScreen<NautilusInventoryMenu> {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    private static final Identifier NAUTILUS_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/nautilus.png");

    public NautilusInventoryScreen(NautilusInventoryMenu menu, Inventory inventory, AbstractNautilus nautilus, int inventoryColumns) {
        super(menu, inventory, nautilus.getDisplayName(), inventoryColumns, nautilus);
    }

    @Override
    protected Identifier getBackgroundTextureLocation() {
        return NAUTILUS_INVENTORY_LOCATION;
    }

    @Override
    protected Identifier getSlotSpriteLocation() {
        return SLOT_SPRITE;
    }

    @Override
    protected @Nullable Identifier getChestSlotsSpriteLocation() {
        return null;
    }

    @Override
    protected boolean shouldRenderSaddleSlot() {
        return this.mount.canUseSlot(EquipmentSlot.SADDLE);
    }

    @Override
    protected boolean shouldRenderArmorSlot() {
        return this.mount.canUseSlot(EquipmentSlot.BODY);
    }
}


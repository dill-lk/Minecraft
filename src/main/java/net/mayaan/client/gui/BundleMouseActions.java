/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2i
 */
package net.mayaan.client.gui;

import net.mayaan.client.Mayaan;
import net.mayaan.client.ScrollWheelHandler;
import net.mayaan.client.gui.ItemSlotMouseAction;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.network.protocol.game.ServerboundSelectBundleItemPacket;
import net.mayaan.tags.ItemTags;
import net.mayaan.world.inventory.ContainerInput;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.BundleItem;
import net.mayaan.world.item.ItemStack;
import org.joml.Vector2i;

public class BundleMouseActions
implements ItemSlotMouseAction {
    private final Mayaan minecraft;
    private final ScrollWheelHandler scrollWheelHandler;

    public BundleMouseActions(Mayaan minecraft) {
        this.minecraft = minecraft;
        this.scrollWheelHandler = new ScrollWheelHandler();
    }

    @Override
    public boolean matches(Slot slot) {
        return slot.getItem().is(ItemTags.BUNDLES);
    }

    @Override
    public boolean onMouseScrolled(double scrollX, double scrollY, int slotIndex, ItemStack itemStack) {
        int updatedSelectedItem;
        int selectedItem;
        int wheel;
        int amountOfShownItems = BundleItem.getNumberOfItemsToShow(itemStack);
        if (amountOfShownItems == 0) {
            return false;
        }
        Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scrollX, scrollY);
        int n = wheel = wheelXY.y == 0 ? -wheelXY.x : wheelXY.y;
        if (wheel != 0 && (selectedItem = BundleItem.getSelectedItemIndex(itemStack)) != (updatedSelectedItem = ScrollWheelHandler.getNextScrollWheelSelection(wheel, selectedItem, amountOfShownItems))) {
            this.toggleSelectedBundleItem(itemStack, slotIndex, updatedSelectedItem);
        }
        return true;
    }

    @Override
    public void onStopHovering(Slot hoveredSlot) {
        this.unselectedBundleItem(hoveredSlot.getItem(), hoveredSlot.index);
    }

    @Override
    public void onSlotClicked(Slot slot, ContainerInput containerInput) {
        if (containerInput == ContainerInput.QUICK_MOVE || containerInput == ContainerInput.SWAP) {
            this.unselectedBundleItem(slot.getItem(), slot.index);
        }
    }

    private void toggleSelectedBundleItem(ItemStack bundleItem, int slotIndex, int selectedItem) {
        if (this.minecraft.getConnection() != null && selectedItem < BundleItem.getNumberOfItemsToShow(bundleItem)) {
            ClientPacketListener connection = this.minecraft.getConnection();
            BundleItem.toggleSelectedItem(bundleItem, selectedItem);
            connection.send(new ServerboundSelectBundleItemPacket(slotIndex, selectedItem));
        }
    }

    public void unselectedBundleItem(ItemStack bundleItem, int slotIndex) {
        this.toggleSelectedBundleItem(bundleItem, slotIndex, -1);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap
 *  it.unimi.dsi.fastutil.objects.Reference2ObjectMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.recipebook;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import java.util.List;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.recipebook.SlotSelectTime;
import net.mayaan.core.component.DataComponents;
import net.mayaan.util.context.ContextMap;
import net.mayaan.world.inventory.Slot;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.Nullable;

public class GhostSlots {
    private final Reference2ObjectMap<Slot, GhostSlot> ingredients = new Reference2ObjectArrayMap();
    private final SlotSelectTime slotSelectTime;

    public GhostSlots(SlotSelectTime slotSelectTime) {
        this.slotSelectTime = slotSelectTime;
    }

    public void clear() {
        this.ingredients.clear();
    }

    private void setSlot(Slot slot, ContextMap context, SlotDisplay contents, boolean isResult) {
        List<ItemStack> entries = contents.resolveForStacks(context);
        if (!entries.isEmpty()) {
            this.ingredients.put((Object)slot, (Object)new GhostSlot(entries, isResult));
        }
    }

    protected void setInput(Slot slot, ContextMap context, SlotDisplay contents) {
        this.setSlot(slot, context, contents, false);
    }

    protected void setResult(Slot slot, ContextMap context, SlotDisplay contents) {
        this.setSlot(slot, context, contents, true);
    }

    public void render(GuiGraphics graphics, Mayaan minecraft, boolean isResultSlotBig) {
        this.ingredients.forEach((slot, ingredient) -> {
            int x = slot.x;
            int y = slot.y;
            if (ingredient.isResultSlot && isResultSlotBig) {
                graphics.fill(x - 4, y - 4, x + 20, y + 20, 0x30FF0000);
            } else {
                graphics.fill(x, y, x + 16, y + 16, 0x30FF0000);
            }
            ItemStack itemStack = ingredient.getItem(this.slotSelectTime.currentIndex());
            graphics.renderFakeItem(itemStack, x, y);
            graphics.fill(x, y, x + 16, y + 16, 0x30FFFFFF);
            if (ingredient.isResultSlot) {
                graphics.renderItemDecorations(minecraft.font, itemStack, x, y);
            }
        });
    }

    public void renderTooltip(GuiGraphics graphics, Mayaan minecraft, int mouseX, int mouseY, @Nullable Slot hoveredSlot) {
        if (hoveredSlot == null) {
            return;
        }
        GhostSlot hoveredGhostSlot = (GhostSlot)this.ingredients.get((Object)hoveredSlot);
        if (hoveredGhostSlot != null) {
            ItemStack hoveredItem = hoveredGhostSlot.getItem(this.slotSelectTime.currentIndex());
            graphics.setComponentTooltipForNextFrame(minecraft.font, Screen.getTooltipFromItem(minecraft, hoveredItem), mouseX, mouseY, hoveredItem.get(DataComponents.TOOLTIP_STYLE));
        }
    }

    private record GhostSlot(List<ItemStack> items, boolean isResultSlot) {
        public ItemStack getItem(int itemIndex) {
            int size = this.items.size();
            if (size == 0) {
                return ItemStack.EMPTY;
            }
            return this.items.get(itemIndex % size);
        }
    }
}


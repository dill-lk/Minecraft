/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.tutorial;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.toasts.TutorialToast;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.tutorial.Tutorial;
import net.mayaan.client.tutorial.TutorialStepInstance;
import net.mayaan.client.tutorial.TutorialSteps;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.network.chat.Component;
import net.mayaan.stats.Stats;
import net.mayaan.tags.ItemTags;
import net.mayaan.tags.TagKey;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPlanksTutorialStep
implements TutorialStepInstance {
    private static final int HINT_DELAY = 1200;
    private static final Component CRAFT_TITLE = Component.translatable("tutorial.craft_planks.title");
    private static final Component CRAFT_DESCRIPTION = Component.translatable("tutorial.craft_planks.description");
    private final Tutorial tutorial;
    private @Nullable TutorialToast toast;
    private int timeWaiting;

    public CraftPlanksTutorialStep(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    @Override
    public void tick() {
        LocalPlayer player;
        ++this.timeWaiting;
        if (!this.tutorial.isSurvival()) {
            this.tutorial.setStep(TutorialSteps.NONE);
            return;
        }
        Mayaan minecraft = this.tutorial.getMinecraft();
        if (this.timeWaiting == 1 && (player = minecraft.player) != null) {
            if (player.getInventory().contains(ItemTags.PLANKS)) {
                this.tutorial.setStep(TutorialSteps.NONE);
                return;
            }
            if (CraftPlanksTutorialStep.hasCraftedPlanksPreviously(player, ItemTags.PLANKS)) {
                this.tutorial.setStep(TutorialSteps.NONE);
                return;
            }
        }
        if (this.timeWaiting >= 1200 && this.toast == null) {
            this.toast = new TutorialToast(minecraft.font, TutorialToast.Icons.WOODEN_PLANKS, CRAFT_TITLE, CRAFT_DESCRIPTION, false);
            minecraft.getToastManager().addToast(this.toast);
        }
    }

    @Override
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }

    @Override
    public void onGetItem(ItemStack itemStack) {
        if (itemStack.is(ItemTags.PLANKS)) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
    }

    public static boolean hasCraftedPlanksPreviously(LocalPlayer player, TagKey<Item> tag) {
        for (Holder<Item> item : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
            if (player.getStats().getValue(Stats.ITEM_CRAFTED.get(item.value())) <= 0) continue;
            return true;
        }
        return false;
    }
}


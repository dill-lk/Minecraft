/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.tutorial;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.player.ClientInput;
import net.mayaan.core.BlockPos;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.HitResult;

public interface TutorialStepInstance {
    default public void clear() {
    }

    default public void tick() {
    }

    default public void onInput(ClientInput input) {
    }

    default public void onMouse(double xd, double yd) {
    }

    default public void onLookAt(ClientLevel level, HitResult hit) {
    }

    default public void onDestroyBlock(ClientLevel level, BlockPos pos, BlockState state, float percent) {
    }

    default public void onOpenInventory() {
    }

    default public void onGetItem(ItemStack itemStack) {
    }
}


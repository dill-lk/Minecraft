/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.HoldingEntityRenderState;
import net.mayaan.client.renderer.entity.state.VillagerDataHolderRenderState;
import net.mayaan.world.entity.npc.villager.VillagerData;
import org.jspecify.annotations.Nullable;

public class VillagerRenderState
extends HoldingEntityRenderState
implements VillagerDataHolderRenderState {
    public boolean isUnhappy;
    public @Nullable VillagerData villagerData;

    @Override
    public @Nullable VillagerData getVillagerData() {
        return this.villagerData;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface BucketPickup {
    public ItemStack pickupBlock(@Nullable LivingEntity var1, LevelAccessor var2, BlockPos var3, BlockState var4);

    public Optional<SoundEvent> getPickupSound();
}


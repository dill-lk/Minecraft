/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.boss.enderdragon.phases;

import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.boss.enderdragon.EndCrystal;
import net.mayaan.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface DragonPhaseInstance {
    public boolean isSitting();

    public void doClientTick();

    public void doServerTick(ServerLevel var1);

    public void onCrystalDestroyed(EndCrystal var1, BlockPos var2, DamageSource var3, @Nullable Player var4);

    public void begin();

    public void end();

    public float getFlySpeed();

    public float getTurnSpeed();

    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase();

    public @Nullable Vec3 getFlyTargetLocation();

    public float onHurt(DamageSource var1, float var2);
}


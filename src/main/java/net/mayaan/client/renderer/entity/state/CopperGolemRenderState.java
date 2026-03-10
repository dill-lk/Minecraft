/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.state.ArmedEntityRenderState;
import net.mayaan.world.entity.AnimationState;
import net.mayaan.world.entity.animal.golem.CopperGolemState;
import net.mayaan.world.level.block.WeatheringCopper;

public class CopperGolemRenderState
extends ArmedEntityRenderState {
    public WeatheringCopper.WeatherState weathering = WeatheringCopper.WeatherState.UNAFFECTED;
    public CopperGolemState copperGolemState = CopperGolemState.IDLE;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState interactionGetItem = new AnimationState();
    public final AnimationState interactionGetNoItem = new AnimationState();
    public final AnimationState interactionDropItem = new AnimationState();
    public final AnimationState interactionDropNoItem = new AnimationState();
    public final BlockModelRenderState blockOnAntenna = new BlockModelRenderState();
}


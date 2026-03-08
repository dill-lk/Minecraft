/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.CopperGolemStatueBlock;
import net.mayaan.world.level.block.WeatheringCopper;

public class CopperGolemStatueRenderState
extends BlockEntityRenderState {
    public CopperGolemStatueBlock.Pose pose = CopperGolemStatueBlock.Pose.STANDING;
    public Direction direction = Direction.NORTH;
    public WeatheringCopper.WeatherState oxidationState = WeatheringCopper.WeatherState.UNAFFECTED;
}


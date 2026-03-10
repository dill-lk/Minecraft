/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.state.MapRenderState;
import net.mayaan.core.Direction;
import net.mayaan.world.level.saveddata.maps.MapId;
import org.jspecify.annotations.Nullable;

public class ItemFrameRenderState
extends EntityRenderState {
    public Direction direction = Direction.NORTH;
    public final BlockModelRenderState frameModel = new BlockModelRenderState();
    public final ItemStackRenderState item = new ItemStackRenderState();
    public int rotation;
    public boolean isGlowFrame;
    public @Nullable MapId mapId;
    public final MapRenderState mapRenderState = new MapRenderState();
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity.state;

import java.util.Collections;
import java.util.List;
import net.mayaan.client.renderer.blockentity.state.BlockEntityRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.core.Direction;

public class CampfireRenderState
extends BlockEntityRenderState {
    public List<ItemStackRenderState> items = Collections.emptyList();
    public Direction facing = Direction.NORTH;
}


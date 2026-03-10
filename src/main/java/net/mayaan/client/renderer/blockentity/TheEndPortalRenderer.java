/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.blockentity;

import net.mayaan.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.mayaan.client.renderer.blockentity.state.EndPortalRenderState;
import net.mayaan.world.level.block.entity.TheEndPortalBlockEntity;

public class TheEndPortalRenderer
extends AbstractEndPortalRenderer<TheEndPortalBlockEntity, EndPortalRenderState> {
    @Override
    public EndPortalRenderState createRenderState() {
        return new EndPortalRenderState();
    }
}


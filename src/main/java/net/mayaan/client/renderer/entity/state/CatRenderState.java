/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.FelineRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.DyeColor;
import org.jspecify.annotations.Nullable;

public class CatRenderState
extends FelineRenderState {
    private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/cat/cat_tabby.png");
    public Identifier texture = DEFAULT_TEXTURE;
    public boolean isLyingOnTopOfSleepingPlayer;
    public @Nullable DyeColor collarColor;
}


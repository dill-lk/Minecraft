/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.gui.pip;

import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record GuiSkinRenderState(PlayerModel playerModel, Identifier texture, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState
{
    public GuiSkinRenderState(PlayerModel playerModel, Identifier texture, float rotationX, float rotationY, float pivotY, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea) {
        this(playerModel, texture, rotationX, rotationY, pivotY, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}


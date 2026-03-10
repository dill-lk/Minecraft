/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.gui.pip;

import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.model.Model;
import net.mayaan.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.mayaan.world.level.block.state.properties.WoodType;
import org.jspecify.annotations.Nullable;

public record GuiSignRenderState(Model.Simple signModel, WoodType woodType, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState
{
    public GuiSignRenderState(Model.Simple signModel, WoodType woodType, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea) {
        this(signModel, woodType, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}


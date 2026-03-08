/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.state.gui.pip;

import net.mayaan.client.gui.navigation.ScreenRectangle;
import net.mayaan.client.model.object.banner.BannerFlagModel;
import net.mayaan.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.level.block.entity.BannerPatternLayers;
import org.jspecify.annotations.Nullable;

public record GuiBannerResultRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState
{
    public GuiBannerResultRenderState(BannerFlagModel flag, DyeColor baseColor, BannerPatternLayers resultBannerPatterns, int x0, int y0, int x1, int y1, @Nullable ScreenRectangle scissorArea) {
        this(flag, baseColor, resultBannerPatterns, x0, y0, x1, y1, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }

    @Override
    public float scale() {
        return 16.0f;
    }
}


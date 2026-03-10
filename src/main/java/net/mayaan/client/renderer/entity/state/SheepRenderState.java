/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.color.ColorLerper;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.item.DyeColor;

public class SheepRenderState
extends LivingEntityRenderState {
    public float headEatPositionScale;
    public float headEatAngleScale;
    public boolean isSheared;
    public DyeColor woolColor = DyeColor.WHITE;
    public boolean isJebSheep;

    public int getWoolColor() {
        if (this.isJebSheep) {
            return ColorLerper.getLerpedColor(ColorLerper.Type.SHEEP, this.ageInTicks);
        }
        return ColorLerper.Type.SHEEP.getColor(this.woolColor);
    }
}


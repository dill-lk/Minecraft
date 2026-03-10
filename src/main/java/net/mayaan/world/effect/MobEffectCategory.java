/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.effect;

import net.mayaan.ChatFormatting;

public enum MobEffectCategory {
    BENEFICIAL(ChatFormatting.BLUE),
    HARMFUL(ChatFormatting.RED),
    NEUTRAL(ChatFormatting.BLUE);

    private final ChatFormatting tooltipFormatting;

    private MobEffectCategory(ChatFormatting tooltipFormatting) {
        this.tooltipFormatting = tooltipFormatting;
    }

    public ChatFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}


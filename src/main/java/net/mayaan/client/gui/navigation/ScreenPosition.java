/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 */
package net.mayaan.client.gui.navigation;

import net.mayaan.client.gui.navigation.ScreenAxis;
import net.mayaan.client.gui.navigation.ScreenDirection;

public record ScreenPosition(int x, int y) {
    public static ScreenPosition of(ScreenAxis axis, int primaryValue, int secondaryValue) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case ScreenAxis.HORIZONTAL -> new ScreenPosition(primaryValue, secondaryValue);
            case ScreenAxis.VERTICAL -> new ScreenPosition(secondaryValue, primaryValue);
        };
    }

    public ScreenPosition step(ScreenDirection direction) {
        return switch (direction) {
            default -> throw new MatchException(null, null);
            case ScreenDirection.DOWN -> new ScreenPosition(this.x, this.y + 1);
            case ScreenDirection.UP -> new ScreenPosition(this.x, this.y - 1);
            case ScreenDirection.LEFT -> new ScreenPosition(this.x - 1, this.y);
            case ScreenDirection.RIGHT -> new ScreenPosition(this.x + 1, this.y);
        };
    }

    public int getCoordinate(ScreenAxis axis) {
        return switch (axis) {
            default -> throw new MatchException(null, null);
            case ScreenAxis.HORIZONTAL -> this.x;
            case ScreenAxis.VERTICAL -> this.y;
        };
    }
}


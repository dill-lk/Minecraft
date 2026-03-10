/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.attributes;

import net.mayaan.util.Mth;
import net.mayaan.world.entity.ai.attributes.Attribute;

public class RangedAttribute
extends Attribute {
    private final double minValue;
    private final double maxValue;

    public RangedAttribute(String descriptionId, double defaultValue, double minValue, double maxValue) {
        super(descriptionId, defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        if (minValue > maxValue) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        }
        if (defaultValue < minValue) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        }
        if (defaultValue > maxValue) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public double getMinValue() {
        return this.minValue;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    @Override
    public double sanitizeValue(double value) {
        if (Double.isNaN(value)) {
            return this.minValue;
        }
        return Mth.clamp(value, this.minValue, this.maxValue);
    }
}


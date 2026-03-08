/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.valueproviders;

import java.util.Arrays;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.SampledFloat;

public class MultipliedFloats
implements SampledFloat {
    private final SampledFloat[] values;

    public MultipliedFloats(SampledFloat ... values) {
        this.values = values;
    }

    @Override
    public float sample(RandomSource random) {
        float result = 1.0f;
        for (SampledFloat value : this.values) {
            result *= value.sample(random);
        }
        return result;
    }

    public String toString() {
        return "MultipliedFloats" + Arrays.toString(this.values);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.attribute;

import net.mayaan.core.BlockPos;
import net.mayaan.world.attribute.EnvironmentAttribute;
import net.mayaan.world.attribute.SpatialAttributeInterpolator;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface EnvironmentAttributeReader {
    public static final EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader(){

        @Override
        public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
            return attribute.defaultValue();
        }

        @Override
        public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, @Nullable SpatialAttributeInterpolator biomeInterpolator) {
            return attribute.defaultValue();
        }
    };

    public <Value> Value getDimensionValue(EnvironmentAttribute<Value> var1);

    default public <Value> Value getValue(EnvironmentAttribute<Value> attribute, BlockPos pos) {
        return this.getValue(attribute, Vec3.atCenterOf(pos));
    }

    default public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos) {
        return this.getValue(attribute, pos, null);
    }

    public <Value> Value getValue(EnvironmentAttribute<Value> var1, Vec3 var2, @Nullable SpatialAttributeInterpolator var3);
}


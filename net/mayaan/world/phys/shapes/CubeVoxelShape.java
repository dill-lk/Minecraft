/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.doubles.DoubleList
 */
package net.mayaan.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.mayaan.core.Direction;
import net.mayaan.util.Mth;
import net.mayaan.world.phys.shapes.CubePointRange;
import net.mayaan.world.phys.shapes.DiscreteVoxelShape;
import net.mayaan.world.phys.shapes.VoxelShape;

public final class CubeVoxelShape
extends VoxelShape {
    protected CubeVoxelShape(DiscreteVoxelShape shape) {
        super(shape);
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {
        return new CubePointRange(this.shape.getSize(axis));
    }

    @Override
    protected int findIndex(Direction.Axis axis, double coord) {
        int size = this.shape.getSize(axis);
        return Mth.floor(Mth.clamp(coord * (double)size, -1.0, (double)size));
    }
}


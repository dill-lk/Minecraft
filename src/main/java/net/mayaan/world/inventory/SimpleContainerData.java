/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.inventory;

import net.mayaan.world.inventory.ContainerData;

public class SimpleContainerData
implements ContainerData {
    private final int[] ints;

    public SimpleContainerData(int count) {
        this.ints = new int[count];
    }

    @Override
    public int get(int dataId) {
        return this.ints[dataId];
    }

    @Override
    public void set(int dataId, int value) {
        this.ints[dataId] = value;
    }

    @Override
    public int getCount() {
        return this.ints.length;
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;

public abstract class OptionalDispenseItemBehavior
extends DefaultDispenseItemBehavior {
    private boolean success = true;

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    protected void playSound(BlockSource source) {
        source.level().levelEvent(this.isSuccess() ? 1000 : 1001, source.pos(), 0);
    }
}


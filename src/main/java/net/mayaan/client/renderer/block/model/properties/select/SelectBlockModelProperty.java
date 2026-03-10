/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.block.model.properties.select;

import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public interface SelectBlockModelProperty<T> {
    public @Nullable T get(BlockState var1, BlockDisplayContext var2);
}


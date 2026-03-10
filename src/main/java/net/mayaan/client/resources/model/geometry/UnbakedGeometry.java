/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.model.geometry;

import net.mayaan.client.renderer.block.dispatch.ModelState;
import net.mayaan.client.resources.model.ModelBaker;
import net.mayaan.client.resources.model.ModelDebugName;
import net.mayaan.client.resources.model.geometry.QuadCollection;
import net.mayaan.client.resources.model.sprite.TextureSlots;

@FunctionalInterface
public interface UnbakedGeometry {
    public static final UnbakedGeometry EMPTY = (textureSlots, modelBaker, modelState, modelDebugName) -> QuadCollection.EMPTY;

    public QuadCollection bake(TextureSlots var1, ModelBaker var2, ModelState var3, ModelDebugName var4);
}


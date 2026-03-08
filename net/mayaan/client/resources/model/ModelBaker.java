/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3fc
 */
package net.mayaan.client.resources.model;

import net.mayaan.client.renderer.block.dispatch.BlockStateModelPart;
import net.mayaan.client.resources.model.ResolvedModel;
import net.mayaan.client.resources.model.geometry.BakedQuad;
import net.mayaan.client.resources.model.sprite.MaterialBaker;
import net.mayaan.resources.Identifier;
import org.joml.Vector3fc;

public interface ModelBaker {
    public ResolvedModel getModel(Identifier var1);

    public BlockStateModelPart missingBlockModelPart();

    public MaterialBaker materials();

    public Interner interner();

    public <T> T compute(SharedOperationKey<T> var1);

    public static interface Interner {
        public Vector3fc vector(Vector3fc var1);

        public BakedQuad.SpriteInfo spriteInfo(BakedQuad.SpriteInfo var1);
    }

    @FunctionalInterface
    public static interface SharedOperationKey<T> {
        public T compute(ModelBaker var1);
    }
}


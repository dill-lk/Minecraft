/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.block.dispatch;

import java.util.List;
import net.mayaan.client.resources.model.ModelBaker;
import net.mayaan.client.resources.model.ResolvableModel;
import net.mayaan.client.resources.model.geometry.BakedQuad;
import net.mayaan.client.resources.model.sprite.Material;
import net.mayaan.core.Direction;
import org.jspecify.annotations.Nullable;

public interface BlockStateModelPart {
    public List<BakedQuad> getQuads(@Nullable Direction var1);

    public boolean useAmbientOcclusion();

    public Material.Baked particleMaterial();

    public boolean hasTranslucency();

    public static interface Unbaked
    extends ResolvableModel {
        public BlockStateModelPart bake(ModelBaker var1);
    }
}


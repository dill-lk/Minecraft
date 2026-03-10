/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4fc
 */
package net.mayaan.client.renderer.block.model;

import java.util.function.Function;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.block.dispatch.BlockStateModel;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.world.level.block.state.BlockState;
import org.joml.Matrix4fc;

public interface BlockModel {
    public void update(BlockModelRenderState var1, BlockState var2, BlockDisplayContext var3, long var4);

    public record BakingContext(EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, Function<BlockState, BlockStateModel> modelGetter, BlockModel missingBlockModel) implements SpecialModelRenderer.BakingContext
    {
    }

    public static interface Unbaked {
        public BlockModel bake(BakingContext var1, Matrix4fc var2);
    }
}


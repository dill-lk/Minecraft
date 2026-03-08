/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Suppliers
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  it.unimi.dsi.fastutil.ints.IntList
 *  org.joml.Matrix4fc
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.maayanlabs.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import net.mayaan.client.color.item.ItemTintSource;
import net.mayaan.client.color.item.ItemTintSources;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.block.dispatch.BlockModelRotation;
import net.mayaan.client.renderer.item.ItemModel;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.item.ModelRenderProperties;
import net.mayaan.client.renderer.texture.TextureAtlas;
import net.mayaan.client.resources.model.ModelBaker;
import net.mayaan.client.resources.model.ResolvableModel;
import net.mayaan.client.resources.model.ResolvedModel;
import net.mayaan.client.resources.model.geometry.BakedQuad;
import net.mayaan.client.resources.model.sprite.TextureSlots;
import net.mayaan.resources.Identifier;
import net.mayaan.tags.ItemTags;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class CuboidItemModelWrapper
implements ItemModel {
    private final List<ItemTintSource> tints;
    private final List<BakedQuad> quads;
    private final Supplier<Vector3fc[]> extents;
    private final ModelRenderProperties properties;
    private final Matrix4fc transformation;
    private final boolean animated;

    private CuboidItemModelWrapper(List<ItemTintSource> tints, List<BakedQuad> quads, ModelRenderProperties properties, Matrix4fc transformation) {
        this.tints = tints;
        this.quads = quads;
        this.properties = properties;
        this.transformation = transformation;
        this.extents = Suppliers.memoize(() -> CuboidItemModelWrapper.computeExtents(this.quads));
        boolean animated = false;
        for (BakedQuad quad : quads) {
            if (!quad.spriteInfo().sprite().contents().isAnimated()) continue;
            animated = true;
            break;
        }
        this.animated = animated;
    }

    public static Vector3fc[] computeExtents(List<BakedQuad> quads) {
        HashSet<Vector3fc> result = new HashSet<Vector3fc>();
        for (BakedQuad quad : quads) {
            for (int vertex = 0; vertex < 4; ++vertex) {
                result.add(quad.position(vertex));
            }
        }
        return (Vector3fc[])result.toArray(Vector3fc[]::new);
    }

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);
        ItemStackRenderState.LayerRenderState layer = output.newLayer();
        if (item.hasFoil()) {
            ItemStackRenderState.FoilType foilType = CuboidItemModelWrapper.hasSpecialAnimatedTexture(item) ? ItemStackRenderState.FoilType.SPECIAL : ItemStackRenderState.FoilType.STANDARD;
            layer.setFoilType(foilType);
            output.setAnimated();
            output.appendModelIdentityElement((Object)foilType);
        }
        if (!this.tints.isEmpty()) {
            IntList tintLayers = layer.tintLayers();
            for (ItemTintSource tintSource : this.tints) {
                int tint = tintSource.calculate(item, level, owner == null ? null : owner.asLivingEntity());
                tintLayers.add(tint);
                output.appendModelIdentityElement(tint);
            }
        }
        layer.setExtents(this.extents);
        layer.setLocalTransform(this.transformation);
        this.properties.applyToLayer(layer, displayContext);
        layer.prepareQuadList().addAll(this.quads);
        if (this.animated) {
            output.setAnimated();
        }
    }

    private static void validateAtlasUsage(List<BakedQuad> quads) {
        Iterator<BakedQuad> quadIterator = quads.iterator();
        if (!quadIterator.hasNext()) {
            return;
        }
        Identifier expectedAtlas = quadIterator.next().spriteInfo().sprite().atlasLocation();
        while (quadIterator.hasNext()) {
            BakedQuad quad = quadIterator.next();
            Identifier quadAtlas = quad.spriteInfo().sprite().atlasLocation();
            if (quadAtlas.equals(expectedAtlas)) continue;
            throw new IllegalStateException("Multiple atlases used in model, expected " + String.valueOf(expectedAtlas) + ", but also got " + String.valueOf(quadAtlas));
        }
        if (!expectedAtlas.equals(TextureAtlas.LOCATION_ITEMS) && !expectedAtlas.equals(TextureAtlas.LOCATION_BLOCKS)) {
            throw new IllegalArgumentException("Atlas " + String.valueOf(expectedAtlas) + " can't be usef for item models");
        }
    }

    private static boolean hasSpecialAnimatedTexture(ItemStack itemStack) {
        return itemStack.is(ItemTags.COMPASSES) || itemStack.is(Items.CLOCK);
    }

    public record Unbaked(Identifier model, Optional<Transformation> transformation, List<ItemTintSource> tints) implements ItemModel.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model), (App)Transformation.EXTENDED_CODEC.optionalFieldOf("transformation").forGetter(Unbaked::transformation), (App)ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of()).forGetter(Unbaked::tints)).apply((Applicative)i, Unbaked::new));

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.model);
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            ModelBaker baker = context.blockModelBaker();
            ResolvedModel resolvedModel = baker.getModel(this.model);
            TextureSlots textureSlots = resolvedModel.getTopTextureSlots();
            List<BakedQuad> quads = resolvedModel.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY).getAll();
            ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, textureSlots);
            CuboidItemModelWrapper.validateAtlasUsage(quads);
            Matrix4fc modelTransform = Transformation.compose(transformation, this.transformation);
            return new CuboidItemModelWrapper(this.tints, quads, properties, modelTransform);
        }

        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}


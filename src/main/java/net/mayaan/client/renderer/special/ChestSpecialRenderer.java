/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  java.lang.MatchException
 *  org.joml.Vector3fc
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.chest.ChestModel;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.level.block.state.properties.ChestType;
import org.joml.Vector3fc;

public class ChestSpecialRenderer
implements NoDataSpecialModelRenderer {
    public static final Identifier GIFT_CHEST_TEXTURE = Identifier.withDefaultNamespace("christmas");
    public static final Identifier NORMAL_CHEST_TEXTURE = Identifier.withDefaultNamespace("normal");
    public static final Identifier TRAPPED_CHEST_TEXTURE = Identifier.withDefaultNamespace("trapped");
    public static final Identifier ENDER_CHEST_TEXTURE = Identifier.withDefaultNamespace("ender");
    public static final Identifier COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper");
    public static final Identifier EXPOSED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_exposed");
    public static final Identifier WEATHERED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_weathered");
    public static final Identifier OXIDIZED_COPPER_CHEST_TEXTURE = Identifier.withDefaultNamespace("copper_oxidized");
    private final SpriteGetter sprites;
    private final ChestModel model;
    private final SpriteId sprite;
    private final float openness;

    public ChestSpecialRenderer(SpriteGetter sprites, ChestModel model, SpriteId sprite, float openness) {
        this.sprites = sprites;
        this.model = model;
        this.sprite = sprite;
        this.openness = openness;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        submitNodeCollector.submitModel(this.model, Float.valueOf(this.openness), poseStack, this.sprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, this.sprites.get(this.sprite), outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(Float.valueOf(this.openness));
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked(Identifier texture, float openness, ChestType chestType) implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)Codec.FLOAT.optionalFieldOf("openness", (Object)Float.valueOf(0.0f)).forGetter(Unbaked::openness), (App)ChestType.CODEC.optionalFieldOf("chest_type", (Object)ChestType.SINGLE).forGetter(Unbaked::chestType)).apply((Applicative)i, Unbaked::new));

        public Unbaked(Identifier texture, ChestType chestType) {
            this(texture, 0.0f, chestType);
        }

        public Unbaked(Identifier texture) {
            this(texture, 0.0f, ChestType.SINGLE);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public ChestSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            ChestModel model = new ChestModel(context.entityModelSet().bakeLayer(Unbaked.getModel(this.chestType)));
            SpriteId fullTexture = Sheets.CHEST_MAPPER.apply(this.texture);
            return new ChestSpecialRenderer(context.sprites(), model, fullTexture, this.openness);
        }

        private static ModelLayerLocation getModel(ChestType type) {
            return switch (type) {
                default -> throw new MatchException(null, null);
                case ChestType.SINGLE -> ModelLayers.CHEST;
                case ChestType.RIGHT -> ModelLayers.DOUBLE_CHEST_RIGHT;
                case ChestType.LEFT -> ModelLayers.DOUBLE_CHEST_LEFT;
            };
        }
    }
}


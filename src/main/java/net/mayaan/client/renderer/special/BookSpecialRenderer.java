/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
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
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.book.BookModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.EnchantTableRenderer;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

public class BookSpecialRenderer
implements NoDataSpecialModelRenderer {
    private final SpriteGetter sprites;
    private final BookModel model;
    private final BookModel.State state;

    public BookSpecialRenderer(SpriteGetter sprites, BookModel model, BookModel.State state) {
        this.sprites = sprites;
        this.model = model;
        this.state = state;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        SpriteId sprite = EnchantTableRenderer.BOOK_TEXTURE;
        submitNodeCollector.submitModel(this.model, this.state, poseStack, sprite.renderType(RenderTypes::entitySolid), lightCoords, overlayCoords, -1, this.sprites.get(sprite), outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.setupAnim(this.state);
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked(float openAngle, float page1, float page2) implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("open_angle").forGetter(Unbaked::openAngle), (App)Codec.FLOAT.fieldOf("page1").forGetter(Unbaked::page1), (App)Codec.FLOAT.fieldOf("page2").forGetter(Unbaked::page2)).apply((Applicative)i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public BookSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new BookSpecialRenderer(context.sprites(), new BookModel(context.entityModelSet().bakeLayer(ModelLayers.BOOK)), new BookModel.State(this.openAngle * ((float)Math.PI / 180), this.page1, this.page2));
        }
    }
}


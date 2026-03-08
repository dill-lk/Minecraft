/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.model.sprite;

import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import java.util.function.Function;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.entity.ItemRenderer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.resources.Identifier;

public record SpriteId(Identifier atlasLocation, Identifier texture) {
    public RenderType renderType(Function<Identifier, RenderType> renderType) {
        return renderType.apply(this.atlasLocation);
    }

    public VertexConsumer buffer(SpriteGetter sprites, MultiBufferSource bufferSource, Function<Identifier, RenderType> renderType) {
        return sprites.get(this).wrap(bufferSource.getBuffer(this.renderType(renderType)));
    }

    public VertexConsumer buffer(SpriteGetter sprites, MultiBufferSource bufferSource, Function<Identifier, RenderType> renderType, boolean sheeted, boolean hasFoil) {
        return sprites.get(this).wrap(ItemRenderer.getFoilBuffer(bufferSource, this.renderType(renderType), sheeted, hasFoil));
    }
}


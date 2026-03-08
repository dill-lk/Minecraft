/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.joml.Vector3fc
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BedRenderer;
import net.mayaan.client.renderer.special.NoDataSpecialModelRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.level.block.state.properties.BedPart;
import org.joml.Vector3fc;

public class BedSpecialRenderer
implements NoDataSpecialModelRenderer {
    private final BedRenderer bedRenderer;
    private final SpriteId sprite;
    private final BedPart part;

    public BedSpecialRenderer(BedRenderer bedRenderer, SpriteId sprite, BedPart part) {
        this.bedRenderer = bedRenderer;
        this.sprite = sprite;
        this.part = part;
    }

    @Override
    public void submit(ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        this.bedRenderer.submitPiece(this.part, this.sprite, poseStack, submitNodeCollector, lightCoords, overlayCoords, null, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        this.bedRenderer.getExtents(this.part, output);
    }

    public record Unbaked(Identifier texture, BedPart part) implements NoDataSpecialModelRenderer.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("texture").forGetter(Unbaked::texture), (App)BedPart.CODEC.fieldOf("part").forGetter(Unbaked::part)).apply((Applicative)i, Unbaked::new));

        public Unbaked(DyeColor dyeColor, BedPart part) {
            this(Sheets.colorToResourceSprite(dyeColor), part);
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public BedSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new BedSpecialRenderer(new BedRenderer(context), Sheets.BED_MAPPER.apply(this.texture), this.part);
        }
    }
}


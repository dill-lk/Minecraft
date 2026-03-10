/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import java.util.function.Consumer;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.BannerRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.BannerBlock;
import net.mayaan.world.level.block.entity.BannerPatternLayers;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class BannerSpecialRenderer
implements SpecialModelRenderer<BannerPatternLayers> {
    private final BannerRenderer bannerRenderer;
    private final DyeColor baseColor;
    private final BannerBlock.AttachmentType attachment;

    public BannerSpecialRenderer(DyeColor baseColor, BannerRenderer bannerRenderer, BannerBlock.AttachmentType attachment) {
        this.bannerRenderer = bannerRenderer;
        this.baseColor = baseColor;
        this.attachment = attachment;
    }

    @Override
    public @Nullable BannerPatternLayers extractArgument(ItemStack stack) {
        return stack.get(DataComponents.BANNER_PATTERNS);
    }

    @Override
    public void submit(@Nullable BannerPatternLayers patterns, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        this.bannerRenderer.submitSpecial(this.attachment, poseStack, submitNodeCollector, lightCoords, overlayCoords, this.baseColor, Objects.requireNonNullElse(patterns, BannerPatternLayers.EMPTY), outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        this.bannerRenderer.getExtents(output);
    }

    public record Unbaked(DyeColor baseColor, BannerBlock.AttachmentType attachment) implements SpecialModelRenderer.Unbaked<BannerPatternLayers>
    {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.fieldOf("color").forGetter(Unbaked::baseColor), (App)BannerBlock.AttachmentType.CODEC.optionalFieldOf("attachment", (Object)BannerBlock.AttachmentType.GROUND).forGetter(Unbaked::attachment)).apply((Applicative)i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public BannerSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new BannerSpecialRenderer(this.baseColor, new BannerRenderer(context), this.attachment);
        }
    }
}


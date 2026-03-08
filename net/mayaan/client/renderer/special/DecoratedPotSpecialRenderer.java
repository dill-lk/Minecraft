/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.function.Consumer;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.blockentity.DecoratedPotRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.core.component.DataComponents;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.entity.PotDecorations;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public class DecoratedPotSpecialRenderer
implements SpecialModelRenderer<PotDecorations> {
    private final DecoratedPotRenderer decoratedPotRenderer;

    public DecoratedPotSpecialRenderer(DecoratedPotRenderer decoratedPotRenderer) {
        this.decoratedPotRenderer = decoratedPotRenderer;
    }

    @Override
    public @Nullable PotDecorations extractArgument(ItemStack stack) {
        return stack.get(DataComponents.POT_DECORATIONS);
    }

    @Override
    public void submit(@Nullable PotDecorations decorations, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        this.decoratedPotRenderer.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, Objects.requireNonNullElse(decorations, PotDecorations.EMPTY), outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        this.decoratedPotRenderer.getExtents(output);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<PotDecorations>
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit((Object)new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        public DecoratedPotSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            return new DecoratedPotSpecialRenderer(new DecoratedPotRenderer(context));
        }
    }
}


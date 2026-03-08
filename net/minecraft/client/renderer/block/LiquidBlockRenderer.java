/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.platform.Transparency;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LiquidBlockRenderer {
    private static final float MAX_FLUID_HEIGHT = 0.8888889f;
    private final TextureAtlasSprite lavaStill;
    private final TextureAtlasSprite lavaFlowing;
    private final TextureAtlasSprite waterStill;
    private final TextureAtlasSprite waterFlowing;
    private final TextureAtlasSprite waterOverlay;
    private final Map<Fluid, ChunkSectionLayer> layerByFluid;

    public LiquidBlockRenderer(SpriteGetter sprites) {
        this.lavaStill = sprites.get(ModelBakery.LAVA_STILL);
        this.lavaFlowing = sprites.get(ModelBakery.LAVA_FLOW);
        this.waterStill = sprites.get(ModelBakery.WATER_STILL);
        this.waterFlowing = sprites.get(ModelBakery.WATER_FLOW);
        this.waterOverlay = sprites.get(ModelBakery.WATER_OVERLAY);
        Transparency waterTransparency = this.waterStill.transparency().or(this.waterFlowing.transparency());
        Transparency lavaTransparency = this.lavaStill.transparency().or(this.lavaFlowing.transparency());
        this.layerByFluid = Map.of(Fluids.WATER, ChunkSectionLayer.byTransparency(waterTransparency), Fluids.FLOWING_WATER, ChunkSectionLayer.byTransparency(waterTransparency), Fluids.LAVA, ChunkSectionLayer.byTransparency(lavaTransparency), Fluids.FLOWING_LAVA, ChunkSectionLayer.byTransparency(lavaTransparency));
    }

    private static boolean isNeighborSameFluid(FluidState fluidState, FluidState neighborFluidState) {
        return neighborFluidState.getType().isSame(fluidState.getType());
    }

    private static boolean isFaceOccludedByState(Direction direction, float height, BlockState state) {
        VoxelShape occluder = state.getFaceOcclusionShape(direction.getOpposite());
        if (occluder == Shapes.empty()) {
            return false;
        }
        if (occluder == Shapes.block()) {
            boolean fullBlock = height == 1.0f;
            return direction != Direction.UP || fullBlock;
        }
        VoxelShape shape = Shapes.box(0.0, 0.0, 0.0, 1.0, height, 1.0);
        return Shapes.blockOccludes(shape, occluder, direction);
    }

    private static boolean isFaceOccludedByNeighbor(Direction direction, float height, BlockState neighborState) {
        return LiquidBlockRenderer.isFaceOccludedByState(direction, height, neighborState);
    }

    private static boolean isFaceOccludedBySelf(BlockState state, Direction direction) {
        return LiquidBlockRenderer.isFaceOccludedByState(direction.getOpposite(), 1.0f, state);
    }

    public static boolean shouldRenderFace(FluidState fluidState, BlockState blockState, Direction direction, FluidState neighborFluidState) {
        return !LiquidBlockRenderer.isNeighborSameFluid(fluidState, neighborFluidState) && !LiquidBlockRenderer.isFaceOccludedBySelf(blockState, direction);
    }

    public ChunkSectionLayer getRenderLayer(FluidState state) {
        return this.layerByFluid.getOrDefault(state.getType(), ChunkSectionLayer.SOLID);
    }

    public void tesselate(BlockAndTintGetter level, BlockPos pos, VertexConsumer builder, BlockState blockState, FluidState fluidState) {
        float bottomOffs;
        float heightSouthWest;
        float heightSouthEast;
        float heightNorthWest;
        float heightNorthEast;
        BlockState blockStateDown = level.getBlockState(pos.relative(Direction.DOWN));
        FluidState fluidStateDown = blockStateDown.getFluidState();
        BlockState blockStateUp = level.getBlockState(pos.relative(Direction.UP));
        FluidState fluidStateUp = blockStateUp.getFluidState();
        BlockState blockStateNorth = level.getBlockState(pos.relative(Direction.NORTH));
        FluidState fluidStateNorth = blockStateNorth.getFluidState();
        BlockState blockStateSouth = level.getBlockState(pos.relative(Direction.SOUTH));
        FluidState fluidStateSouth = blockStateSouth.getFluidState();
        BlockState blockStateWest = level.getBlockState(pos.relative(Direction.WEST));
        FluidState fluidStateWest = blockStateWest.getFluidState();
        BlockState blockStateEast = level.getBlockState(pos.relative(Direction.EAST));
        FluidState fluidStateEast = blockStateEast.getFluidState();
        boolean renderUp = !LiquidBlockRenderer.isNeighborSameFluid(fluidState, fluidStateUp);
        boolean renderDown = LiquidBlockRenderer.shouldRenderFace(fluidState, blockState, Direction.DOWN, fluidStateDown) && !LiquidBlockRenderer.isFaceOccludedByNeighbor(Direction.DOWN, 0.8888889f, blockStateDown);
        boolean renderNorth = LiquidBlockRenderer.shouldRenderFace(fluidState, blockState, Direction.NORTH, fluidStateNorth);
        boolean renderSouth = LiquidBlockRenderer.shouldRenderFace(fluidState, blockState, Direction.SOUTH, fluidStateSouth);
        boolean renderWest = LiquidBlockRenderer.shouldRenderFace(fluidState, blockState, Direction.WEST, fluidStateWest);
        boolean renderEast = LiquidBlockRenderer.shouldRenderFace(fluidState, blockState, Direction.EAST, fluidStateEast);
        if (!(renderUp || renderDown || renderEast || renderWest || renderNorth || renderSouth)) {
            return;
        }
        boolean isLava = fluidState.is(FluidTags.LAVA);
        TextureAtlasSprite stillSprite = isLava ? this.lavaStill : this.waterStill;
        TextureAtlasSprite flowingSprite = isLava ? this.lavaFlowing : this.waterFlowing;
        int color = isLava ? -1 : BiomeColors.getAverageWaterColor(level, pos);
        CardinalLighting cardinalLighting = level.cardinalLighting();
        Fluid type = fluidState.getType();
        float heightSelf = this.getHeight(level, type, pos, blockState, fluidState);
        if (heightSelf >= 1.0f) {
            heightNorthEast = 1.0f;
            heightNorthWest = 1.0f;
            heightSouthEast = 1.0f;
            heightSouthWest = 1.0f;
        } else {
            float heightNorth = this.getHeight(level, type, pos.north(), blockStateNorth, fluidStateNorth);
            float heightSouth = this.getHeight(level, type, pos.south(), blockStateSouth, fluidStateSouth);
            float heightEast = this.getHeight(level, type, pos.east(), blockStateEast, fluidStateEast);
            float heightWest = this.getHeight(level, type, pos.west(), blockStateWest, fluidStateWest);
            heightNorthEast = this.calculateAverageHeight(level, type, heightSelf, heightNorth, heightEast, pos.relative(Direction.NORTH).relative(Direction.EAST));
            heightNorthWest = this.calculateAverageHeight(level, type, heightSelf, heightNorth, heightWest, pos.relative(Direction.NORTH).relative(Direction.WEST));
            heightSouthEast = this.calculateAverageHeight(level, type, heightSelf, heightSouth, heightEast, pos.relative(Direction.SOUTH).relative(Direction.EAST));
            heightSouthWest = this.calculateAverageHeight(level, type, heightSelf, heightSouth, heightWest, pos.relative(Direction.SOUTH).relative(Direction.WEST));
        }
        float x = pos.getX() & 0xF;
        float y = pos.getY() & 0xF;
        float z = pos.getZ() & 0xF;
        float offs = 0.001f;
        float f = bottomOffs = renderDown ? 0.001f : 0.0f;
        if (renderUp && !LiquidBlockRenderer.isFaceOccludedByNeighbor(Direction.UP, Math.min(Math.min(heightNorthWest, heightSouthWest), Math.min(heightSouthEast, heightNorthEast)), blockStateUp)) {
            float v11;
            float u11;
            float v10;
            float u10;
            float v01;
            float u01;
            float v00;
            float u00;
            heightNorthWest -= 0.001f;
            heightSouthWest -= 0.001f;
            heightSouthEast -= 0.001f;
            heightNorthEast -= 0.001f;
            Vec3 flow = fluidState.getFlow(level, pos);
            if (flow.x == 0.0 && flow.z == 0.0) {
                u00 = stillSprite.getU(0.0f);
                v00 = stillSprite.getV(0.0f);
                u01 = u00;
                v01 = stillSprite.getV(1.0f);
                u10 = stillSprite.getU(1.0f);
                v10 = v01;
                u11 = u10;
                v11 = v00;
            } else {
                float angle = (float)Mth.atan2(flow.z, flow.x) - 1.5707964f;
                float s = Mth.sin(angle) * 0.25f;
                float c = Mth.cos(angle) * 0.25f;
                float cc = 0.5f;
                u00 = flowingSprite.getU(0.5f + (-c - s));
                v00 = flowingSprite.getV(0.5f + (-c + s));
                u01 = flowingSprite.getU(0.5f + (-c + s));
                v01 = flowingSprite.getV(0.5f + (c + s));
                u10 = flowingSprite.getU(0.5f + (c + s));
                v10 = flowingSprite.getV(0.5f + (c - s));
                u11 = flowingSprite.getU(0.5f + (c - s));
                v11 = flowingSprite.getV(0.5f + (-c - s));
            }
            int topLightCoords = this.getLightCoords(level, pos);
            int topColor = ARGB.scaleRGB(color, cardinalLighting.up());
            this.vertex(builder, x + 0.0f, y + heightNorthWest, z + 0.0f, topColor, u00, v00, topLightCoords);
            this.vertex(builder, x + 0.0f, y + heightSouthWest, z + 1.0f, topColor, u01, v01, topLightCoords);
            this.vertex(builder, x + 1.0f, y + heightSouthEast, z + 1.0f, topColor, u10, v10, topLightCoords);
            this.vertex(builder, x + 1.0f, y + heightNorthEast, z + 0.0f, topColor, u11, v11, topLightCoords);
            if (fluidState.shouldRenderBackwardUpFace(level, pos.above())) {
                this.vertex(builder, x + 0.0f, y + heightNorthWest, z + 0.0f, topColor, u00, v00, topLightCoords);
                this.vertex(builder, x + 1.0f, y + heightNorthEast, z + 0.0f, topColor, u11, v11, topLightCoords);
                this.vertex(builder, x + 1.0f, y + heightSouthEast, z + 1.0f, topColor, u10, v10, topLightCoords);
                this.vertex(builder, x + 0.0f, y + heightSouthWest, z + 1.0f, topColor, u01, v01, topLightCoords);
            }
        }
        if (renderDown) {
            float u0 = stillSprite.getU0();
            float u1 = stillSprite.getU1();
            float v0 = stillSprite.getV0();
            float v1 = stillSprite.getV1();
            int belowLightCoords = this.getLightCoords(level, pos.below());
            int belowColor = ARGB.scaleRGB(color, cardinalLighting.down());
            this.vertex(builder, x, y + bottomOffs, z + 1.0f, belowColor, u0, v1, belowLightCoords);
            this.vertex(builder, x, y + bottomOffs, z, belowColor, u0, v0, belowLightCoords);
            this.vertex(builder, x + 1.0f, y + bottomOffs, z, belowColor, u1, v0, belowLightCoords);
            this.vertex(builder, x + 1.0f, y + bottomOffs, z + 1.0f, belowColor, u1, v1, belowLightCoords);
        }
        int sideLightCoords = this.getLightCoords(level, pos);
        for (Direction faceDir : Direction.Plane.HORIZONTAL) {
            Block relativeBlock;
            float z1;
            float z0;
            float x1;
            float x0;
            float hh1;
            float hh0;
            if (!(switch (faceDir) {
                case Direction.NORTH -> {
                    hh0 = heightNorthWest;
                    hh1 = heightNorthEast;
                    x0 = x;
                    x1 = x + 1.0f;
                    z0 = z + 0.001f;
                    z1 = z + 0.001f;
                    yield renderNorth;
                }
                case Direction.SOUTH -> {
                    hh0 = heightSouthEast;
                    hh1 = heightSouthWest;
                    x0 = x + 1.0f;
                    x1 = x;
                    z0 = z + 1.0f - 0.001f;
                    z1 = z + 1.0f - 0.001f;
                    yield renderSouth;
                }
                case Direction.WEST -> {
                    hh0 = heightSouthWest;
                    hh1 = heightNorthWest;
                    x0 = x + 0.001f;
                    x1 = x + 0.001f;
                    z0 = z + 1.0f;
                    z1 = z;
                    yield renderWest;
                }
                default -> {
                    hh0 = heightNorthEast;
                    hh1 = heightSouthEast;
                    x0 = x + 1.0f - 0.001f;
                    x1 = x + 1.0f - 0.001f;
                    z0 = z;
                    z1 = z + 1.0f;
                    yield renderEast;
                }
            }) || LiquidBlockRenderer.isFaceOccludedByNeighbor(faceDir, Math.max(hh0, hh1), level.getBlockState(pos.relative(faceDir)))) continue;
            BlockPos tPos = pos.relative(faceDir);
            TextureAtlasSprite sprite = flowingSprite;
            if (!isLava && ((relativeBlock = level.getBlockState(tPos).getBlock()) instanceof HalfTransparentBlock || relativeBlock instanceof LeavesBlock)) {
                sprite = this.waterOverlay;
            }
            float u0 = sprite.getU(0.0f);
            float u1 = sprite.getU(0.5f);
            float v01 = sprite.getV((1.0f - hh0) * 0.5f);
            float v02 = sprite.getV((1.0f - hh1) * 0.5f);
            float v1 = sprite.getV(0.5f);
            float shadeSide = faceDir.getAxis() == Direction.Axis.Z ? cardinalLighting.north() : cardinalLighting.west();
            int faceColor = ARGB.scaleRGB(color, cardinalLighting.up() * shadeSide);
            this.vertex(builder, x0, y + hh0, z0, faceColor, u0, v01, sideLightCoords);
            this.vertex(builder, x1, y + hh1, z1, faceColor, u1, v02, sideLightCoords);
            this.vertex(builder, x1, y + bottomOffs, z1, faceColor, u1, v1, sideLightCoords);
            this.vertex(builder, x0, y + bottomOffs, z0, faceColor, u0, v1, sideLightCoords);
            if (sprite == this.waterOverlay) continue;
            this.vertex(builder, x0, y + bottomOffs, z0, faceColor, u0, v1, sideLightCoords);
            this.vertex(builder, x1, y + bottomOffs, z1, faceColor, u1, v1, sideLightCoords);
            this.vertex(builder, x1, y + hh1, z1, faceColor, u1, v02, sideLightCoords);
            this.vertex(builder, x0, y + hh0, z0, faceColor, u0, v01, sideLightCoords);
        }
    }

    private float calculateAverageHeight(BlockAndTintGetter level, Fluid type, float heightSelf, float height2, float height1, BlockPos cornerPos) {
        if (height1 >= 1.0f || height2 >= 1.0f) {
            return 1.0f;
        }
        float[] weightedHeight = new float[2];
        if (height1 > 0.0f || height2 > 0.0f) {
            float heightCorner = this.getHeight(level, type, cornerPos);
            if (heightCorner >= 1.0f) {
                return 1.0f;
            }
            this.addWeightedHeight(weightedHeight, heightCorner);
        }
        this.addWeightedHeight(weightedHeight, heightSelf);
        this.addWeightedHeight(weightedHeight, height1);
        this.addWeightedHeight(weightedHeight, height2);
        return weightedHeight[0] / weightedHeight[1];
    }

    private void addWeightedHeight(float[] weightedHeight, float height) {
        if (height >= 0.8f) {
            weightedHeight[0] = weightedHeight[0] + height * 10.0f;
            weightedHeight[1] = weightedHeight[1] + 10.0f;
        } else if (height >= 0.0f) {
            weightedHeight[0] = weightedHeight[0] + height;
            weightedHeight[1] = weightedHeight[1] + 1.0f;
        }
    }

    private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return this.getHeight(level, fluidType, pos, state, state.getFluidState());
    }

    private float getHeight(BlockAndTintGetter level, Fluid fluidType, BlockPos pos, BlockState state, FluidState fluidState) {
        if (fluidType.isSame(fluidState.getType())) {
            BlockState aboveState = level.getBlockState(pos.above());
            if (fluidType.isSame(aboveState.getFluidState().getType())) {
                return 1.0f;
            }
            return fluidState.getOwnHeight();
        }
        if (!state.isSolid()) {
            return 0.0f;
        }
        return -1.0f;
    }

    private void vertex(VertexConsumer builder, float x, float y, float z, int color, float u, float v, int lightCoords) {
        builder.addVertex(x, y, z, color, u, v, OverlayTexture.NO_OVERLAY, lightCoords, 0.0f, 1.0f, 0.0f);
    }

    private int getLightCoords(BlockAndTintGetter level, BlockPos pos) {
        return LightCoordsUtil.max(LevelRenderer.getLightCoords(level, pos), LevelRenderer.getLightCoords(level, pos.above()));
    }
}


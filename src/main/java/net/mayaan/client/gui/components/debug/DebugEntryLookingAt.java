/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.mayaan.ChatFormatting;
import net.mayaan.SharedConstants;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.core.TypedInstance;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Util;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateHolder;
import net.mayaan.world.level.block.state.properties.Property;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.material.Fluid;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import org.jspecify.annotations.Nullable;

public abstract class DebugEntryLookingAt
implements DebugScreenEntry {
    private static final int RANGE = 20;
    private static final Identifier BLOCK_GROUP = Identifier.withDefaultNamespace("looking_at_block");
    private static final Identifier FLUID_GROUP = Identifier.withDefaultNamespace("looking_at_fluid");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Level clientOrServerLevel;
        Entity cameraEntity = Mayaan.getInstance().getCameraEntity();
        Level level = clientOrServerLevel = SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES ? serverOrClientLevel : Mayaan.getInstance().level;
        if (cameraEntity == null || clientOrServerLevel == null) {
            return;
        }
        HitResult block = this.getHitResult(cameraEntity);
        ArrayList<String> result = new ArrayList<String>();
        if (block.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult)block).getBlockPos();
            this.extractInfo(result, clientOrServerLevel, pos);
        }
        displayer.addToGroup(this.group(), result);
    }

    public abstract HitResult getHitResult(Entity var1);

    public abstract void extractInfo(List<String> var1, Level var2, BlockPos var3);

    public abstract Identifier group();

    public static void addTagEntries(List<String> result, TypedInstance<?> instance) {
        instance.tags().map(e -> "#" + String.valueOf(e.location())).forEach(result::add);
    }

    public static class FluidTagInfo
    extends DebugEntryLookingAtTags<FluidState> {
        @Override
        public HitResult getHitResult(Entity cameraEntity) {
            return cameraEntity.pick(20.0, 0.0f, true);
        }

        @Override
        public FluidState getInstance(Level level, BlockPos pos) {
            return level.getFluidState(pos);
        }

        @Override
        public Identifier group() {
            return FLUID_GROUP;
        }
    }

    public static class FluidStateInfo
    extends DebugEntryLookingAtState<Fluid, FluidState> {
        protected FluidStateInfo() {
            super("Targeted Fluid");
        }

        @Override
        public HitResult getHitResult(Entity cameraEntity) {
            return cameraEntity.pick(20.0, 0.0f, true);
        }

        @Override
        public FluidState getInstance(Level level, BlockPos pos) {
            return level.getFluidState(pos);
        }

        @Override
        public Identifier group() {
            return FLUID_GROUP;
        }
    }

    public static class BlockTagInfo
    extends DebugEntryLookingAtTags<BlockState> {
        @Override
        public HitResult getHitResult(Entity cameraEntity) {
            return cameraEntity.pick(20.0, 0.0f, false);
        }

        @Override
        public BlockState getInstance(Level level, BlockPos pos) {
            return level.getBlockState(pos);
        }

        @Override
        public Identifier group() {
            return BLOCK_GROUP;
        }
    }

    public static class BlockStateInfo
    extends DebugEntryLookingAtState<Block, BlockState> {
        protected BlockStateInfo() {
            super("Targeted Block");
        }

        @Override
        public HitResult getHitResult(Entity cameraEntity) {
            return cameraEntity.pick(20.0, 0.0f, false);
        }

        @Override
        public BlockState getInstance(Level level, BlockPos pos) {
            return level.getBlockState(pos);
        }

        @Override
        public Identifier group() {
            return BLOCK_GROUP;
        }
    }

    public static abstract class DebugEntryLookingAtTags<T extends TypedInstance<?>>
    extends DebugEntryLookingAt {
        protected abstract T getInstance(Level var1, BlockPos var2);

        @Override
        public void extractInfo(List<String> result, Level level, BlockPos pos) {
            T instance = this.getInstance(level, pos);
            DebugEntryLookingAtTags.addTagEntries(result, instance);
        }
    }

    public static abstract class DebugEntryLookingAtState<OwnerType, StateType extends StateHolder<OwnerType, StateType>>
    extends DebugEntryLookingAt {
        private final String prefix;

        protected DebugEntryLookingAtState(String prefix) {
            this.prefix = prefix;
        }

        protected abstract StateType getInstance(Level var1, BlockPos var2);

        @Override
        public void extractInfo(List<String> result, Level level, BlockPos pos) {
            StateType stateInstance = this.getInstance(level, pos);
            result.add(String.valueOf(ChatFormatting.UNDERLINE) + this.prefix + ": " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
            result.add(((TypedInstance)stateInstance).typeHolder().getRegisteredName());
            DebugEntryLookingAtState.addStateProperties(result, stateInstance);
        }

        private static void addStateProperties(List<String> result, StateHolder<?, ?> stateHolder) {
            for (Map.Entry<Property<?>, Comparable<?>> entry : stateHolder.getValues().entrySet()) {
                result.add(DebugEntryLookingAtState.getPropertyValueString(entry));
            }
        }

        private static String getPropertyValueString(Map.Entry<Property<?>, Comparable<?>> entry) {
            Property<?> property = entry.getKey();
            Comparable<?> value = entry.getValue();
            Object valueString = Util.getPropertyName(property, value);
            if (Boolean.TRUE.equals(value)) {
                valueString = String.valueOf(ChatFormatting.GREEN) + (String)valueString;
            } else if (Boolean.FALSE.equals(value)) {
                valueString = String.valueOf(ChatFormatting.RED) + (String)valueString;
            }
            return property.getName() + ": " + (String)valueString;
        }
    }
}


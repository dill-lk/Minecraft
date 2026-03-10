/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.network.chat.Component;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.SimpleMenuProvider;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.ContainerLevelAccess;
import net.mayaan.world.inventory.SmithingMenu;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.CraftingTableBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;

public class SmithingTableBlock
extends CraftingTableBlock {
    public static final MapCodec<SmithingTableBlock> CODEC = SmithingTableBlock.simpleCodec(SmithingTableBlock::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.upgrade");

    public MapCodec<SmithingTableBlock> codec() {
        return CODEC;
    }

    protected SmithingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new SmithingMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
        }
        return InteractionResult.SUCCESS;
    }
}


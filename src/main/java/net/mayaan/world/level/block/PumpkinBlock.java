/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.item.ItemEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.CarvedPumpkinBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.phys.BlockHitResult;

public class PumpkinBlock
extends Block {
    public static final MapCodec<PumpkinBlock> CODEC = PumpkinBlock.simpleCodec(PumpkinBlock::new);

    public MapCodec<PumpkinBlock> codec() {
        return CODEC;
    }

    protected PumpkinBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!itemStack.is(Items.SHEARS)) {
            return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
        }
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        Direction clickedDirection = hitResult.getDirection();
        Direction direction = clickedDirection.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : clickedDirection;
        PumpkinBlock.dropFromBlockInteractLootTable(serverLevel, BuiltInLootTables.CARVE_PUMPKIN, state, level.getBlockEntity(pos), itemStack, player, (ignored, pumpkinSeeds) -> {
            ItemEntity entity = new ItemEntity(level, (double)pos.getX() + 0.5 + (double)direction.getStepX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5 + (double)direction.getStepZ() * 0.65, (ItemStack)pumpkinSeeds);
            RandomSource random = level.getRandom();
            entity.setDeltaMovement(0.05 * (double)direction.getStepX() + random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + random.nextDouble() * 0.02);
            level.addFreshEntity(entity);
        });
        level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.setBlock(pos, (BlockState)Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
        itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
        level.gameEvent((Entity)player, GameEvent.SHEAR, pos);
        player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
        return InteractionResult.SUCCESS;
    }
}


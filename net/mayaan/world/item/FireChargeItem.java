/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Position;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ProjectileItem;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseFireBlock;
import net.mayaan.world.level.block.CampfireBlock;
import net.mayaan.world.level.block.CandleBlock;
import net.mayaan.world.level.block.CandleCakeBlock;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.Vec3;

public class FireChargeItem
extends Item
implements ProjectileItem {
    public FireChargeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        boolean used = false;
        if (CampfireBlock.canLight(blockState) || CandleBlock.canLight(blockState) || CandleCakeBlock.canLight(blockState)) {
            this.playSound(level, pos);
            level.setBlockAndUpdate(pos, (BlockState)blockState.setValue(BlockStateProperties.LIT, true));
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
            used = true;
        } else if (BaseFireBlock.canBePlacedAt(level, pos = pos.relative(context.getClickedFace()), context.getHorizontalDirection())) {
            this.playSound(level, pos);
            level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_PLACE, pos);
            used = true;
        }
        if (used) {
            context.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private void playSound(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        RandomSource random = level.getRandom();
        double dirX = random.triangle((double)direction.getStepX(), 0.11485000000000001);
        double dirY = random.triangle((double)direction.getStepY(), 0.11485000000000001);
        double dirZ = random.triangle((double)direction.getStepZ(), 0.11485000000000001);
        Vec3 dir = new Vec3(dirX, dirY, dirZ);
        SmallFireball fireball = new SmallFireball(level, position.x(), position.y(), position.z(), dir.normalize());
        fireball.setItem(itemStack);
        return fireball;
    }

    @Override
    public void shoot(Projectile projectile, double xd, double yd, double zd, float pow, float uncertainty) {
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder().positionFunction((source, direction) -> DispenserBlock.getDispensePosition(source, 1.0, Vec3.ZERO)).uncertainty(6.6666665f).power(1.0f).overrideDispenseEvent(1018).build();
    }
}


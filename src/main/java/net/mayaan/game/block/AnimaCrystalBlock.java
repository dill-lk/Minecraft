package net.mayaan.game.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.BlockHitResult;

/**
 * Anima Crystal Block — deposits of crystallized Anima found at ley-line crossings.
 *
 * These glowing cyan-gold crystals are the primary source of Anima energy in Xibalkaal.
 * They emit ambient particle effects and power nearby Ley-line Conduits.
 * Found naturally in the Crystal Veins biome and at ley-line intersection points.
 */
public class AnimaCrystalBlock extends Block {
    public static final MapCodec<AnimaCrystalBlock> CODEC = AnimaCrystalBlock.simpleCodec(AnimaCrystalBlock::new);

    public AnimaCrystalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /** Amount of Anima restored when a player right-clicks an Anima Crystal Block. */
    private static final int CRYSTAL_ANIMA_RESTORE = 5;

    @Override
    public MapCodec<? extends AnimaCrystalBlock> codec() {
        return CODEC;
    }

    /**
     * Right-clicking an Anima Crystal Block restores {@link #CRYSTAL_ANIMA_RESTORE} Anima to
     * the player. Represents directly touching the crystallized Anima Prime — a brief,
     * direct connection to the ley-line substrate. The crystal does not deplete (fed by the
     * ley-line, not a finite reserve). Returns {@link InteractionResult#SUCCESS} regardless,
     * so the player sees the animation even when already full.
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && player instanceof net.mayaan.server.level.ServerPlayer serverPlayer) {
            boolean restored = net.mayaan.game.magic.AnimaManager.INSTANCE.restore(
                    serverPlayer.getUUID(), CRYSTAL_ANIMA_RESTORE);
            if (restored) {
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        net.mayaan.game.MayaanSounds.GLYPH_CAST_BASIC,
                        net.mayaan.sounds.SoundSource.BLOCKS,
                        0.4f, 1.6f);
                net.mayaan.game.MayaanPacketSender.sendAnimaSync(serverPlayer);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Emit Anima glow particles — faint golden sparkles rising from the crystal
        if (random.nextInt(5) == 0) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
            double y = pos.getY() + 1.0 + random.nextDouble() * 0.1;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0.0, 0.01, 0.0);
        }
    }
}

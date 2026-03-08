package net.mayaan.game.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

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

    @Override
    public MapCodec<? extends AnimaCrystalBlock> codec() {
        return CODEC;
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

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
 * Ley-line Conduit Block — channels Anima energy between ley-line nodes.
 *
 * The Mayaan built these throughout Xibalkaal to direct Anima flow from ley-line
 * crossings to their cities and machines. Many still function beneath jungle overgrowth.
 * An active Conduit glows faintly gold and can be used as a fast-travel waypoint.
 *
 * Players can craft Conduits once they learn the CHANNEL glyph, allowing them to build
 * their own Anima infrastructure.
 */
public class LeylineConduitBlock extends Block {
    public static final MapCodec<LeylineConduitBlock> CODEC = LeylineConduitBlock.simpleCodec(LeylineConduitBlock::new);

    public LeylineConduitBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends LeylineConduitBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Golden Anima flow particles streaming along the conduit's surface
        if (random.nextInt(3) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0.0, 0.02, 0.0);
        }
    }
}

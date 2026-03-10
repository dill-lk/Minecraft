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
 * Ley-line Conduit Block — channels Anima energy between ley-line nodes.
 *
 * The Mayaan built these throughout Xibalkaal to direct Anima flow from ley-line
 * crossings to their cities and machines. Many still function beneath jungle overgrowth.
 * An active Conduit glows faintly gold and can be used as a fast-travel waypoint.
 *
 * Players can craft Conduits once they learn the CHANNEL glyph, allowing them to build
 * their own Anima infrastructure.
 *
 * <h2>Right-click interaction</h2>
 * Right-clicking the conduit while in survival / adventure mode opens the
 * {@link net.mayaan.client.gui.screens.GlyphCastScreen} (client side only), allowing
 * the player to select a glyph and cast tier. The selected cast is sent to the server
 * via {@link net.mayaan.network.protocol.game.ServerboundMayaanCastGlyphPacket}.
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
            Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            net.mayaan.client.Mayaan.getInstance()
                    .setScreen(new net.mayaan.client.gui.screens.GlyphCastScreen());
        }
        return InteractionResult.SUCCESS;
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

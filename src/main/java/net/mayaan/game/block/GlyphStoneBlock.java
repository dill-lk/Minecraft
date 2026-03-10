package net.mayaan.game.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;

/**
 * Glyph Stone Block — Mayaan temple stone inscribed with ancient glyphs.
 *
 * When placed, these carved stone blocks emit a faint magical hum detectable by
 * players attuned to Anima. Used as structural components in Mayaan temples and
 * ruins. Crafted by players at a Glyph Table once they learn the STRENGTHEN glyph.
 *
 * Different variants exist for each of the seven Glyph types.
 */
public class GlyphStoneBlock extends Block {
    public static final MapCodec<GlyphStoneBlock> CODEC = GlyphStoneBlock.simpleCodec(GlyphStoneBlock::new);

    public GlyphStoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends GlyphStoneBlock> codec() {
        return CODEC;
    }
}

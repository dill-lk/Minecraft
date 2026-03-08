package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Glyph Fragment — a piece of inscribed Mayaan material containing a partial glyph.
 *
 * Found scattered through ruins, embedded in Constructs' memories, and hidden within
 * Timeline Echoes. Players collect fragments and combine them at a Glyph Table to
 * learn complete Glyphs and unlock spells, recipes, and passages.
 *
 * Each fragment is tied to one of the seven {@link GlyphType Glyph types}.
 * A complete set of fragments for a glyph grants mastery of that glyph's effects.
 */
public class GlyphFragment extends Item {
    private final GlyphType glyphType;

    public GlyphFragment(GlyphType glyphType, Item.Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
        this.glyphType = glyphType;
    }

    /** Returns which glyph type this fragment belongs to. */
    public GlyphType getGlyphType() {
        return glyphType;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.glyph_fragment.type",
                Component.translatable("glyph.mayaan." + glyphType.getId())));
        builder.accept(Component.translatable("item.mayaan.glyph_fragment.script",
                glyphType.getScriptName()));
    }
}

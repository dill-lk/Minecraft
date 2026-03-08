package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Stone Shard — the starting item of every Mayaan playthrough.
 *
 * Found in the player's hand when they wash ashore on the Isle of First Light.
 * Inscribed with the Mayaan glyph "Yaal" (SEEK), it pulses faintly when the
 * player faces a direction of interest. This is the first hint of the glyph
 * system and the player's first taste of Anima sensitivity.
 *
 * "Long before the first sun rose over the mortal lands, there was Mayaan."
 */
public class StoneShard extends Item {
    public StoneShard(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.stone_shard.glyph",
                GlyphType.SEEK.getScriptName()));
        builder.accept(Component.translatable("item.mayaan.stone_shard.lore"));
    }
}

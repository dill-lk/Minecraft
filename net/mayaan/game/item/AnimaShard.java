package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Anima Shard — crystallized life-force extracted from ley-line nodes.
 *
 * The Anima Shard is the primary currency of Mayaan magic. Players collect these
 * by mining Anima Crystal Blocks or defeating powerful creatures. Shards are consumed
 * when activating Glyph inscriptions, crafting magical items, and powering Constructs.
 *
 * Using too many Anima Shards in a small area triggers an Anima Drought warning.
 */
public class AnimaShard extends Item {
    public AnimaShard(Item.Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.anima_shard.desc"));
    }
}

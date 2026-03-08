package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Core Shard — the crystallized Anima heart that animates a Construct.
 *
 * Core Shards are splinters of condensed Anima Prime, each containing a faint
 * crystallization of personality. The Forgeborn faction guard a vault with enough
 * Core Shards to reanimate every dormant Construct on the continent.
 *
 * Players can use a Core Shard to:
 * - Repair a damaged Construct
 * - Bond with a neutral Construct, making it a companion
 * - Power a Resonance Pillar as a renewable Anima battery
 *
 * Rare drop from ancient Construct enemies; also found in deep Mayaan vaults.
 */
public class CoreShard extends Item {
    public CoreShard(Item.Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.core_shard.desc"));
        builder.accept(Component.translatable("item.mayaan.core_shard.lore"));
    }
}

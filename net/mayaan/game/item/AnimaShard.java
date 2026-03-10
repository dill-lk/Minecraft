package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.AnimaSystem;
import net.mayaan.game.magic.PlayerAnimaData;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;
import net.mayaan.world.level.Level;

/**
 * Anima Shard — crystallized life-force extracted from ley-line nodes.
 *
 * The Anima Shard is the primary currency of Mayaan magic. Players collect these
 * by mining Anima Crystal Blocks or defeating powerful creatures. Shards are consumed
 * when activating Glyph inscriptions, crafting magical items, and powering Constructs.
 *
 * Using too many Anima Shards in a small area triggers an Anima Drought warning.
 *
 * <h2>Right-click use</h2>
 * Right-clicking an Anima Shard restores {@value #ANIMA_RESTORE_PER_SHARD} Anima to the
 * player (clamped to max), consuming one item from the stack, and sends a system message.
 */
public class AnimaShard extends Item {

    /** Amount of Anima restored by consuming a single Anima Shard. */
    public static final float ANIMA_RESTORE_PER_SHARD = 25f;

    public AnimaShard(Item.Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        PlayerAnimaData data = AnimaManager.INSTANCE.getAnimaData(serverPlayer.getUUID());
        if (data.isFull()) {
            serverPlayer.sendSystemMessage(Component.translatable(
                    "item.mayaan.anima_shard.full"));
            return InteractionResult.FAIL;
        }

        data.regen(ANIMA_RESTORE_PER_SHARD);

        // Consume one shard
        ItemStack stack = serverPlayer.getItemInHand(hand);
        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        serverPlayer.sendSystemMessage(Component.translatable("item.mayaan.anima_shard.restored",
                ANIMA_RESTORE_PER_SHARD));

        // Sync anima state to client immediately
        net.mayaan.game.MayaanPacketSender.sendAnimaSync(serverPlayer);

        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.anima_shard.desc"));
    }
}

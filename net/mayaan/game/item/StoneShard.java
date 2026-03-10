package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;
import net.mayaan.world.level.Level;

/**
 * Stone Shard — the starting item of every Mayaan playthrough.
 *
 * Found in the player's hand when they wash ashore on the Isle of First Light.
 * Inscribed with the Mayaan glyph "Yaal" (SEEK), it pulses faintly when the
 * player faces a direction of interest. This is the first hint of the glyph
 * system and the player's first taste of Anima sensitivity.
 *
 * <h2>Right-click use</h2>
 * Activating the Stone Shard triggers the SEEK glyph pulse:
 * <ul>
 *   <li>Server: plays the glyph-seek sound at the player's position</li>
 *   <li>Server: attempts to complete the {@code seek_glyph_first_use} story goal</li>
 *   <li>Client: opens the {@link net.mayaan.client.gui.screens.GlyphPulseOverlay}</li>
 * </ul>
 *
 * "Long before the first sun rose over the mortal lands, there was Mayaan."
 */
public class StoneShard extends Item {
    public StoneShard(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            // Client-side: open the SEEK glyph pulse overlay
            net.mayaan.client.Mayaan.getInstance()
                    .setScreen(new net.mayaan.client.gui.screens.GlyphPulseOverlay());
        } else if (player instanceof ServerPlayer serverPlayer) {
            // Server-side: play sound and advance story if needed
            level.playSound(null,
                    serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                    net.mayaan.game.MayaanSounds.GLYPH_CAST_BASIC,
                    net.mayaan.sounds.SoundSource.PLAYERS,
                    0.6f, 1.4f);
            net.mayaan.game.MayaanServerEvents.tryCompleteGoalByKey(
                    serverPlayer, serverPlayer.getUUID(), "seek_glyph_first_use");
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable("item.mayaan.stone_shard.glyph",
                GlyphType.SEEK.getScriptName()));
        builder.accept(Component.translatable("item.mayaan.stone_shard.lore"));
    }
}

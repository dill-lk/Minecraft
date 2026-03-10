package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
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
 * Glyph Fragment — a piece of inscribed Mayaan material containing a partial glyph.
 *
 * Found scattered through ruins, embedded in Constructs' memories, and hidden within
 * Timeline Echoes. Players collect fragments and combine them at a Glyph Table to
 * learn complete Glyphs and unlock spells, recipes, and passages.
 *
 * Each fragment is tied to one of the seven {@link GlyphType Glyph types}.
 * A complete set of fragments for a glyph grants mastery of that glyph's effects.
 *
 * <h2>Right-click use</h2>
 * Right-clicking a Glyph Fragment awards the player one fragment of the appropriate type
 * via {@link GlyphKnowledgeManager}, consuming one item from the stack. A chat message
 * confirms the award and shows the new mastery level. On the server side the updated
 * glyph state is immediately synced to the client.
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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        GlyphMastery newMastery = GlyphKnowledgeManager.INSTANCE.awardFragment(
                serverPlayer.getUUID(), glyphType);

        // Consume one item from the stack
        ItemStack stack = serverPlayer.getItemInHand(hand);
        if (!serverPlayer.getAbilities().instabuild) {
            stack.shrink(1);
        }

        // Notify the player
        serverPlayer.sendSystemMessage(Component.translatable(
                "item.mayaan.glyph_fragment.absorbed",
                Component.translatable("glyph.mayaan." + glyphType.getId()),
                Component.translatable("glyph.mastery." + newMastery.name().toLowerCase())));

        // Sync updated glyph state to the client immediately
        net.mayaan.game.MayaanPacketSender.sendGlyphSync(serverPlayer);

        return InteractionResult.CONSUME;
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

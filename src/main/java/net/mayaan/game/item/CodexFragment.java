package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.network.chat.Component;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;
import net.mayaan.world.level.Level;

/**
 * Codex Fragment — a preserved record of Mayaan knowledge, found in ruins,
 * Construct memories, and the receiving chambers of Mayaan temples.
 *
 * <p>Codex Fragments are the primary lore-delivery items in Mayaan. They are
 * physical pieces of Mayaan writing media — clay tablets, carved crystal slabs,
 * and impression-pressed stone sheets — kept intact by residual Anima. Reading
 * one adds its content to the player's in-game journal.
 *
 * <h2>Types of Records</h2>
 * Codex Fragments vary enormously in content:
 * <ul>
 *   <li><em>Scout Records</em> — field notes left by Mayaan explorers, including
 *       Ix-Channa's records that led the player here in the first place.</li>
 *   <li><em>Council Minutes</em> — official transcripts of Glyph Council sessions,
 *       including the vote to reject Xalon's Seven Keys compromise.</li>
 *   <li><em>Personal Writing (Heart Script)</em> — informal records in the Mayaan
 *       personal writing system; more emotional, less formal, often more revealing.</li>
 *   <li><em>Technical Schematics</em> — construction diagrams for Constructs,
 *       ley-line networks, and Anima machinery. Unlock crafting recipes.</li>
 *   <li><em>Astronomical Records</em> — Star Caller observations of ley-line
 *       fluctuations, dimensional events, and the growing signal from Yaan.</li>
 * </ul>
 *
 * <h2>Glyph Knowledge Integration</h2>
 * Fragments written in Heart Script or Old Mayaan require a minimum Glyph Knowledge
 * score to read in full. Fragments with insufficient knowledge unlock partially,
 * with some lines fragmented or missing, and can be re-read after gaining more knowledge.
 */
public final class CodexFragment extends Item {

    /**
     * The category of knowledge contained in a Codex Fragment.
     */
    public enum Category {

        /** Field notes from Mayaan Scouts, including Ix-Channa's records. */
        SCOUT_RECORD("scout_record"),

        /** Official Glyph Council session transcripts and resolutions. */
        COUNCIL_MINUTES("council_minutes"),

        /**
         * Personal writing in Heart Script — informal, emotional, most revealing.
         * Requires higher Glyph Knowledge to read fully.
         */
        HEART_SCRIPT("heart_script"),

        /** Technical schematics for Constructs, ley-line networks, and machinery. Unlock crafting recipes. */
        TECHNICAL_SCHEMATIC("technical_schematic"),

        /** Star Caller astronomical observations and ley-line fluctuation records. */
        ASTRONOMICAL_RECORD("astronomical_record"),

        /** Unknown category — used when reading older or damaged fragments. */
        UNKNOWN("unknown");

        private final String id;

        Category(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    /** Unique identifier for this specific fragment (used for journal deduplication). */
    private final String fragmentId;

    /** The category of content this fragment contains. */
    private final Category category;

    /**
     * The minimum Glyph Knowledge score required to read this fragment in full.
     * 0 = any player can read it; 7 = only a player who has mastered all glyphs.
     */
    private final int requiredKnowledgeScore;

    public CodexFragment(String fragmentId, Category category, int requiredKnowledgeScore,
            Item.Properties properties) {
        super(properties.rarity(rarityFor(category)));
        this.fragmentId = fragmentId;
        this.category = category;
        this.requiredKnowledgeScore = Math.max(0, Math.min(7, requiredKnowledgeScore));
    }

    /** Returns the unique identifier for this fragment. */
    public String getFragmentId() {
        return fragmentId;
    }

    /** Returns the category of knowledge contained in this fragment. */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the minimum Glyph Knowledge score required to read this fragment in full.
     * Players below this threshold see a partially-fragmented version.
     */
    public int getRequiredKnowledgeScore() {
        return requiredKnowledgeScore;
    }

    // ── Right-click: open the reading screen ─────────────────────────────────

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        int score = net.mayaan.client.ClientMayaanData.INSTANCE.getKnowledgeScore();
        boolean canRead = score >= requiredKnowledgeScore;
        net.mayaan.client.Mayaan.getInstance()
                .setScreen(new net.mayaan.client.gui.screens.CodexReadScreen(
                        fragmentId, category, canRead));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable(
                "item.mayaan.codex_fragment.category",
                Component.translatable("codex_category.mayaan." + category.getId())));
        if (requiredKnowledgeScore > 0) {
            builder.accept(Component.translatable(
                    "item.mayaan.codex_fragment.knowledge_required", requiredKnowledgeScore));
        }
    }

    private static net.mayaan.world.item.Rarity rarityFor(Category category) {
        return switch (category) {
            case COUNCIL_MINUTES -> net.mayaan.world.item.Rarity.RARE;
            case HEART_SCRIPT -> net.mayaan.world.item.Rarity.RARE;
            case TECHNICAL_SCHEMATIC -> net.mayaan.world.item.Rarity.UNCOMMON;
            default -> net.mayaan.world.item.Rarity.UNCOMMON;
        };
    }
}

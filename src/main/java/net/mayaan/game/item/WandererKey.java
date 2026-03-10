package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Wanderer's Key — one of seven physical keys that together can seal The Maw
 * without sacrifice using the Seven Keys method devised by Glyph-Keeper Xalon.
 *
 * <p>The Seven Keys method was Xalon's alternative to the Great Sacrifice: a theoretical
 * mechanism that could seal The Maw in a controlled, non-destructive way. It required
 * seven Warden's Keys working in concert — but the Council couldn't gather all seven in
 * time, so the Sacrifice was chosen instead.
 *
 * <p>Xalon built the path anyway. She scattered the keys across the world, inscribed
 * the Stone Shard as the central connection piece, and left a receiving chamber at
 * the Isle of First Light for whoever would come.
 *
 * <h2>The Seven Keys</h2>
 * <ol>
 *   <li>The <em>Stone Shard</em> — the central connection piece. The player's starting item.
 *       Already in the player's possession from the Prologue.</li>
 *   <li>The <em>Council's Seal Ring</em> — carried by a Warden buried under the Iron Pact
 *       fortress at Xaan Hold. Uncovered during the Maw Breach battle.</li>
 *   <li>The <em>Resonance Fork</em> — hidden inside the Forgeborn's vault, which the dormant
 *       Wardens were protecting. Found when the vault finally opens.</li>
 *   <li>The <em>Star Chart Prism</em> — held by the Star Callers as a sacred astronomical
 *       instrument. Donated by Ek at the Gate Rebuilt ceremony.</li>
 *   <li>The <em>Root-Bound Crystal</em> — grown into the oldest tree at the Rootweaver's
 *       village; given freely by Elder Cenote when the player reaches HONOURED standing.</li>
 *   <li>The <em>Tide Keeper's Prism</em> — the second piece the Sea Temple's Tide Keeper
 *       was truly protecting (alongside the Ixchelic Shard). Surrendered alongside Shard Two.</li>
 *   <li>The <em>Forgotten King's Sigil</em> — the key the Forgotten King carried as the
 *       Warden Commander; surrendered alongside Ixchelic Shard Three.</li>
 * </ol>
 *
 * <p>Note: The player collects all seven keys during normal story progression without
 * realizing it until the {@code understand_the_stone_shard} goal in Act III. Only at that
 * point does the game reveal that all seven have been in the player's possession.
 *
 * @see net.mayaan.game.item.StoneShard
 * @see net.mayaan.game.story.StoryChapter#TRUTH_IN_AMBER
 */
public final class WandererKey extends Item {

    /**
     * The seven Warden's Keys needed for the Seven Keys sealing method.
     */
    public enum KeyIndex {

        /**
         * Key 1 — The Stone Shard. The central connection piece of the Seven Keys.
         * Inscribed with SEEK by Xalon and placed in the Isle of First Light receiving chamber.
         * This is the {@link StoneShard} item; the enum entry exists for completeness.
         */
        STONE_SHARD(1, "stone_shard", "Isle of First Light — Receiving Chamber"),

        /**
         * Key 2 — The Council's Seal Ring. Carried by a buried Warden under Xaan Hold.
         * Recovered during the Maw Breach battle in Act I.
         */
        COUNCIL_SEAL_RING(2, "council_seal_ring", "Xaan Hold — Under the Iron Pact Fortress"),

        /**
         * Key 3 — The Resonance Fork. Hidden in the Forgeborn's vault that the dormant
         * Wardens were protecting. Found when Tzon's vault finally opens.
         */
        RESONANCE_FORK(3, "resonance_fork", "Tz'ikin — The Forgeborn's Sealed Vault"),

        /**
         * Key 4 — The Star Chart Prism. Held by the Star Callers as a sacred astronomical
         * instrument. Donated by Ek at the Gate Rebuilt ceremony.
         */
        STAR_CHART_PRISM(4, "star_chart_prism", "Serpent Highlands — Star Callers' Observatory"),

        /**
         * Key 5 — The Root-Bound Crystal. Grown into the oldest tree at the Rootweaver
         * village. Given freely by Elder Cenote when the player reaches HONOURED standing.
         */
        ROOT_BOUND_CRYSTAL(5, "root_bound_crystal", "Eternal Canopy — Rootweaver Heart Tree"),

        /**
         * Key 6 — The Tide Keeper's Prism. The Sea Temple's Tide Keeper was protecting
         * this alongside Ixchelic Shard Two; both are surrendered together.
         */
        TIDE_KEEPERS_PRISM(6, "tide_keepers_prism", "Abyssal Coast — Sea Temple Archive"),

        /**
         * Key 7 — The Forgotten King's Sigil. The Warden Commander's personal seal,
         * carried through ten thousand iterations of the loop.
         * Surrendered alongside Ixchelic Shard Three.
         */
        FORGOTTEN_KINGS_SIGIL(7, "forgotten_kings_sigil", "Serpent Highlands — The Forgotten City");

        private final int number;
        private final String id;
        private final String origin;

        KeyIndex(int number, String id, String origin) {
            this.number = number;
            this.id = id;
            this.origin = origin;
        }

        /** Returns the ordinal position of this key (1–7). */
        public int getNumber() {
            return number;
        }

        /** Returns the unique string identifier for this key. */
        public String getId() {
            return id;
        }

        /** Returns a human-readable description of where this key originates. */
        public String getOrigin() {
            return origin;
        }

        @Override
        public String toString() {
            return "mayaan:wanderer_key/" + id;
        }
    }

    private final KeyIndex keyIndex;

    public WandererKey(KeyIndex keyIndex, Item.Properties properties) {
        super(properties.rarity(Rarity.RARE));
        this.keyIndex = keyIndex;
    }

    /** Returns which of the seven keys this is. */
    public KeyIndex getKeyIndex() {
        return keyIndex;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable(
                "item.mayaan.wanderer_key.number", keyIndex.getNumber(), 7));
        builder.accept(Component.translatable(
                "item.mayaan.wanderer_key.origin",
                Component.literal(keyIndex.getOrigin())));
        builder.accept(Component.translatable("item.mayaan.wanderer_key.desc"));
    }
}

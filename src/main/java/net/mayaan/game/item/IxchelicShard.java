package net.mayaan.game.item;

import java.util.function.Consumer;
import net.mayaan.network.chat.Component;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Rarity;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;

/**
 * Ixchelic Shard — one of three fragments of the Ixchelic Stone, the activation
 * crystal of the Astral Gate.
 *
 * <p>The Ixchelic Stone was the Mayaan Astral Gate's dimensional key: a single large
 * crystal tuned to the frequency of Yaan. When the Gate was destroyed in The Unraveling,
 * the Stone shattered into three pieces. Each piece was placed in the most secure vault
 * the Glyph Council could arrange in the minutes before the Great Sacrifice.
 *
 * <h2>The Three Shards</h2>
 * <ul>
 *   <li>{@link Index#ONE} — Placed in the deepest vault of the Crystal Veins by the Council itself.
 *       Guarded by the Warden of the Abyss for millennia.</li>
 *   <li>{@link Index#TWO} — Entrusted to the Sea Temple's Tide Keeper as part of the cultural archive.
 *       Submerged at the edge of the Abyssal Coast.</li>
 *   <li>{@link Index#THREE} — Embedded in the chest of the Forgotten King — the Warden Commander
 *       who refused to leave and was trapped in a temporal loop in the Serpent Highlands.</li>
 * </ul>
 *
 * <p>All three must be inserted into the Gate's crystal housing to reconstruct the Ixchelic Stone
 * and activate the Astral Gate (chapter {@code gate_rebuilt}).
 *
 * @see net.mayaan.game.story.StoryChapter#GATE_REBUILT
 */
public final class IxchelicShard extends Item {

    /**
     * The three numbered shards of the shattered Ixchelic Stone.
     */
    public enum Index {

        /**
         * First Shard — guarded for millennia by the Warden of the Abyss in the Crystal Veins.
         * The vault's depth and the Anima-pressure zone surrounding it made retrieval
         * nearly impossible without the Stone Shard as a Warden's Key.
         */
        ONE(1, "warden_of_the_abyss", "Crystal Veins — Vault of the First Shard"),

        /**
         * Second Shard — kept in the Sea Temple's cultural archive by the Tide Keeper.
         * Accessible only during specific lunar phases when the water above recedes.
         */
        TWO(2, "tide_keeper", "Abyssal Coast — Sea Temple Archive"),

        /**
         * Third Shard — embedded in the Forgotten King's chest.
         * Carried within the loop-trapped Warden Commander for ten thousand iterations.
         * Released when the loop was finally broken.
         */
        THREE(3, "forgotten_king", "Serpent Highlands — The Forgotten City");

        private final int number;
        /** The keeper or guardian associated with this shard. */
        private final String guardianKey;
        /** A brief human-readable description of the shard's resting place. */
        private final String location;

        Index(int number, String guardianKey, String location) {
            this.number = number;
            this.guardianKey = guardianKey;
            this.location = location;
        }

        /** Returns the ordinal position of this shard (1, 2, or 3). */
        public int getNumber() {
            return number;
        }

        /** Returns the guardian/keeper key associated with this shard. */
        public String getGuardianKey() {
            return guardianKey;
        }

        /** Returns a human-readable description of where this shard was kept. */
        public String getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "ixchelic_shard_" + number;
        }
    }

    private final Index shardIndex;

    public IxchelicShard(Index shardIndex, Item.Properties properties) {
        super(properties.rarity(Rarity.EPIC));
        this.shardIndex = shardIndex;
    }

    /** Returns which of the three shards this is. */
    public Index getShardIndex() {
        return shardIndex;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag flag) {
        builder.accept(Component.translatable(
                "item.mayaan.ixchelic_shard.number", shardIndex.getNumber()));
        builder.accept(Component.translatable(
                "item.mayaan.ixchelic_shard.location",
                Component.literal(shardIndex.getLocation())));
        builder.accept(Component.translatable("item.mayaan.ixchelic_shard.desc"));
    }
}

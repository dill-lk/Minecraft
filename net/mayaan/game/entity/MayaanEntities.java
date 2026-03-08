package net.mayaan.game.entity;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.game.MayaanIdentifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.MobCategory;

/**
 * Registry of all Mayaan-specific entity types.
 *
 * <p>Entity types are registered with the {@code mayaan:} namespace and linked to
 * their corresponding entity classes. The attribute sets for each entity must be
 * registered separately in the world's attribute supplier registration, calling
 * the {@code createAttributes()} method of each entity class.
 *
 * <h2>Creatures</h2>
 * <ul>
 *   <li>{@link #CANOPY_STALKER} — six-limbed ambush predator of the Eternal Canopy</li>
 *   <li>{@link #STONE_SERPENT} — massive rock-plated serpent from the Crystal Veins</li>
 *   <li>{@link #BLOOM_WALKER} — mobile fungal colony that disperses hallucinogenic spores</li>
 *   <li>{@link #VOID_MOTH} — phase-passing dimensional visitor from the Void Shelf</li>
 *   <li>{@link #HOLLOW_KNIGHT} — ancient Construct corrupted by Maw-resonance</li>
 * </ul>
 *
 * <h2>Constructs</h2>
 * <ul>
 *   <li>{@link #MAYAAN_CONSTRUCT} — a standard Mayaan Construct (soldier, worker, guardian)</li>
 *   <li>{@link #IX_COMPANION} — Ix, the ancient guide Construct; carries fractured memories</li>
 * </ul>
 */
public final class MayaanEntities {

    // ── World Creatures ───────────────────────────────────────────────────────

    /**
     * Canopy Stalker — six-limbed ambush predator; paralytic venom; drop: venom gland, stalker scale.
     * Spawns in {@link net.mayaan.game.biome.MayaanBiomes#ETERNAL_CANOPY}.
     */
    public static final EntityType<CanopyStalker> CANOPY_STALKER = MayaanEntities.register(
            "canopy_stalker",
            EntityType.Builder.<CanopyStalker>of(CanopyStalker::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.7f)
                    .clientTrackingRange(8));

    /**
     * Stone Serpent — massive rock-plated serpent; knockback-immune; drop: serpent scale plate.
     * Spawns in {@link net.mayaan.game.biome.MayaanBiomes#CRYSTAL_VEINS}.
     */
    public static final EntityType<StoneSerpent> STONE_SERPENT = MayaanEntities.register(
            "stone_serpent",
            EntityType.Builder.<StoneSerpent>of(StoneSerpent::new, MobCategory.MONSTER)
                    .sized(2.0f, 0.9f)
                    .clientTrackingRange(10));

    /**
     * Bloom Walker — mobile fungal colony; neutral unless provoked; spore cloud on hurt/death.
     * Spawns in {@link net.mayaan.game.biome.MayaanBiomes#ETERNAL_CANOPY} and
     * {@link net.mayaan.game.biome.MayaanBiomes#MIRRORWOOD}.
     */
    public static final EntityType<BloomWalker> BLOOM_WALKER = MayaanEntities.register(
            "bloom_walker",
            EntityType.Builder.<BloomWalker>of(BloomWalker::new, MobCategory.CREATURE)
                    .sized(1.0f, 1.2f)
                    .clientTrackingRange(8));

    /**
     * Void Moth — phase-passing dimensional visitor; completely neutral; drop: void wing fragment.
     * Spawns in {@link net.mayaan.game.biome.MayaanBiomes#MIRRORWOOD} and
     * {@link net.mayaan.game.biome.MayaanBiomes#THE_VOID_SHELF}.
     */
    public static final EntityType<VoidMoth> VOID_MOTH = MayaanEntities.register(
            "void_moth",
            EntityType.Builder.<VoidMoth>of(VoidMoth::new, MobCategory.AMBIENT)
                    .sized(0.5f, 0.3f)
                    .clientTrackingRange(6)
                    .fireImmune());

    /**
     * Hollow Knight — corrupted Construct; Anima Drain attack; drop: corrupted core shard.
     * Spawns in Mayaan ruins and temple interiors in any biome.
     */
    public static final EntityType<HollowKnight> HOLLOW_KNIGHT = MayaanEntities.register(
            "hollow_knight",
            EntityType.Builder.<HollowKnight>of(HollowKnight::new, MobCategory.MONSTER)
                    .sized(0.7f, 2.1f)
                    .clientTrackingRange(10));

    // ── Constructs ────────────────────────────────────────────────────────────

    /**
     * Standard Mayaan Construct — neutral wanderer; bondable with Core Shard.
     * Spawns near dormant Mayaan machinery throughout Xibalkaal.
     */
    public static final EntityType<MayaanConstruct> MAYAAN_CONSTRUCT = MayaanEntities.register(
            "mayaan_construct",
            EntityType.Builder.<MayaanConstruct>of(MayaanConstruct::new, MobCategory.MISC)
                    .sized(0.7f, 2.0f)
                    .clientTrackingRange(8));

    /**
     * Ix — the ancient guide Construct. Carries fractured memories; bonds with the player
     * at the start of the game; upgradeable via Core Shard repair.
     * Only one Ix companion exists per world — spawned by
     * {@link net.mayaan.game.story.StorySpawnHandler} at new-game initialization.
     */
    public static final EntityType<IxCompanion> IX_COMPANION = MayaanEntities.register(
            "ix",
            EntityType.Builder.<IxCompanion>of(IxCompanion::new, MobCategory.MISC)
                    .sized(0.7f, 2.0f)
                    .clientTrackingRange(8));

    private MayaanEntities() {}

    private static <T extends net.mayaan.world.entity.Entity> EntityType<T> register(
            String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE, MayaanIdentifier.of(name));
        EntityType<T> type = builder.build(key);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type);
    }
}

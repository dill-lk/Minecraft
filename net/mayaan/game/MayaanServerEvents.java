package net.mayaan.game;

import java.util.List;
import java.util.UUID;
import net.mayaan.game.block.LeylineConduitBlock;
import net.mayaan.game.entity.BloomWalker;
import net.mayaan.game.entity.CanopyStalker;
import net.mayaan.game.entity.HollowKnight;
import net.mayaan.game.entity.VoidMoth;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.faction.FactionManager;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.AnimaSystem;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.magic.PlayerAnimaData;
import net.mayaan.game.story.StoryChapter;
import net.mayaan.game.story.StoryManager;
import net.mayaan.game.story.StorySpawnHandler;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;

/**
 * Server-side event hooks that wire the Mayaan game subsystems into the engine event loop.
 *
 * <p>Call the {@code static} methods of this class from the server-layer event listeners:
 * <ul>
 *   <li>{@link #onPlayerJoin(ServerPlayer)} — from {@code PlayerList.placeNewPlayer} / join callback</li>
 *   <li>{@link #onPlayerLeave(ServerPlayer)} — from {@code PlayerList.remove} / quit callback</li>
 *   <li>{@link #onPlayerTick(ServerPlayer)} — from {@code ServerPlayer.tick()} or the level tick loop</li>
 *   <li>{@link #onDayChange(List)} — from the day-cycle callback (midnight → dawn)</li>
 *   <li>{@link #onEntityDeath(Entity, DamageSource)} — from {@code LivingEntity.die}</li>
 * </ul>
 *
 * <p>This class deliberately contains <em>no state</em>; all per-player state lives in the
 * singleton managers ({@link AnimaManager}, {@link FactionManager}, {@link StoryManager},
 * and {@link PlayerDataStore}).
 *
 * <h2>Tick granularity</h2>
 * Not every subsystem needs to run every tick. {@link #onPlayerTick} batches the work:
 * <ul>
 *   <li>Anima regen — every tick (server rate of 20/s is intentional for smooth display)</li>
 *   <li>Leyline detection — every 5 ticks (100ms granularity is sufficient)</li>
 *   <li>Anima Drought sound — debounced; only fires once per drought crossing</li>
 *   <li>Anima Surge mob effect — checked every 20 ticks (1 second)</li>
 * </ul>
 */
public final class MayaanServerEvents {

    /**
     * Ticks until the next leyline-detection check for a given player.
     * This is a simple modulo against {@link ServerPlayer#tickCount} to avoid per-player state.
     */
    private static final int LEYLINE_CHECK_INTERVAL = 5;

    /**
     * Ticks until the next Anima Surge check (standing on leyline long enough to earn the buff).
     */
    private static final int SURGE_CHECK_INTERVAL = 20;

    /**
     * Number of consecutive leyline-contact ticks required before granting Anima Surge.
     * At {@link #LEYLINE_CHECK_INTERVAL} ticks per check: 5 * 20 = 100 ticks = 5 seconds.
     */
    static final int SURGE_REQUIRED_CHECKS = 20;

    /**
     * Duration (ticks) for the Anima Surge effect: 200 ticks = 10 seconds after leaving.
     * The tick handler continuously refreshes this duration while the player is on a conduit.
     */
    static final int ANIMA_SURGE_DURATION = 200;

    /**
     * Duration (ticks) of the Glyph Weakness effect after a recoil event: 1200 ticks = 60s.
     */
    public static final int GLYPH_WEAKNESS_DURATION = 1200;

    private MayaanServerEvents() {}

    // ── Join / Leave ──────────────────────────────────────────────────────────

    /**
     * Called when a player joins the server.
     *
     * <p>Responsibilities:
     * <ol>
     *   <li>Loads persisted per-player data for all 4 subsystems via {@link PlayerDataStore}</li>
     *   <li>If this is a brand-new game for the player, teleports them to the Isle of First
     *       Light and gives them the Stone Shard via {@link StorySpawnHandler}</li>
     * </ol>
     *
     * @param player the joining player
     */
    public static void onPlayerJoin(ServerPlayer player) {
        UUID playerId = player.getUUID();
        PlayerDataStore.INSTANCE.onPlayerJoin(playerId);

        if (StoryManager.INSTANCE.isNewGame(playerId)) {
            StorySpawnHandler.initializeNewPlayer(player);
        }
    }

    /**
     * Called when a player disconnects from the server.
     *
     * <p>Persists all per-player subsystem data via {@link PlayerDataStore}.
     *
     * @param player the departing player
     */
    public static void onPlayerLeave(ServerPlayer player) {
        PlayerDataStore.INSTANCE.onPlayerLeave(player.getUUID());
    }

    // ── Per-tick ──────────────────────────────────────────────────────────────

    /**
     * Called every server tick for each online player.
     *
     * <p>This method handles:
     * <ul>
     *   <li>Anima regeneration (every tick, leyline-aware)</li>
     *   <li>Leyline contact detection (every {@link #LEYLINE_CHECK_INTERVAL} ticks)</li>
     *   <li>Anima Drought onset sound (debounced to once per drought entry)</li>
     *   <li>Anima Surge mob-effect grant/refresh (every {@link #SURGE_CHECK_INTERVAL} ticks)</li>
     * </ul>
     *
     * @param player the player being ticked
     */
    public static void onPlayerTick(ServerPlayer player) {
        UUID playerId = player.getUUID();
        int tick = player.tickCount;

        // ── Leyline detection ─────────────────────────────────────────────────
        boolean onLeyline = false;
        if (tick % LEYLINE_CHECK_INTERVAL == 0) {
            onLeyline = isStandingOnLeylineConduit(player);
        }

        // ── Anima regen ───────────────────────────────────────────────────────
        AnimaManager.INSTANCE.onTick(playerId, onLeyline);

        // ── Anima Drought sound feedback ──────────────────────────────────────
        // Check once per second; play the drought-onset sound only when the
        // drought flag first becomes true (transition, not sustained).
        if (tick % 20 == 0) {
            PlayerAnimaData animaData = AnimaManager.INSTANCE.getAnimaData(playerId);
            if (animaData.isInDrought() && !animaData.isDroughtSoundPlayed()) {
                animaData.markDroughtSoundPlayed();
                player.level().playLocalSound(
                        player.getX(), player.getY(), player.getZ(),
                        MayaanSounds.ANIMA_DROUGHT_ONSET,
                        net.mayaan.sounds.SoundSource.PLAYERS,
                        0.8f, 1.0f, false);
            } else if (!animaData.isInDrought()) {
                animaData.clearDroughtSoundPlayed();
            }
        }

        // ── Anima Surge buff ──────────────────────────────────────────────────
        if (tick % SURGE_CHECK_INTERVAL == 0) {
            handleAnimaSurge(player, playerId, isStandingOnLeylineConduit(player));
        }
    }

    // ── Day change ────────────────────────────────────────────────────────────

    /**
     * Called once per in-game day, at midnight → dawn transition.
     *
     * <p>Resets the Anima drought counter for every online player and plays the
     * drought-relief sound for any player who was in drought.
     *
     * @param players all currently online players
     */
    public static void onDayChange(List<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            UUID playerId = player.getUUID();
            boolean wasInDrought = AnimaManager.INSTANCE.getAnimaData(playerId).isInDrought();
            AnimaManager.INSTANCE.onDayChange(playerId);

            if (wasInDrought) {
                player.level().playLocalSound(
                        player.getX(), player.getY(), player.getZ(),
                        MayaanSounds.ANIMA_DROUGHT_RELIEF,
                        net.mayaan.sounds.SoundSource.PLAYERS,
                        0.6f, 1.0f, false);
            }
        }
    }

    // ── Entity death ──────────────────────────────────────────────────────────

    /**
     * Called when any living entity dies.
     *
     * <p>Currently handles:
     * <ul>
     *   <li>Faction standing adjustments when a player kills a creature:
     *       <ul>
     *         <li>Killing a {@link CanopyStalker} grants +5 Rootweaver standing
     *             (they prey on Rootweaver canopy paths)</li>
     *         <li>Killing a {@link BloomWalker} is neutral to all factions unless
     *             the player has Rootweaver standing &lt; ACCEPTED, in which case it
     *             loses −3 (BloomWalkers are protected in Rootweaver territory)</li>
     *         <li>Killing a {@link HollowKnight} grants +10 Iron Pact standing
     *             (they are the Pact's primary threat objective)</li>
     *         <li>Killing a {@link VoidMoth} is neutral (they are not yet understood)</li>
     *       </ul>
     *   </li>
     *   <li>Story goal advancement: if the killed entity is a named boss type, attempts
     *       to complete the matching story goal via {@link StoryManager}</li>
     * </ul>
     *
     * @param entity      the entity that just died
     * @param damageSource the damage source that killed it
     */
    public static void onEntityDeath(Entity entity, DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();
        if (!(attacker instanceof ServerPlayer player)) {
            return;
        }
        UUID playerId = player.getUUID();

        if (entity instanceof CanopyStalker) {
            FactionManager.INSTANCE.adjustStanding(playerId, Faction.ROOTWEAVERS, +5);
        } else if (entity instanceof BloomWalker) {
            // BloomWalker kill is harmful to Rootweavers if below ACCEPTED standing
            net.mayaan.game.faction.FactionStanding standing =
                    FactionManager.INSTANCE.getStanding(playerId, Faction.ROOTWEAVERS);
            if (standing.ordinal() < net.mayaan.game.faction.FactionStanding.ACCEPTED.ordinal()) {
                FactionManager.INSTANCE.adjustStanding(playerId, Faction.ROOTWEAVERS, -3);
            }
        } else if (entity instanceof HollowKnight) {
            FactionManager.INSTANCE.adjustStanding(playerId, Faction.IRON_PACT, +10);
            // Also attempt to complete the "defeat_hollow_knight" story goal if active
            tryCompleteGoalByKey(player, playerId, "defeat_hollow_knight");
        }
    }

    // ── Glyph recoil ─────────────────────────────────────────────────────────

    /**
     * Called when a Prime Glyph cast overloads and the player suffers glyph recoil.
     *
     * <p>Applies the {@link MayaanMobEffects#GLYPH_WEAKNESS} effect and plays the
     * glyph-recoil sound at the player's position.
     *
     * @param player the player who suffered the recoil
     */
    public static void onGlyphRecoil(ServerPlayer player) {
        MobEffectInstance weakness = new MobEffectInstance(
                MayaanMobEffects.GLYPH_WEAKNESS, GLYPH_WEAKNESS_DURATION, 0, false, true);
        player.addEffect(weakness);
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(),
                MayaanSounds.GLYPH_RECOIL,
                net.mayaan.sounds.SoundSource.PLAYERS,
                1.0f, 1.0f, false);
    }

    /**
     * Called after a successful glyph cast of any tier.
     *
     * <p>Plays the appropriate cast sound and spawns the matching particle trail at the
     * player's eye position.
     *
     * @param player  the player who cast
     * @param result  the cast result from {@link net.mayaan.game.magic.GlyphCasting#tryCast}
     */
    public static void onGlyphCastSuccess(ServerPlayer player,
            net.mayaan.game.magic.GlyphCasting.CastResult result) {
        net.mayaan.sounds.SoundEvent castSound = switch (result.tier()) {
            case BASIC -> MayaanSounds.GLYPH_CAST_BASIC;
            case MAJOR -> MayaanSounds.GLYPH_CAST_MAJOR;
            case PRIME -> MayaanSounds.GLYPH_CAST_PRIME;
        };
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(),
                castSound, net.mayaan.sounds.SoundSource.PLAYERS,
                1.0f, 1.0f, false);

        // Fire the advancement criterion
        net.mayaan.game.advancements.MayaanCriteriaTriggers.GLYPH_CAST.trigger(player, result);
    }

    // ── Story ─────────────────────────────────────────────────────────────────

    /**
     * Called when a Timeline Echo sequence is completed in full.
     *
     * <p>Fires the {@link net.mayaan.game.advancements.MayaanCriteriaTriggers#ECHO_EXPERIENCED}
     * advancement criterion, plays the echo-end sound at the player's position, and attempts
     * to complete the story goal whose ID matches {@code triggerGoalId}.
     *
     * @param player        the player who experienced the echo
     * @param triggerGoalId the echo's trigger goal ID (from {@link net.mayaan.game.echo.TimelineEcho})
     */
    public static void onEchoCompleted(ServerPlayer player, String triggerGoalId) {
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(),
                MayaanSounds.ECHO_END, net.mayaan.sounds.SoundSource.PLAYERS,
                0.7f, 1.0f, false);
        net.mayaan.game.advancements.MayaanCriteriaTriggers.ECHO_EXPERIENCED.trigger(
                player, triggerGoalId);
        tryCompleteGoalByKey(player, player.getUUID(), triggerGoalId);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the player is standing on a {@link LeylineConduitBlock}.
     */
    static boolean isStandingOnLeylineConduit(ServerPlayer player) {
        net.mayaan.core.BlockPos below = player.blockPosition().below();
        BlockState state = player.level().getBlockState(below);
        Block block = state.getBlock();
        return block instanceof LeylineConduitBlock;
    }

    /**
     * Manages the Anima Surge mob effect: grants/refreshes when on a conduit,
     * and does not immediately remove on leaving (handled by natural effect expiry).
     */
    private static void handleAnimaSurge(ServerPlayer player, UUID playerId, boolean onLeyline) {
        if (onLeyline) {
            // Increment per-player leyline contact counter; stored in AnimaData
            PlayerAnimaData data = AnimaManager.INSTANCE.getAnimaData(playerId);
            data.incrementLeylineContactTicks();
            if (data.getLeylineContactTicks() >= SURGE_REQUIRED_CHECKS) {
                MobEffectInstance surge = new MobEffectInstance(
                        MayaanMobEffects.ANIMA_SURGE, ANIMA_SURGE_DURATION, 0, false, true);
                player.addEffect(surge);
            }
        } else {
            AnimaManager.INSTANCE.getAnimaData(playerId).resetLeylineContactTicks();
        }
    }

    /**
     * Attempts to complete the story goal with the given key for the player.
     * Silently does nothing if the goal does not exist or is not active.
     * Fires the {@link net.mayaan.game.advancements.MayaanCriteriaTriggers#STORY_GOAL}
     * advancement criterion on completion.
     */
    private static void tryCompleteGoalByKey(ServerPlayer player, UUID playerId, String goalKey) {
        StoryChapter chapter = StoryManager.INSTANCE.getCurrentChapter(playerId);
        for (net.mayaan.game.story.StoryGoal goal : chapter.getGoals()) {
            if (goal.getId().equals(goalKey)) {
                StoryManager.INSTANCE.completeGoal(playerId, goal);
                net.mayaan.game.advancements.MayaanCriteriaTriggers.STORY_GOAL.trigger(
                        player, goal);
                break;
            }
        }
    }
}

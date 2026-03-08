package net.mayaan.game.entity;

import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.FlyingMob;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

/**
 * Void Moth — a dimensional visitor that passes through solid matter.
 *
 * <p>Void Moths are not native to Xibalkaal. They are native to the Void Shelf — the
 * silent, geometric space between dimensions — and drift through the mortal world as
 * passively as fog drifts through trees. Their wing-scales carry fragments of the Void
 * Shelf dimension: dust-sized pockets of null-space that brush reality thin wherever they land.
 *
 * <h2>Phase-Passing</h2>
 * Void Moths can pass through solid blocks. They do not collide with terrain (their
 * {@code noPhysics} flag is set at spawn). This makes them extremely difficult to corner
 * or trap using conventional means. BIND Glyph magic is the only reliable way to halt one.
 *
 * <h2>Combat Behavior</h2>
 * <ul>
 *   <li>Completely neutral — never attacks; never flees</li>
 *   <li>If struck, releases a burst of {@code void_wing_dust} that applies dimensional
 *       disorientation (brief screen distortion; no gameplay penalty)</li>
 *   <li>Cannot be aggroed — it ignores all targeting logic</li>
 * </ul>
 *
 * <h2>Loot</h2>
 * Drops 1–2 {@code mayaan:void_wing_fragment} on death. Used in dimensional crafting
 * (TRANSLATE glyph augments, Void Shelf access items) and as a rare dye.
 *
 * @see net.mayaan.game.biome.MayaanBiomes#THE_VOID_SHELF
 * @see net.mayaan.game.biome.MayaanBiomes#MIRRORWOOD
 */
public class VoidMoth extends FlyingMob {

    /** Whether this Void Moth is currently releasing a void dust burst (on hurt). */
    private boolean dustBurstActive = false;

    public VoidMoth(EntityType<? extends VoidMoth> type, Level level) {
        super(type, level);
        // Void Moths do not collide with terrain — they phase through solid blocks
        this.noPhysics = true;
    }

    @Override
    protected void registerGoals() {
        // Void Moths are passive — they drift, look, and that's all
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        // No target selectors — Void Moths ignore threats
    }

    /**
     * Creates the attribute set for a Void Moth.
     * Very fragile, fast-flying, completely neutral.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.45)
                .add(Attributes.FOLLOW_RANGE, 0.0)   // does not target
                .add(Attributes.FLYING_SPEED, 0.45);
    }

    /** Returns whether this Void Moth is in an active void-dust burst state. */
    public boolean isDustBurstActive() {
        return dustBurstActive;
    }

    /** Sets the dust-burst state (triggered on hurt at the server event layer). */
    public void setDustBurstActive(boolean dustBurstActive) {
        this.dustBurstActive = dustBurstActive;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DustBurstActive", dustBurstActive);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.dustBurstActive = input.getBooleanOr("DustBurstActive", false);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.void_moth");
    }
}

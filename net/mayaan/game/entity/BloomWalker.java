package net.mayaan.game.entity;

import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.MeleeAttackGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.ai.goal.target.HurtByTargetGoal;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

/**
 * Bloom Walker — a mobile fungal colony that disperses hallucinogenic spores.
 *
 * <p>Bloom Walkers are not quite animals. The "creature" is a fruiting body — a
 * temporary locomotive form that a fungal colony extends to move toward a richer
 * nutrient source. Each Bloom Walker is part of a vast underground mycelial network.
 * Killing one does not kill the colony; it simply causes the colony to extend a
 * new fruiting body from another location within 48 hours.
 *
 * <h2>Spore Effect</h2>
 * When a Bloom Walker dies or is hit, it releases a cloud of {@code bloom_spores}
 * that lingers for 8 seconds in a 4-block radius. Players inside the cloud experience
 * the <em>Bloom Haze</em> effect: nearby passive mobs briefly display as hostile
 * (a visual illusion only). Holding an ILLUMINATE Glyph Fragment suppresses the effect.
 *
 * <h2>Combat Behavior</h2>
 * <ul>
 *   <li>Neutral unless attacked or a player steps inside a 2-block radius</li>
 *   <li>Attacks by swinging its root-tendril mass (low damage)</li>
 *   <li>Releases a small spore burst on hit</li>
 *   <li>Cannot be aggroed by Glyph magic alone</li>
 * </ul>
 *
 * <h2>Loot</h2>
 * Drops 1–3 {@code mayaan:bloom_spore_cap} and 0–1 {@code mayaan:mycelial_thread}.
 * Used in illusion-type potions and Rootweaver faction crafting.
 *
 * @see net.mayaan.game.biome.MayaanBiomes#ETERNAL_CANOPY
 * @see net.mayaan.game.biome.MayaanBiomes#MIRRORWOOD
 */
public class BloomWalker extends Monster {

    /**
     * Within this many blocks of a player (without being struck), the Bloom Walker
     * will turn to face the player and may begin a slow advance.
     */
    public static final float NOTICE_RANGE = 2.0f;

    /** Whether this Bloom Walker is currently releasing a spore cloud. */
    private boolean sporingActive = false;

    public BloomWalker(EntityType<? extends BloomWalker> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.7, false));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, net.mayaan.world.entity.player.Player.class, 8.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    /**
     * Creates the attribute set for a Bloom Walker.
     * Slow, fragile, spreads spores — more nuisance than threat alone.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0)
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 12.0)
                .add(Attributes.ARMOR, 0.0);
    }

    /** Returns whether this Bloom Walker is actively releasing spores. */
    public boolean isSporingActive() {
        return sporingActive;
    }

    /** Sets the spore-active state (triggered on hurt/death at the event layer). */
    public void setSporingActive(boolean sporingActive) {
        this.sporingActive = sporingActive;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("SporingActive", sporingActive);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.sporingActive = input.getBooleanOr("SporingActive", false);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.bloom_walker");
    }
}

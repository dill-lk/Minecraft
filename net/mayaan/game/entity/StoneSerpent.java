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
import net.mayaan.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

/**
 * Stone Serpent — a massive rock-bodied serpent that migrates through Xibalkaal's cave systems.
 *
 * <p>Stone Serpents are one of the most ancient creatures on Xibalkaal — ancient enough that
 * the Mayaan regarded them as minor geological events rather than animals. Their rock-plated
 * scales can resist most non-magical tools, but their undersides are soft stone-leather that
 * can be pierced with enough force.
 *
 * <h2>Shed Scale Economy</h2>
 * Stone Serpents periodically shed their outer plates during migration. Shed scales are
 * found in cave systems and yield {@code mayaan:serpent_scale_plate} — a high-tier crafting
 * material for heavy Construct armor and explosion-resistant structures.
 *
 * <h2>Combat Behavior</h2>
 * <ul>
 *   <li>Passive unless the player comes within {@link #AGGRO_RANGE} blocks</li>
 *   <li>Once aggroed, uses a body-slam attack that knocks back in a large arc</li>
 *   <li>Extremely resistant to knockback itself due to its mass</li>
 *   <li>Immune to fire damage; takes double damage from BIND glyph magic</li>
 * </ul>
 *
 * <h2>Loot</h2>
 * Drops 2–5 {@code mayaan:serpent_scale_plate} and 0–1 {@code mayaan:stone_serpent_core}
 * (a Core Shard-adjacent material used to craft Serpent Shell armor sets).
 *
 * @see net.mayaan.game.biome.MayaanBiomes#CRYSTAL_VEINS
 */
public class StoneSerpent extends Monster {

    /** Distance in blocks at which this serpent will notice and pursue a player. */
    public static final float AGGRO_RANGE = 10.0f;

    /** Whether this serpent is currently in a pre-shed state (scales flickering). */
    private boolean preShed = false;

    public StoneSerpent(EntityType<? extends StoneSerpent> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.85, false));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Creates the attribute set for a Stone Serpent.
     * Incredibly durable, moderately slow — a geological force of nature.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 140.0)
                .add(Attributes.MOVEMENT_SPEED, 0.22)
                .add(Attributes.ATTACK_DAMAGE, 12.0)
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.ARMOR, 14.0)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0);
    }

    /** Returns whether this serpent is in a pre-shed state (scales about to drop). */
    public boolean isPreShed() {
        return preShed;
    }

    /** Sets the pre-shed state (triggered by server logic on a scheduled interval). */
    public void setPreShed(boolean preShed) {
        this.preShed = preShed;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("PreShed", preShed);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.preShed = input.getBooleanOr("PreShed", false);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.stone_serpent");
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.storage.loot.parameters;

import net.mayaan.util.Unit;
import net.mayaan.util.context.ContextKey;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemInstance;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;

public class LootContextParams {
    public static final ContextKey<Entity> THIS_ENTITY = ContextKey.vanilla("this_entity");
    public static final ContextKey<Entity> INTERACTING_ENTITY = ContextKey.vanilla("interacting_entity");
    public static final ContextKey<Entity> TARGET_ENTITY = ContextKey.vanilla("target_entity");
    public static final ContextKey<Player> LAST_DAMAGE_PLAYER = ContextKey.vanilla("last_damage_player");
    public static final ContextKey<DamageSource> DAMAGE_SOURCE = ContextKey.vanilla("damage_source");
    public static final ContextKey<Entity> ATTACKING_ENTITY = ContextKey.vanilla("attacking_entity");
    public static final ContextKey<Entity> DIRECT_ATTACKING_ENTITY = ContextKey.vanilla("direct_attacking_entity");
    public static final ContextKey<Vec3> ORIGIN = ContextKey.vanilla("origin");
    public static final ContextKey<BlockState> BLOCK_STATE = ContextKey.vanilla("block_state");
    public static final ContextKey<BlockEntity> BLOCK_ENTITY = ContextKey.vanilla("block_entity");
    public static final ContextKey<ItemInstance> TOOL = ContextKey.vanilla("tool");
    public static final ContextKey<Float> EXPLOSION_RADIUS = ContextKey.vanilla("explosion_radius");
    public static final ContextKey<Integer> ENCHANTMENT_LEVEL = ContextKey.vanilla("enchantment_level");
    public static final ContextKey<Boolean> ENCHANTMENT_ACTIVE = ContextKey.vanilla("enchantment_active");
    public static final ContextKey<Unit> ADDITIONAL_COST_COMPONENT_ALLOWED = ContextKey.vanilla("additional_cost_component_allowed");
}


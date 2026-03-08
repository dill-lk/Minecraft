/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.enchantment.effects;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mayaan.core.Holder;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.StringRepresentable;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.attributes.Attribute;
import net.mayaan.world.entity.ai.attributes.AttributeModifier;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.LevelBasedValue;
import net.mayaan.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.mayaan.world.phys.Vec3;

public record EnchantmentAttributeEffect(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) implements EnchantmentLocationBasedEffect
{
    public static final MapCodec<EnchantmentAttributeEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(EnchantmentAttributeEffect::id), (App)Attribute.CODEC.fieldOf("attribute").forGetter(EnchantmentAttributeEffect::attribute), (App)LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnchantmentAttributeEffect::amount), (App)AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(EnchantmentAttributeEffect::operation)).apply((Applicative)i, EnchantmentAttributeEffect::new));

    private Identifier idForSlot(StringRepresentable slot) {
        return this.id.withSuffix("/" + slot.getSerializedName());
    }

    public AttributeModifier getModifier(int level, StringRepresentable slot) {
        return new AttributeModifier(this.idForSlot(slot), this.amount().calculate(level), this.operation());
    }

    @Override
    public void onChangedBlock(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position, boolean becameActive) {
        if (becameActive && entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            living.getAttributes().addTransientAttributeModifiers((Multimap<Holder<Attribute>, AttributeModifier>)this.makeAttributeMap(enchantmentLevel, item.inSlot()));
        }
    }

    @Override
    public void onDeactivated(EnchantedItemInUse item, Entity entity, Vec3 position, int level) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity)entity;
            living.getAttributes().removeAttributeModifiers((Multimap<Holder<Attribute>, AttributeModifier>)this.makeAttributeMap(level, item.inSlot()));
        }
    }

    private HashMultimap<Holder<Attribute>, AttributeModifier> makeAttributeMap(int enchantmentLevel, EquipmentSlot slot) {
        HashMultimap map = HashMultimap.create();
        map.put(this.attribute, (Object)this.getModifier(enchantmentLevel, slot));
        return map;
    }

    public MapCodec<EnchantmentAttributeEffect> codec() {
        return MAP_CODEC;
    }
}


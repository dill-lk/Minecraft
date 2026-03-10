/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.equipment;

import java.util.Map;
import net.mayaan.core.Holder;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.tags.TagKey;
import net.mayaan.world.entity.EquipmentSlotGroup;
import net.mayaan.world.entity.ai.attributes.AttributeModifier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.component.ItemAttributeModifiers;
import net.mayaan.world.item.equipment.ArmorType;
import net.mayaan.world.item.equipment.EquipmentAsset;

public record ArmorMaterial(int durability, Map<ArmorType, Integer> defense, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> assetId) {
    public ItemAttributeModifiers createAttributes(ArmorType type) {
        int defense = this.defense.getOrDefault(type, 0);
        ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();
        EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(type.getSlot());
        Identifier modifierId = Identifier.withDefaultNamespace("armor." + type.getName());
        modifiers.add(Attributes.ARMOR, new AttributeModifier(modifierId, defense, AttributeModifier.Operation.ADD_VALUE), slotGroup);
        modifiers.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(modifierId, this.toughness, AttributeModifier.Operation.ADD_VALUE), slotGroup);
        if (this.knockbackResistance > 0.0f) {
            modifiers.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(modifierId, this.knockbackResistance, AttributeModifier.Operation.ADD_VALUE), slotGroup);
        }
        return modifiers.build();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.item.equipment.trim;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.Util;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipProvider;
import net.mayaan.world.item.equipment.EquipmentAsset;
import net.mayaan.world.item.equipment.trim.MaterialAssetGroup;
import net.mayaan.world.item.equipment.trim.TrimMaterial;
import net.mayaan.world.item.equipment.trim.TrimPattern;

public record ArmorTrim(Holder<TrimMaterial> material, Holder<TrimPattern> pattern) implements TooltipProvider
{
    public static final Codec<ArmorTrim> CODEC = RecordCodecBuilder.create(i -> i.group((App)TrimMaterial.CODEC.fieldOf("material").forGetter(ArmorTrim::material), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(ArmorTrim::pattern)).apply((Applicative)i, ArmorTrim::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorTrim> STREAM_CODEC = StreamCodec.composite(TrimMaterial.STREAM_CODEC, ArmorTrim::material, TrimPattern.STREAM_CODEC, ArmorTrim::pattern, ArmorTrim::new);
    private static final Component UPGRADE_TITLE = Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("smithing_template.upgrade"))).withStyle(ChatFormatting.GRAY);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(UPGRADE_TITLE);
        consumer.accept(CommonComponents.space().append(this.pattern.value().copyWithStyle(this.material)));
        consumer.accept(CommonComponents.space().append(this.material.value().description()));
    }

    public Identifier layerAssetId(String layerAssetPrefix, ResourceKey<EquipmentAsset> equipmentAsset) {
        MaterialAssetGroup.AssetInfo materialAsset = this.material().value().assets().assetId(equipmentAsset);
        return this.pattern().value().assetId().withPath(patternPath -> layerAssetPrefix + "/" + patternPath + "_" + materialAsset.suffix());
    }
}


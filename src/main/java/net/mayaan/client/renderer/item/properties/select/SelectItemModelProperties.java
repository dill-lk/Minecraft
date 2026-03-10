/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.client.renderer.item.properties.select;

import com.mojang.serialization.Codec;
import net.mayaan.client.renderer.item.properties.select.Charge;
import net.mayaan.client.renderer.item.properties.select.ComponentContents;
import net.mayaan.client.renderer.item.properties.select.ContextDimension;
import net.mayaan.client.renderer.item.properties.select.ContextEntityType;
import net.mayaan.client.renderer.item.properties.select.CustomModelDataProperty;
import net.mayaan.client.renderer.item.properties.select.DisplayContext;
import net.mayaan.client.renderer.item.properties.select.ItemBlockState;
import net.mayaan.client.renderer.item.properties.select.LocalTime;
import net.mayaan.client.renderer.item.properties.select.MainHand;
import net.mayaan.client.renderer.item.properties.select.SelectItemModelProperty;
import net.mayaan.client.renderer.item.properties.select.TrimMaterialProperty;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ExtraCodecs;

public class SelectItemModelProperties {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, SelectItemModelProperty.Type<?, ?>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final Codec<SelectItemModelProperty.Type<?, ?>> CODEC = ID_MAPPER.codec(Identifier.CODEC);

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("custom_model_data"), CustomModelDataProperty.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("main_hand"), MainHand.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("charge_type"), Charge.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("trim_material"), TrimMaterialProperty.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("block_state"), ItemBlockState.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("display_context"), DisplayContext.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("local_time"), LocalTime.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("context_entity_type"), ContextEntityType.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("context_dimension"), ContextDimension.TYPE);
        ID_MAPPER.put(Identifier.withDefaultNamespace("component"), ComponentContents.castType());
    }
}


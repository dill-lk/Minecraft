/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.mayaan.client.model.geom;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.mayaan.client.model.geom.LayerDefinitions;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.geom.builders.LayerDefinition;

public class EntityModelSet {
    public static final EntityModelSet EMPTY = new EntityModelSet(Map.of());
    private final Map<ModelLayerLocation, LayerDefinition> roots;

    public EntityModelSet(Map<ModelLayerLocation, LayerDefinition> roots) {
        this.roots = roots;
    }

    public ModelPart bakeLayer(ModelLayerLocation id) {
        LayerDefinition result = this.roots.get(id);
        if (result == null) {
            throw new IllegalArgumentException("No model for layer " + String.valueOf(id));
        }
        return result.bakeRoot();
    }

    public static EntityModelSet vanilla() {
        return new EntityModelSet((Map<ModelLayerLocation, LayerDefinition>)ImmutableMap.copyOf(LayerDefinitions.createRoots()));
    }
}


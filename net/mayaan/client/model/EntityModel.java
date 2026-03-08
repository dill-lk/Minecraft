/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import java.util.function.Function;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;

public abstract class EntityModel<T extends EntityRenderState>
extends Model<T> {
    public static final float MODEL_Y_OFFSET = -1.501f;

    protected EntityModel(ModelPart root) {
        this(root, RenderTypes::entityCutout);
    }

    protected EntityModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }
}


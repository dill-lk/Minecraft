/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model.geom;

import net.mayaan.resources.Identifier;

public record ModelLayerLocation(Identifier model, String layer) {
    @Override
    public String toString() {
        return String.valueOf(this.model) + "#" + this.layer;
    }
}


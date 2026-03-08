/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer;

import net.mayaan.client.resources.model.sprite.SpriteId;
import net.mayaan.resources.Identifier;

public record SpriteMapper(Identifier sheet, String prefix) {
    public SpriteId apply(Identifier path) {
        return new SpriteId(this.sheet, path.withPrefix(this.prefix + "/"));
    }

    public SpriteId defaultNamespaceApply(String path) {
        return this.apply(Identifier.withDefaultNamespace(path));
    }
}


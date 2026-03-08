/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.texture;

import java.io.IOException;
import net.mayaan.client.renderer.texture.ReloadableTexture;
import net.mayaan.client.renderer.texture.TextureContents;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceManager;

public class SimpleTexture
extends ReloadableTexture {
    public SimpleTexture(Identifier location) {
        super(location);
    }

    @Override
    public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
        return TextureContents.load(resourceManager, this.resourceId());
    }
}


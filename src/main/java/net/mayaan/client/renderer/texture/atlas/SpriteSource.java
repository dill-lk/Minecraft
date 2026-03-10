/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.texture.atlas;

import com.mojang.serialization.MapCodec;
import java.util.function.Predicate;
import net.mayaan.client.renderer.texture.SpriteContents;
import net.mayaan.client.renderer.texture.atlas.SpriteResourceLoader;
import net.mayaan.resources.FileToIdConverter;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.Resource;
import net.mayaan.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;

public interface SpriteSource {
    public static final FileToIdConverter TEXTURE_ID_CONVERTER = new FileToIdConverter("textures", ".png");

    public void run(ResourceManager var1, Output var2);

    public MapCodec<? extends SpriteSource> codec();

    public static interface DiscardableLoader
    extends Loader {
        default public void discard() {
        }
    }

    @FunctionalInterface
    public static interface Loader {
        public @Nullable SpriteContents get(SpriteResourceLoader var1);
    }

    public static interface Output {
        default public void add(Identifier id, Resource resource) {
            this.add(id, loader -> loader.loadSprite(id, resource));
        }

        public void add(Identifier var1, DiscardableLoader var2);

        public void removeAll(Predicate<Identifier> var1);
    }
}


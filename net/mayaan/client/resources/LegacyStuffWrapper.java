/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources;

import com.maayanlabs.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceManager;

public class LegacyStuffWrapper {
    @Deprecated
    public static int[] getPixels(ResourceManager resourceManager, Identifier location) throws IOException {
        try (InputStream resource = resourceManager.open(location);){
            NativeImage image = NativeImage.read(resource);
            try {
                int[] nArray = image.makePixelArray();
                if (image != null) {
                    image.close();
                }
                return nArray;
            }
            catch (Throwable throwable) {
                if (image != null) {
                    try {
                        image.close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
    }
}


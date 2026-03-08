/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.texture;

import java.io.IOException;
import java.nio.file.Path;
import net.mayaan.resources.Identifier;

public interface Dumpable {
    public void dumpContents(Identifier var1, Path var2) throws IOException;
}


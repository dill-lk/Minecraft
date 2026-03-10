/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix.operations;

import java.io.IOException;
import java.nio.file.Path;
import net.mayaan.util.worldupdate.UpgradeProgress;

public interface FileFixOperation {
    public void fix(Path var1, UpgradeProgress var2) throws IOException;
}


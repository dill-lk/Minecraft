/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.filefix.operations;

import java.io.IOException;
import java.nio.file.Path;
import net.mayaan.util.filefix.FileFixUtil;
import net.mayaan.util.filefix.operations.FileFixOperation;
import net.mayaan.util.worldupdate.UpgradeProgress;

public record DeleteFileOrEmptyDirectory(String target) implements FileFixOperation
{
    @Override
    public void fix(Path baseDirectory, UpgradeProgress upgradeProgress) throws IOException {
        FileFixUtil.deleteFileOrEmptyDirectory(baseDirectory, this.target);
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.schemas.Schema
 */
package net.mayaan.util.filefix.fixes;

import com.mojang.datafixers.schemas.Schema;
import net.mayaan.util.filefix.FileFix;
import net.mayaan.util.filefix.operations.FileFixOperations;

public class ResourcePackLocationFileFix
extends FileFix {
    public ResourcePackLocationFileFix(Schema schema) {
        super(schema);
    }

    @Override
    public void makeFixer() {
        this.addFileFixOperation(FileFixOperations.move("resources.zip", "resourcepacks/resources.zip"));
    }
}


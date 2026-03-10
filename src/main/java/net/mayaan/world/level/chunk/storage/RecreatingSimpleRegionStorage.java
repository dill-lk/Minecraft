/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixer
 *  org.apache.commons.io.FileUtils
 */
package net.mayaan.world.level.chunk.storage;

import com.mojang.datafixers.DataFixer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.storage.IOWorker;
import net.mayaan.world.level.chunk.storage.RegionStorageInfo;
import net.mayaan.world.level.chunk.storage.SimpleRegionStorage;
import org.apache.commons.io.FileUtils;

public class RecreatingSimpleRegionStorage
extends SimpleRegionStorage {
    private final IOWorker writeWorker;
    private final Path writeFolder;

    public RecreatingSimpleRegionStorage(RegionStorageInfo readInfo, Path readFolder, RegionStorageInfo writeInfo, Path writeFolder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType) {
        super(readInfo, readFolder, fixerUpper, syncWrites, dataFixType);
        this.writeFolder = writeFolder;
        this.writeWorker = new IOWorker(writeInfo, writeFolder, syncWrites);
    }

    @Override
    public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
        return this.writeWorker.store(pos, supplier);
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.writeWorker.close();
        if (this.writeFolder.toFile().exists()) {
            FileUtils.deleteDirectory((File)this.writeFolder.toFile());
        }
    }
}


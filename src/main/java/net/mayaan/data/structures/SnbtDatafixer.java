/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 */
package net.mayaan.data.structures;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import net.mayaan.DetectedVersion;
import net.mayaan.SharedConstants;
import net.mayaan.data.CachedOutput;
import net.mayaan.data.structures.NbtToSnbt;
import net.mayaan.data.structures.StructureUpdater;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.server.Bootstrap;

public class SnbtDatafixer {
    public static void main(String[] args) throws IOException {
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
        for (String dir : args) {
            SnbtDatafixer.updateInDirectory(dir);
        }
    }

    private static void updateInDirectory(String structureDir) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(structureDir, new String[0]), new FileVisitOption[0]);){
            walk.filter(path -> path.toString().endsWith(".snbt")).forEach(path -> {
                try {
                    String snbt = Files.readString(path);
                    CompoundTag readSnbt = NbtUtils.snbtToStructure(snbt);
                    CompoundTag updatedTag = StructureUpdater.update(path.toString(), readSnbt);
                    NbtToSnbt.writeSnbt(CachedOutput.NO_CACHE, path, NbtUtils.structureToSnbt(updatedTag));
                }
                catch (CommandSyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL$TypeReference
 */
package net.mayaan.util.filefix.access;

import com.mojang.datafixers.DSL;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.util.filefix.access.ChunkNbt;
import net.mayaan.util.filefix.access.CompressedNbt;
import net.mayaan.util.filefix.access.FileResourceType;
import net.mayaan.util.filefix.access.LevelDat;
import net.mayaan.util.filefix.access.PlayerData;
import net.mayaan.util.filefix.access.SavedDataNbt;
import net.mayaan.world.level.chunk.storage.RegionStorageInfo;

public class FileResourceTypes {
    public static final FileResourceType<LevelDat> LEVEL_DAT = new FileResourceType<LevelDat>(LevelDat::new);
    public static final FileResourceType<PlayerData> PLAYER_DATA = new FileResourceType<PlayerData>(PlayerData::new);

    public static FileResourceType<SavedDataNbt> savedData(DSL.TypeReference type) {
        return FileResourceTypes.savedData(type, CompressedNbt.MissingSeverity.NEUTRAL);
    }

    public static FileResourceType<SavedDataNbt> savedData(DSL.TypeReference type, CompressedNbt.MissingSeverity missingSeverity) {
        return new FileResourceType<SavedDataNbt>((path, dataVersion) -> new SavedDataNbt(type, path, dataVersion, missingSeverity));
    }

    public static FileResourceType<ChunkNbt> chunk(DataFixTypes type, RegionStorageInfo info) {
        return new FileResourceType<ChunkNbt>((path, dataVersion) -> new ChunkNbt(info, path, type, dataVersion));
    }
}


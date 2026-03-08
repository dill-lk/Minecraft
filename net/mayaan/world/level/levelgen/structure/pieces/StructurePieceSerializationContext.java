/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.pieces;

import net.mayaan.core.RegistryAccess;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public record StructurePieceSerializationContext(ResourceManager resourceManager, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager) {
    public static StructurePieceSerializationContext fromLevel(ServerLevel level) {
        MayaanServer server = level.getServer();
        return new StructurePieceSerializationContext(server.getResourceManager(), server.registryAccess(), server.getStructureManager());
    }
}


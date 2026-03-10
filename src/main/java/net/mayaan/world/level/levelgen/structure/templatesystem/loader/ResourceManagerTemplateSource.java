/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixer
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.world.level.levelgen.structure.templatesystem.loader;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.util.Optional;
import java.util.stream.Stream;
import net.mayaan.core.HolderGetter;
import net.mayaan.resources.FileToIdConverter;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.mayaan.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import org.slf4j.Logger;

public class ResourceManagerTemplateSource
extends TemplateSource {
    private static final Logger LOGGER = LogUtils.getLogger();
    private ResourceManager resourceManager;
    private final FileToIdConverter fileToIdConverter;

    public ResourceManagerTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup, ResourceManager resourceManager, FileToIdConverter fileToIdConverter) {
        super(fixerUpper, blockLookup);
        this.resourceManager = resourceManager;
        this.fileToIdConverter = fileToIdConverter;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    @Override
    public Optional<StructureTemplate> load(Identifier id) {
        Identifier identifier = this.fileToIdConverter.idToFile(id);
        return this.load(() -> this.resourceManager.open(identifier), false, e -> LOGGER.error("Couldn't load structure {}", (Object)id, e));
    }

    @Override
    public Stream<Identifier> list() {
        return this.fileToIdConverter.listMatchingResources(this.resourceManager).keySet().stream().map(this.fileToIdConverter::fileToId);
    }
}


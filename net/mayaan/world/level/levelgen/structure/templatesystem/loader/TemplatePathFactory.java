/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.levelgen.structure.templatesystem.loader;

import java.nio.file.Path;
import java.util.List;
import net.mayaan.IdentifierException;
import net.mayaan.resources.FileToIdConverter;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.PackType;
import net.mayaan.util.FileUtil;

public class TemplatePathFactory {
    private final Path sourceDir;

    public TemplatePathFactory(Path sourceDir, PackType packType) {
        this(sourceDir.resolve(packType.getDirectory()));
    }

    public TemplatePathFactory(Path sourceDir) {
        this.sourceDir = sourceDir;
    }

    public Path createAndValidatePathToStructure(Identifier id, FileToIdConverter converter) {
        return this.createAndValidatePathToStructure(converter.idToFile(id));
    }

    public Path createAndValidatePathToStructure(Identifier resourceLocation) {
        Path namespacePath = this.sourceDir.resolve(resourceLocation.getNamespace());
        List decomposedPath = (List)FileUtil.decomposePath(resourceLocation.getPath()).getOrThrow(msg -> new IdentifierException("Invalid file path '" + String.valueOf(resourceLocation) + "': " + msg));
        if (!decomposedPath.stream().allMatch(FileUtil::isPathPartPortable)) {
            throw new IdentifierException("Resource path '" + String.valueOf(resourceLocation) + "' is not portable");
        }
        return FileUtil.resolvePath(namespacePath, decomposedPath);
    }
}


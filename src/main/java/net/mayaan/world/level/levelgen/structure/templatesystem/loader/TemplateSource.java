/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.DataFixer
 */
package net.mayaan.world.level.levelgen.structure.templatesystem.loader;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.mayaan.core.HolderGetter;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.NbtAccounter;
import net.mayaan.nbt.NbtIo;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.resources.IoSupplier;
import net.mayaan.util.FastBufferedInputStream;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplate;

public abstract class TemplateSource {
    private final DataFixer fixerUpper;
    private final HolderGetter<Block> blockLookup;

    protected TemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
        this.fixerUpper = fixerUpper;
        this.blockLookup = blockLookup;
    }

    public abstract Optional<StructureTemplate> load(Identifier var1);

    public abstract Stream<Identifier> list();

    /*
     * Enabled aggressive exception aggregation
     */
    protected Optional<StructureTemplate> load(IoSupplier<InputStream> opener, boolean asText, Consumer<Throwable> onError) {
        try (InputStream rawInput = opener.get();){
            Optional<StructureTemplate> optional;
            try (FastBufferedInputStream input = new FastBufferedInputStream(rawInput);){
                CompoundTag structureTag = asText ? TemplateSource.readTextStructure(input) : TemplateSource.readStructure(input);
                optional = Optional.of(this.readStructure(structureTag));
            }
            return optional;
        }
        catch (FileNotFoundException e) {
            return Optional.empty();
        }
        catch (Throwable e) {
            onError.accept(e);
            return Optional.empty();
        }
    }

    private static CompoundTag readStructure(InputStream input) throws IOException {
        return NbtIo.readCompressed(input, NbtAccounter.unlimitedHeap());
    }

    private static CompoundTag readTextStructure(InputStream input) throws IOException, CommandSyntaxException {
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);){
            String contents = reader.readAllAsString();
            CompoundTag compoundTag = NbtUtils.snbtToStructure(contents);
            return compoundTag;
        }
    }

    private StructureTemplate readStructure(CompoundTag tag) {
        StructureTemplate structureTemplate = new StructureTemplate();
        int version = NbtUtils.getDataVersion(tag, 500);
        structureTemplate.load(this.blockLookup, DataFixTypes.STRUCTURE.updateToCurrentVersion(this.fixerUpper, tag, version));
        return structureTemplate;
    }
}


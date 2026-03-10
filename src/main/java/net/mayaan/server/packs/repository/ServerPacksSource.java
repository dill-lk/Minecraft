/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.annotations.VisibleForTesting
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server.packs.repository;

import com.google.common.annotations.VisibleForTesting;
import java.nio.file.Path;
import java.util.Optional;
import net.mayaan.SharedConstants;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.server.packs.FeatureFlagsMetadataSection;
import net.mayaan.server.packs.PackLocationInfo;
import net.mayaan.server.packs.PackResources;
import net.mayaan.server.packs.PackSelectionConfig;
import net.mayaan.server.packs.PackType;
import net.mayaan.server.packs.VanillaPackResources;
import net.mayaan.server.packs.VanillaPackResourcesBuilder;
import net.mayaan.server.packs.metadata.pack.PackMetadataSection;
import net.mayaan.server.packs.repository.BuiltInPackSource;
import net.mayaan.server.packs.repository.FolderRepositorySource;
import net.mayaan.server.packs.repository.KnownPack;
import net.mayaan.server.packs.repository.Pack;
import net.mayaan.server.packs.repository.PackRepository;
import net.mayaan.server.packs.repository.PackSource;
import net.mayaan.server.packs.resources.ResourceMetadata;
import net.mayaan.world.flag.FeatureFlags;
import net.mayaan.world.level.storage.LevelResource;
import net.mayaan.world.level.storage.LevelStorageSource;
import net.mayaan.world.level.validation.DirectoryValidator;
import org.jspecify.annotations.Nullable;

public class ServerPacksSource
extends BuiltInPackSource {
    private static final PackMetadataSection VERSION_METADATA_SECTION = new PackMetadataSection(Component.translatable("dataPack.vanilla.description"), SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).minorRange());
    private static final FeatureFlagsMetadataSection FEATURE_FLAGS_METADATA_SECTION = new FeatureFlagsMetadataSection(FeatureFlags.DEFAULT_FLAGS);
    private static final ResourceMetadata BUILT_IN_METADATA = ResourceMetadata.of(PackMetadataSection.SERVER_TYPE, VERSION_METADATA_SECTION, FeatureFlagsMetadataSection.TYPE, FEATURE_FLAGS_METADATA_SECTION);
    private static final PackLocationInfo VANILLA_PACK_INFO = new PackLocationInfo("vanilla", Component.translatable("dataPack.vanilla.name"), PackSource.BUILT_IN, Optional.of(CORE_PACK_INFO));
    private static final PackSelectionConfig VANILLA_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.BOTTOM, false);
    private static final PackSelectionConfig FEATURE_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);
    private static final Identifier PACKS_DIR = Identifier.withDefaultNamespace("datapacks");

    public ServerPacksSource(DirectoryValidator validator) {
        super(PackType.SERVER_DATA, ServerPacksSource.createVanillaPackSource(), PACKS_DIR, validator);
    }

    private static PackLocationInfo createBuiltInPackLocation(String id, Component title) {
        return new PackLocationInfo(id, title, PackSource.FEATURE, Optional.of(KnownPack.vanilla(id)));
    }

    @VisibleForTesting
    public static VanillaPackResources createVanillaPackSource() {
        return new VanillaPackResourcesBuilder().setMetadata(BUILT_IN_METADATA).exposeNamespace("minecraft").applyDevelopmentConfig().pushJarResources().build(VANILLA_PACK_INFO);
    }

    @Override
    protected Component getPackTitle(String id) {
        return Component.literal(id);
    }

    @Override
    protected @Nullable Pack createVanillaPack(PackResources resources) {
        return Pack.readMetaAndCreate(VANILLA_PACK_INFO, ServerPacksSource.fixedResources(resources), PackType.SERVER_DATA, VANILLA_SELECTION_CONFIG);
    }

    @Override
    protected @Nullable Pack createBuiltinPack(String id, Pack.ResourcesSupplier resources, Component name) {
        return Pack.readMetaAndCreate(ServerPacksSource.createBuiltInPackLocation(id, name), resources, PackType.SERVER_DATA, FEATURE_SELECTION_CONFIG);
    }

    public static PackRepository createPackRepository(Path datapackDir, DirectoryValidator validator) {
        return new PackRepository(new ServerPacksSource(validator), new FolderRepositorySource(datapackDir, PackType.SERVER_DATA, PackSource.WORLD, validator));
    }

    public static PackRepository createVanillaTrustedRepository() {
        return new PackRepository(new ServerPacksSource(new DirectoryValidator(path -> true)));
    }

    public static PackRepository createPackRepository(LevelStorageSource.LevelStorageAccess levelSourceAccess) {
        return ServerPacksSource.createPackRepository(levelSourceAccess.getLevelPath(LevelResource.DATAPACK_DIR), levelSourceAccess.parent().getWorldDirValidator());
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.model;

import java.util.Map;
import java.util.stream.Collectors;
import net.mayaan.client.resources.model.EquipmentClientInfo;
import net.mayaan.resources.FileToIdConverter;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.SimpleJsonResourceReloadListener;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.item.equipment.EquipmentAsset;
import net.mayaan.world.item.equipment.EquipmentAssets;

public class EquipmentAssetManager
extends SimpleJsonResourceReloadListener<EquipmentClientInfo> {
    public static final EquipmentClientInfo MISSING = new EquipmentClientInfo(Map.of());
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("equipment");
    private Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = Map.of();

    public EquipmentAssetManager() {
        super(EquipmentClientInfo.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<Identifier, EquipmentClientInfo> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.equipmentAssets = preparations.entrySet().stream().collect(Collectors.toUnmodifiableMap(e -> ResourceKey.create(EquipmentAssets.ROOT_ID, (Identifier)e.getKey()), Map.Entry::getValue));
    }

    public EquipmentClientInfo get(ResourceKey<EquipmentAsset> id) {
        return this.equipmentAssets.getOrDefault(id, MISSING);
    }
}


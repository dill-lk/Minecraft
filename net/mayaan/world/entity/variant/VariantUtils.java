/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.variant;

import java.util.Optional;
import java.util.stream.Stream;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.Registry;
import net.mayaan.core.RegistryAccess;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.entity.variant.PriorityProvider;
import net.mayaan.world.entity.variant.SpawnContext;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class VariantUtils {
    public static final String TAG_VARIANT = "variant";

    public static <T> Holder<T> getDefaultOrAny(RegistryAccess registryAccess, ResourceKey<T> id) {
        HolderLookup.RegistryLookup registry = registryAccess.lookupOrThrow(id.registryKey());
        return (Holder)registry.get(id).or(((Registry)registry)::getAny).orElseThrow();
    }

    public static <T> Holder<T> getAny(RegistryAccess registryAccess, ResourceKey<? extends Registry<T>> registryId) {
        return registryAccess.lookupOrThrow(registryId).getAny().orElseThrow();
    }

    public static <T> void writeVariant(ValueOutput output, Holder<T> holder) {
        holder.unwrapKey().ifPresent(k -> output.store(TAG_VARIANT, Identifier.CODEC, k.identifier()));
    }

    public static <T> Optional<Holder<T>> readVariant(ValueInput input, ResourceKey<? extends Registry<T>> registryId) {
        return input.read(TAG_VARIANT, Identifier.CODEC).map(id -> ResourceKey.create(registryId, id)).flatMap(input.lookup()::get);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<Holder.Reference<T>> selectVariantToSpawn(SpawnContext context, ResourceKey<Registry<T>> variantRegistry) {
        ServerLevelAccessor level = context.level();
        Stream entries = level.registryAccess().lookupOrThrow(variantRegistry).listElements();
        return PriorityProvider.pick(entries, Holder::value, level.getRandom(), context);
    }
}


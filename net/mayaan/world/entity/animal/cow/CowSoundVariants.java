/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.cow;

import net.mayaan.core.Holder;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.animal.cow.CowSoundVariant;

public class CowSoundVariants {
    public static final ResourceKey<CowSoundVariant> CLASSIC = CowSoundVariants.createKey(SoundSet.CLASSIC);
    public static final ResourceKey<CowSoundVariant> MOODY = CowSoundVariants.createKey(SoundSet.MOODY);

    private static ResourceKey<CowSoundVariant> createKey(SoundSet cowSoundVariant) {
        return ResourceKey.create(Registries.COW_SOUND_VARIANT, Identifier.withDefaultNamespace(cowSoundVariant.getIdentifier()));
    }

    public static void bootstrap(BootstrapContext<CowSoundVariant> context) {
        CowSoundVariants.register(context, CLASSIC, SoundSet.CLASSIC);
        CowSoundVariants.register(context, MOODY, SoundSet.MOODY);
    }

    private static void register(BootstrapContext<CowSoundVariant> context, ResourceKey<CowSoundVariant> key, SoundSet CowSoundVariant2) {
        context.register(key, SoundEvents.COW_SOUNDS.get((Object)CowSoundVariant2));
    }

    public static Holder<CowSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
        return registryAccess.lookupOrThrow(Registries.COW_SOUND_VARIANT).getRandom(random).orElseThrow();
    }

    public static enum SoundSet {
        CLASSIC("classic", "cow"),
        MOODY("moody", "cow_moody");

        private final String identifier;
        private final String soundEventIdentifier;

        private SoundSet(String identifier, String soundEventIdentifier) {
            this.identifier = identifier;
            this.soundEventIdentifier = soundEventIdentifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getSoundEventIdentifier() {
            return this.soundEventIdentifier;
        }
    }
}


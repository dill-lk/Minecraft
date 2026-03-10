/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.chicken;

import net.mayaan.core.Holder;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.animal.chicken.ChickenSoundVariant;

public class ChickenSoundVariants {
    public static final ResourceKey<ChickenSoundVariant> CLASSIC = ChickenSoundVariants.createKey(SoundSet.CLASSIC);
    public static final ResourceKey<ChickenSoundVariant> PICKY = ChickenSoundVariants.createKey(SoundSet.PICKY);

    private static ResourceKey<ChickenSoundVariant> createKey(SoundSet chickenSoundVariant) {
        return ResourceKey.create(Registries.CHICKEN_SOUND_VARIANT, Identifier.withDefaultNamespace(chickenSoundVariant.getIdentifier()));
    }

    public static void bootstrap(BootstrapContext<ChickenSoundVariant> context) {
        ChickenSoundVariants.register(context, CLASSIC, SoundSet.CLASSIC);
        ChickenSoundVariants.register(context, PICKY, SoundSet.PICKY);
    }

    private static void register(BootstrapContext<ChickenSoundVariant> context, ResourceKey<ChickenSoundVariant> key, SoundSet ChickenSoundVariant2) {
        context.register(key, SoundEvents.CHICKEN_SOUNDS.get((Object)ChickenSoundVariant2));
    }

    public static Holder<ChickenSoundVariant> pickRandomSoundVariant(RegistryAccess registryAccess, RandomSource random) {
        return registryAccess.lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT).getRandom(random).orElseThrow();
    }

    public static enum SoundSet {
        CLASSIC("classic", "chicken"),
        PICKY("picky", "chicken_picky");

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


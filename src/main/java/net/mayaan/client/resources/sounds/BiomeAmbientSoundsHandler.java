/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.resources.sounds;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Objects;
import net.mayaan.client.player.LocalPlayer;
import net.mayaan.client.resources.sounds.AbstractTickableSoundInstance;
import net.mayaan.client.resources.sounds.AmbientSoundHandler;
import net.mayaan.client.resources.sounds.SimpleSoundInstance;
import net.mayaan.client.resources.sounds.SoundInstance;
import net.mayaan.client.sounds.SoundManager;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.attribute.AmbientAdditionsSettings;
import net.mayaan.world.attribute.AmbientSounds;
import net.mayaan.world.attribute.EnvironmentAttributeSystem;
import net.mayaan.world.attribute.EnvironmentAttributes;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LightLayer;
import org.jspecify.annotations.Nullable;

public class BiomeAmbientSoundsHandler
implements AmbientSoundHandler {
    private static final int LOOP_SOUND_CROSS_FADE_TIME = 40;
    private static final float SKY_MOOD_RECOVERY_RATE = 0.001f;
    private final LocalPlayer player;
    private final SoundManager soundManager;
    private final RandomSource random;
    private final Object2ObjectArrayMap<Holder<SoundEvent>, LoopSoundInstance> loopSounds = new Object2ObjectArrayMap();
    private float moodiness;
    private @Nullable Holder<SoundEvent> previousLoopSound;

    public BiomeAmbientSoundsHandler(LocalPlayer player, SoundManager soundManager) {
        this.random = player.level().getRandom();
        this.player = player;
        this.soundManager = soundManager;
    }

    public float getMoodiness() {
        return this.moodiness;
    }

    @Override
    public void tick() {
        this.loopSounds.values().removeIf(AbstractTickableSoundInstance::isStopped);
        Level level = this.player.level();
        EnvironmentAttributeSystem environmentAttributes = level.environmentAttributes();
        AmbientSounds ambientSounds = environmentAttributes.getValue(EnvironmentAttributes.AMBIENT_SOUNDS, this.player.position());
        Holder currentLoopSound = ambientSounds.loop().orElse(null);
        if (!Objects.equals(currentLoopSound, this.previousLoopSound)) {
            this.previousLoopSound = currentLoopSound;
            this.loopSounds.values().forEach(LoopSoundInstance::fadeOut);
            if (currentLoopSound != null) {
                this.loopSounds.compute((Object)currentLoopSound, (biomeKey, soundInstance) -> {
                    if (soundInstance == null) {
                        soundInstance = new LoopSoundInstance((SoundEvent)currentLoopSound.value());
                        this.soundManager.play((SoundInstance)soundInstance);
                    }
                    soundInstance.fadeIn();
                    return soundInstance;
                });
            }
        }
        for (AmbientAdditionsSettings additions : ambientSounds.additions()) {
            if (!(this.random.nextDouble() < additions.tickChance())) continue;
            this.soundManager.play(SimpleSoundInstance.forAmbientAddition(additions.soundEvent().value()));
        }
        ambientSounds.mood().ifPresent(mood -> {
            int searchSpan = mood.blockSearchExtent() * 2 + 1;
            BlockPos blockSamplingPos = BlockPos.containing(this.player.getX() + (double)this.random.nextInt(searchSpan) - (double)mood.blockSearchExtent(), this.player.getEyeY() + (double)this.random.nextInt(searchSpan) - (double)mood.blockSearchExtent(), this.player.getZ() + (double)this.random.nextInt(searchSpan) - (double)mood.blockSearchExtent());
            int skyBrightness = level.getBrightness(LightLayer.SKY, blockSamplingPos);
            this.moodiness = skyBrightness > 0 ? (this.moodiness -= (float)skyBrightness / 15.0f * 0.001f) : (this.moodiness -= (float)(level.getBrightness(LightLayer.BLOCK, blockSamplingPos) - 1) / (float)mood.tickDelay());
            if (this.moodiness >= 1.0f) {
                double blockSampleX = (double)blockSamplingPos.getX() + 0.5;
                double blockSampleY = (double)blockSamplingPos.getY() + 0.5;
                double blockSampleZ = (double)blockSamplingPos.getZ() + 0.5;
                double blockDirectionX = blockSampleX - this.player.getX();
                double blockDirectionY = blockSampleY - this.player.getEyeY();
                double blockDirectionZ = blockSampleZ - this.player.getZ();
                double blockDistance = Math.sqrt(blockDirectionX * blockDirectionX + blockDirectionY * blockDirectionY + blockDirectionZ * blockDirectionZ);
                double soundSourceDistance = blockDistance + mood.soundPositionOffset();
                SimpleSoundInstance moodSoundInstance = SimpleSoundInstance.forAmbientMood(mood.soundEvent().value(), this.random, this.player.getX() + blockDirectionX / blockDistance * soundSourceDistance, this.player.getEyeY() + blockDirectionY / blockDistance * soundSourceDistance, this.player.getZ() + blockDirectionZ / blockDistance * soundSourceDistance);
                this.soundManager.play(moodSoundInstance);
                this.moodiness = 0.0f;
            } else {
                this.moodiness = Math.max(this.moodiness, 0.0f);
            }
        });
    }

    public static class LoopSoundInstance
    extends AbstractTickableSoundInstance {
        private int fadeDirection;
        private int fade;

        public LoopSoundInstance(SoundEvent soundEvent) {
            super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.looping = true;
            this.delay = 0;
            this.volume = 1.0f;
            this.relative = true;
        }

        @Override
        public void tick() {
            if (this.fade < 0) {
                this.stop();
            }
            this.fade += this.fadeDirection;
            this.volume = Mth.clamp((float)this.fade / 40.0f, 0.0f, 1.0f);
        }

        public void fadeOut() {
            this.fade = Math.min(this.fade, 40);
            this.fadeDirection = -1;
        }

        public void fadeIn() {
            this.fade = Math.max(0, this.fade);
            this.fadeDirection = 1;
        }
    }
}


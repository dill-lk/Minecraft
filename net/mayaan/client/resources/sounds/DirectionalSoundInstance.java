/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.resources.sounds;

import net.mayaan.client.Camera;
import net.mayaan.client.resources.sounds.AbstractTickableSoundInstance;
import net.mayaan.client.resources.sounds.SoundInstance;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundSource;
import net.mayaan.util.RandomSource;
import net.mayaan.world.phys.Vec3;

public class DirectionalSoundInstance
extends AbstractTickableSoundInstance {
    private final Camera camera;
    private final float xAngle;
    private final float yAngle;

    public DirectionalSoundInstance(SoundEvent event, SoundSource source, RandomSource random, Camera camera, float xAngle, float yAngle) {
        super(event, source, random);
        this.camera = camera;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.setPosition();
    }

    private void setPosition() {
        Vec3 direction = Vec3.directionFromRotation(this.xAngle, this.yAngle).scale(10.0);
        this.x = this.camera.position().x + direction.x;
        this.y = this.camera.position().y + direction.y;
        this.z = this.camera.position().z + direction.z;
        this.attenuation = SoundInstance.Attenuation.NONE;
    }

    @Override
    public void tick() {
        this.setPosition();
    }
}


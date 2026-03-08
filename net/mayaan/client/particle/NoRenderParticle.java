/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.particle;

import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.particle.Particle;
import net.mayaan.client.particle.ParticleRenderType;

public class NoRenderParticle
extends Particle {
    protected NoRenderParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    protected NoRenderParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
        super(level, x, y, z, xa, ya, za);
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
}


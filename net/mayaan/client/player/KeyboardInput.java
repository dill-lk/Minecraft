/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.player;

import net.mayaan.client.Options;
import net.mayaan.client.player.ClientInput;
import net.mayaan.world.entity.player.Input;
import net.mayaan.world.phys.Vec2;

public class KeyboardInput
extends ClientInput {
    private final Options options;

    public KeyboardInput(Options options) {
        this.options = options;
    }

    private static float calculateImpulse(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }

    @Override
    public void tick() {
        this.keyPresses = new Input(this.options.keyUp.isDown(), this.options.keyDown.isDown(), this.options.keyLeft.isDown(), this.options.keyRight.isDown(), this.options.keyJump.isDown(), this.options.keyShift.isDown(), this.options.keySprint.isDown());
        float forwardImpulse = KeyboardInput.calculateImpulse(this.keyPresses.forward(), this.keyPresses.backward());
        float leftImpulse = KeyboardInput.calculateImpulse(this.keyPresses.left(), this.keyPresses.right());
        this.moveVector = new Vec2(leftImpulse, forwardImpulse).normalized();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package net.mayaan.client.renderer.block.dispatch;

import com.maayanlabs.math.Transformation;
import net.mayaan.core.Direction;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public interface ModelState {
    public static final Matrix4fc NO_TRANSFORM = new Matrix4f();

    default public Transformation transformation() {
        return Transformation.IDENTITY;
    }

    default public Matrix4fc faceTransformation(Direction face) {
        return NO_TRANSFORM;
    }

    default public Matrix4fc inverseFaceTransformation(Direction face) {
        return NO_TRANSFORM;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Quaternionf
 */
package net.mayaan.client.renderer.state.level;

import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.fog.FogData;
import net.mayaan.client.renderer.state.level.CameraEntityRenderState;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.material.FogType;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class CameraRenderState {
    public BlockPos blockPos = BlockPos.ZERO;
    public Vec3 pos = new Vec3(0.0, 0.0, 0.0);
    public float xRot;
    public float yRot;
    public boolean initialized;
    public boolean isPanoramicMode;
    public Quaternionf orientation = new Quaternionf();
    public Frustum cullFrustum = new Frustum(new Matrix4f(), new Matrix4f());
    public FogType fogType = FogType.NONE;
    public FogData fogData = new FogData();
    public float hudFov;
    public float depthFar;
    public Matrix4f projectionMatrix = new Matrix4f();
    public Matrix4f viewRotationMatrix = new Matrix4f();
    public CameraEntityRenderState entityRenderState = new CameraEntityRenderState();
}


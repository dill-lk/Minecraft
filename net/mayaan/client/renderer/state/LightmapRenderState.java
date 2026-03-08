/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3fc
 */
package net.mayaan.client.renderer.state;

import net.mayaan.client.renderer.LightmapRenderStateExtractor;
import org.joml.Vector3fc;

public class LightmapRenderState {
    public boolean needsUpdate = false;
    public float blockFactor;
    public Vector3fc blockLightTint = LightmapRenderStateExtractor.WHITE;
    public float skyFactor;
    public Vector3fc skyLightColor = LightmapRenderStateExtractor.WHITE;
    public Vector3fc ambientColor = LightmapRenderStateExtractor.WHITE;
    public float brightness;
    public float darknessEffectScale;
    public float nightVisionEffectIntensity;
    public Vector3fc nightVisionColor = LightmapRenderStateExtractor.WHITE;
    public float bossOverlayWorldDarkening;
}


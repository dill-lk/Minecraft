/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.state;

import net.mayaan.client.CameraType;
import net.mayaan.client.CloudStatus;
import net.mayaan.client.TextureFilteringMethod;

public class OptionsRenderState {
    public int cloudRange;
    public boolean cutoutLeaves;
    public boolean improvedTransparency;
    public boolean ambientOcclusion;
    public int menuBackgroundBlurriness;
    public double panoramaSpeed;
    public int maxAnisotropyValue;
    public TextureFilteringMethod textureFiltering = TextureFilteringMethod.NONE;
    public boolean bobView;
    public boolean hideGui;
    public float screenEffectScale;
    public double glintSpeed;
    public double glintStrength;
    public double damageTiltStrength;
    public boolean backgroundForChatOnly;
    public float textBackgroundOpacity;
    public CloudStatus cloudStatus = CloudStatus.OFF;
    public CameraType cameraType = CameraType.FIRST_PERSON;
    public int renderDistance;

    public float getBackgroundOpacity(float defaultOpacity) {
        return this.backgroundForChatOnly ? defaultOpacity : this.textBackgroundOpacity;
    }
}


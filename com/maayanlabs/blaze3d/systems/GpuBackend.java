/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.blaze3d.systems;

import com.maayanlabs.blaze3d.GLFWErrorCapture;
import com.maayanlabs.blaze3d.shaders.GpuDebugOptions;
import com.maayanlabs.blaze3d.shaders.ShaderSource;
import com.maayanlabs.blaze3d.systems.BackendCreationException;
import com.maayanlabs.blaze3d.systems.GpuDevice;

public interface GpuBackend {
    public String getName();

    public void setWindowHints();

    public void handleWindowCreationErrors(GLFWErrorCapture.Error var1) throws BackendCreationException;

    public GpuDevice createDevice(long var1, ShaderSource var3, GpuDebugOptions var4);
}


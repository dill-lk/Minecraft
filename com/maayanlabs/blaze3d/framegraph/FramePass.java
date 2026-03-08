/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.blaze3d.framegraph;

import com.maayanlabs.blaze3d.resource.ResourceDescriptor;
import com.maayanlabs.blaze3d.resource.ResourceHandle;

public interface FramePass {
    public <T> ResourceHandle<T> createsInternal(String var1, ResourceDescriptor<T> var2);

    public <T> void reads(ResourceHandle<T> var1);

    public <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> var1);

    public void requires(FramePass var1);

    public void disableCulling();

    public void executes(Runnable var1);
}


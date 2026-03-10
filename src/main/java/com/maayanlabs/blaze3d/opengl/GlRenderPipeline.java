/*
 * Decompiled with CFR 0.152.
 */
package com.maayanlabs.blaze3d.opengl;

import com.maayanlabs.blaze3d.opengl.GlProgram;
import com.maayanlabs.blaze3d.pipeline.CompiledRenderPipeline;
import com.maayanlabs.blaze3d.pipeline.RenderPipeline;

public record GlRenderPipeline(RenderPipeline info, GlProgram program) implements CompiledRenderPipeline
{
    @Override
    public boolean isValid() {
        return this.program != GlProgram.INVALID_PROGRAM;
    }
}


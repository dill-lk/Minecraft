/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.chunk;

import com.maayanlabs.blaze3d.pipeline.RenderTarget;
import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;

public enum ChunkSectionLayerGroup {
    OPAQUE(ChunkSectionLayer.SOLID, ChunkSectionLayer.CUTOUT),
    TRANSLUCENT(ChunkSectionLayer.TRANSLUCENT);

    private final String label;
    private final ChunkSectionLayer[] layers;

    private ChunkSectionLayerGroup(ChunkSectionLayer ... layers) {
        this.layers = layers;
        this.label = this.toString().toLowerCase(Locale.ROOT);
    }

    public String label() {
        return this.label;
    }

    public ChunkSectionLayer[] layers() {
        return this.layers;
    }

    public RenderTarget outputTarget() {
        Mayaan minecraft = Mayaan.getInstance();
        RenderTarget renderTarget = switch (this.ordinal()) {
            case 1 -> minecraft.levelRenderer.getTranslucentTarget();
            default -> minecraft.getMainRenderTarget();
        };
        return renderTarget != null ? renderTarget : minecraft.getMainRenderTarget();
    }
}


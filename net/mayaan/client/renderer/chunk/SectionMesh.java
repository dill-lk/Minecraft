/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.chunk;

import com.maayanlabs.blaze3d.vertex.VertexFormat;
import java.util.Collections;
import java.util.List;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;
import net.mayaan.client.renderer.chunk.TranslucencyPointOfView;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

public interface SectionMesh
extends AutoCloseable {
    default public boolean isDifferentPointOfView(TranslucencyPointOfView pointOfView) {
        return false;
    }

    default public boolean hasRenderableLayers() {
        return false;
    }

    default public boolean hasTranslucentGeometry() {
        return false;
    }

    default public boolean isEmpty(ChunkSectionLayer layer) {
        return true;
    }

    default public List<BlockEntity> getRenderableBlockEntities() {
        return Collections.emptyList();
    }

    public boolean facesCanSeeEachother(Direction var1, Direction var2);

    default public @Nullable SectionDraw getSectionDraw(ChunkSectionLayer layer) {
        return null;
    }

    @Override
    default public void close() {
    }

    public record SectionDraw(int indexCount, VertexFormat.IndexType indexType, boolean hasCustomIndexBuffer) {
    }
}


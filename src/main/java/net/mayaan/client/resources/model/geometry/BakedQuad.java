/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector3fc
 */
package net.mayaan.client.resources.model.geometry;

import com.maayanlabs.blaze3d.platform.Transparency;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.texture.TextureAtlas;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.client.resources.model.sprite.Material;
import net.mayaan.core.Direction;
import org.joml.Vector3fc;

public record BakedQuad(Vector3fc position0, Vector3fc position1, Vector3fc position2, Vector3fc position3, long packedUV0, long packedUV1, long packedUV2, long packedUV3, int tintIndex, Direction direction, SpriteInfo spriteInfo, boolean shade, int lightEmission) {
    public static final int VERTEX_COUNT = 4;

    public boolean isTinted() {
        return this.tintIndex != -1;
    }

    public Vector3fc position(int vertex) {
        return switch (vertex) {
            case 0 -> this.position0;
            case 1 -> this.position1;
            case 2 -> this.position2;
            case 3 -> this.position3;
            default -> throw new IndexOutOfBoundsException(vertex);
        };
    }

    public long packedUV(int vertex) {
        return switch (vertex) {
            case 0 -> this.packedUV0;
            case 1 -> this.packedUV1;
            case 2 -> this.packedUV2;
            case 3 -> this.packedUV3;
            default -> throw new IndexOutOfBoundsException(vertex);
        };
    }

    public record SpriteInfo(TextureAtlasSprite sprite, ChunkSectionLayer layer, RenderType itemRenderType) {
        public static SpriteInfo of(Material.Baked material, Transparency transparency) {
            ChunkSectionLayer layer = ChunkSectionLayer.byTransparency(transparency);
            RenderType itemRenderType = material.sprite().atlasLocation().equals(TextureAtlas.LOCATION_BLOCKS) ? (transparency.hasTranslucent() ? Sheets.translucentBlockItemSheet() : Sheets.cutoutBlockItemSheet()) : (transparency.hasTranslucent() ? Sheets.translucentItemSheet() : Sheets.cutoutItemSheet());
            return new SpriteInfo(material.sprite(), layer, itemRenderType);
        }
    }
}


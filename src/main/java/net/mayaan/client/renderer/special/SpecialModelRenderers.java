/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.client.renderer.special;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.client.renderer.special.BannerSpecialRenderer;
import net.mayaan.client.renderer.special.BedSpecialRenderer;
import net.mayaan.client.renderer.special.BellSpecialRenderer;
import net.mayaan.client.renderer.special.BookSpecialRenderer;
import net.mayaan.client.renderer.special.ChestSpecialRenderer;
import net.mayaan.client.renderer.special.ConduitSpecialRenderer;
import net.mayaan.client.renderer.special.CopperGolemStatueSpecialRenderer;
import net.mayaan.client.renderer.special.DecoratedPotSpecialRenderer;
import net.mayaan.client.renderer.special.HangingSignSpecialRenderer;
import net.mayaan.client.renderer.special.PlayerHeadSpecialRenderer;
import net.mayaan.client.renderer.special.ShieldSpecialRenderer;
import net.mayaan.client.renderer.special.ShulkerBoxSpecialRenderer;
import net.mayaan.client.renderer.special.SkullSpecialRenderer;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.renderer.special.StandingSignSpecialRenderer;
import net.mayaan.client.renderer.special.TridentSpecialRenderer;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ExtraCodecs;

public class SpecialModelRenderers {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked<?>>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final Codec<SpecialModelRenderer.Unbaked<?>> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(SpecialModelRenderer.Unbaked::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("bed"), BedSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("bell"), BellSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("banner"), BannerSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("book"), BookSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("conduit"), ConduitSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("chest"), ChestSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("copper_golem_statue"), CopperGolemStatueSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("head"), SkullSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("player_head"), PlayerHeadSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("shulker_box"), ShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("shield"), ShieldSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("trident"), TridentSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("decorated_pot"), DecoratedPotSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("standing_sign"), StandingSignSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("hanging_sign"), HangingSignSpecialRenderer.Unbaked.MAP_CODEC);
    }
}


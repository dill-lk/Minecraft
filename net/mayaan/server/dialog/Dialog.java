/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.server.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.mayaan.core.Holder;
import net.mayaan.core.HolderSet;
import net.mayaan.core.RegistryCodecs;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.resources.RegistryFileCodec;
import net.mayaan.server.dialog.CommonDialogData;
import net.mayaan.server.dialog.action.Action;
import net.mayaan.util.ExtraCodecs;

public interface Dialog {
    public static final Codec<Integer> WIDTH_CODEC = ExtraCodecs.intRange(1, 1024);
    public static final Codec<Dialog> DIRECT_CODEC = BuiltInRegistries.DIALOG_TYPE.byNameCodec().dispatch(Dialog::codec, c -> c);
    public static final Codec<Holder<Dialog>> CODEC = RegistryFileCodec.create(Registries.DIALOG, DIRECT_CODEC);
    public static final Codec<HolderSet<Dialog>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.DIALOG, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Dialog>> STREAM_CODEC = ByteBufCodecs.holder(Registries.DIALOG, ByteBufCodecs.fromCodecWithRegistriesTrusted(DIRECT_CODEC));
    public static final StreamCodec<ByteBuf, Dialog> CONTEXT_FREE_STREAM_CODEC = ByteBufCodecs.fromCodecTrusted(DIRECT_CODEC);

    public CommonDialogData common();

    public MapCodec<? extends Dialog> codec();

    public Optional<Action> onCancel();
}


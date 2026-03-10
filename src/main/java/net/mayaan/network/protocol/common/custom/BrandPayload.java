/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common.custom;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.common.custom.CustomPacketPayload;

public record BrandPayload(String brand) implements CustomPacketPayload
{
    public static final StreamCodec<FriendlyByteBuf, BrandPayload> STREAM_CODEC = CustomPacketPayload.codec(BrandPayload::write, BrandPayload::new);
    public static final CustomPacketPayload.Type<BrandPayload> TYPE = CustomPacketPayload.createType("brand");

    private BrandPayload(FriendlyByteBuf input) {
        this(input.readUtf());
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.brand);
    }

    public CustomPacketPayload.Type<BrandPayload> type() {
        return TYPE;
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.DecoderException
 */
package net.mayaan.network;

import io.netty.handler.codec.DecoderException;
import net.mayaan.network.SkipPacketException;
import net.mayaan.network.codec.IdDispatchCodec;

public class SkipPacketDecoderException
extends DecoderException
implements IdDispatchCodec.DontDecorateException,
SkipPacketException {
    public SkipPacketDecoderException(String message) {
        super(message);
    }

    public SkipPacketDecoderException(Throwable cause) {
        super(cause);
    }
}


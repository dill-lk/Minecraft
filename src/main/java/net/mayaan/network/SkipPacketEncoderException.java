/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.handler.codec.EncoderException
 */
package net.mayaan.network;

import io.netty.handler.codec.EncoderException;
import net.mayaan.network.SkipPacketException;
import net.mayaan.network.codec.IdDispatchCodec;

public class SkipPacketEncoderException
extends EncoderException
implements IdDispatchCodec.DontDecorateException,
SkipPacketException {
    public SkipPacketEncoderException(String message) {
        super(message);
    }

    public SkipPacketEncoderException(Throwable cause) {
        super(cause);
    }
}


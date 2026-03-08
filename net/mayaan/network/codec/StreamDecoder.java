/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.codec;

@FunctionalInterface
public interface StreamDecoder<I, T> {
    public T decode(I var1);
}


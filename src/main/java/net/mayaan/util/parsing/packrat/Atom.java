/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.parsing.packrat;

public record Atom<T>(String name) {
    @Override
    public String toString() {
        return "<" + this.name + ">";
    }

    public static <T> Atom<T> of(String name) {
        return new Atom<T>(name);
    }
}


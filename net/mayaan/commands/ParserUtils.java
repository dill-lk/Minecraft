/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 */
package net.mayaan.commands;

import com.mojang.brigadier.StringReader;
import net.mayaan.CharPredicate;

public class ParserUtils {
    public static String readWhile(StringReader reader, CharPredicate predicate) {
        int start = reader.getCursor();
        while (reader.canRead() && predicate.test(reader.peek())) {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }
}


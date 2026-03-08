/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.context.CommandContext
 */
package net.mayaan.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.arguments.StringRepresentableArgument;
import net.mayaan.world.level.block.Mirror;

public class TemplateMirrorArgument
extends StringRepresentableArgument<Mirror> {
    private TemplateMirrorArgument() {
        super(Mirror.CODEC, Mirror::values);
    }

    public static StringRepresentableArgument<Mirror> templateMirror() {
        return new TemplateMirrorArgument();
    }

    public static Mirror getMirror(CommandContext<CommandSourceStack> context, String name) {
        return (Mirror)context.getArgument(name, Mirror.class);
    }
}


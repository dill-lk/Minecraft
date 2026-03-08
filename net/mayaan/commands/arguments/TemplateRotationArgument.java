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
import net.mayaan.world.level.block.Rotation;

public class TemplateRotationArgument
extends StringRepresentableArgument<Rotation> {
    private TemplateRotationArgument() {
        super(Rotation.CODEC, Rotation::values);
    }

    public static TemplateRotationArgument templateRotation() {
        return new TemplateRotationArgument();
    }

    public static Rotation getRotation(CommandContext<CommandSourceStack> context, String name) {
        return (Rotation)context.getArgument(name, Rotation.class);
    }
}


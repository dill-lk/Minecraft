/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.serialization.Codec
 */
package net.mayaan.commands.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.Locale;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.arguments.StringRepresentableArgument;
import net.mayaan.util.StringRepresentable;
import net.mayaan.world.level.levelgen.Heightmap;

public class HeightmapTypeArgument
extends StringRepresentableArgument<Heightmap.Types> {
    private static final Codec<Heightmap.Types> LOWER_CASE_CODEC = StringRepresentable.fromEnumWithMapping(HeightmapTypeArgument::keptTypes, s -> s.toLowerCase(Locale.ROOT));

    private static Heightmap.Types[] keptTypes() {
        return (Heightmap.Types[])Arrays.stream(Heightmap.Types.values()).filter(Heightmap.Types::keepAfterWorldgen).toArray(Heightmap.Types[]::new);
    }

    private HeightmapTypeArgument() {
        super(LOWER_CASE_CODEC, HeightmapTypeArgument::keptTypes);
    }

    public static HeightmapTypeArgument heightmap() {
        return new HeightmapTypeArgument();
    }

    public static Heightmap.Types getHeightmap(CommandContext<CommandSourceStack> context, String name) {
        return (Heightmap.Types)context.getArgument(name, Heightmap.Types.class);
    }

    @Override
    protected String convertId(String id) {
        return id.toLowerCase(Locale.ROOT);
    }
}


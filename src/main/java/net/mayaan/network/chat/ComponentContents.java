/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.chat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.network.chat.FormattedText;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.Style;
import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public interface ComponentContents {
    default public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
        return Optional.empty();
    }

    default public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
        return Optional.empty();
    }

    default public MutableComponent resolve(@Nullable CommandSourceStack source, @Nullable Entity entity, int recursionDepth) throws CommandSyntaxException {
        return MutableComponent.create(this);
    }

    public MapCodec<? extends ComponentContents> codec();
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.chat.contents;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.arguments.selector.EntitySelector;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentContents;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.FormattedText;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.Style;
import net.mayaan.util.CompilableString;
import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public record SelectorContents(CompilableString<EntitySelector> selector, Optional<Component> separator) implements ComponentContents
{
    public static final MapCodec<SelectorContents> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntitySelector.COMPILABLE_CODEC.fieldOf("selector").forGetter(SelectorContents::selector), (App)ComponentSerialization.CODEC.optionalFieldOf("separator").forGetter(SelectorContents::separator)).apply((Applicative)i, SelectorContents::new));

    public MapCodec<SelectorContents> codec() {
        return MAP_CODEC;
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack source, @Nullable Entity entity, int recursionDepth) throws CommandSyntaxException {
        if (source == null) {
            return Component.empty();
        }
        Optional<MutableComponent> resolvedSeparator = ComponentUtils.updateForEntity(source, this.separator, entity, recursionDepth);
        return ComponentUtils.formatList(this.selector.compiled().findEntities(source), resolvedSeparator, Entity::getDisplayName);
    }

    @Override
    public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
        return output.accept(currentStyle, this.selector.source());
    }

    @Override
    public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
        return output.accept(this.selector.source());
    }

    @Override
    public String toString() {
        return "pattern{" + String.valueOf(this.selector) + "}";
    }
}


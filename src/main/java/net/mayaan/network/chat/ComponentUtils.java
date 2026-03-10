/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.DataFixUtils
 *  java.lang.MatchException
 *  javax.annotation.CheckReturnValue
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.chat;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.CheckReturnValue;
import net.mayaan.ChatFormatting;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.locale.Language;
import net.mayaan.network.chat.ClickEvent;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentContents;
import net.mayaan.network.chat.HoverEvent;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.network.chat.Style;
import net.mayaan.network.chat.contents.TranslatableContents;
import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class ComponentUtils {
    public static final String DEFAULT_SEPARATOR_TEXT = ", ";
    public static final Component DEFAULT_SEPARATOR = Component.literal(", ").withStyle(ChatFormatting.GRAY);
    public static final Component DEFAULT_NO_STYLE_SEPARATOR = Component.literal(", ");

    @CheckReturnValue
    public static MutableComponent mergeStyles(MutableComponent component, Style style) {
        if (style.isEmpty()) {
            return component;
        }
        Style inner = component.getStyle();
        if (inner.isEmpty()) {
            return component.setStyle(style);
        }
        if (inner.equals(style)) {
            return component;
        }
        return component.setStyle(inner.applyTo(style));
    }

    @CheckReturnValue
    public static Component mergeStyles(Component component, Style style) {
        if (style.isEmpty()) {
            return component;
        }
        Style inner = component.getStyle();
        if (inner.isEmpty()) {
            return component.copy().setStyle(style);
        }
        if (inner.equals(style)) {
            return component;
        }
        return component.copy().setStyle(inner.applyTo(style));
    }

    public static Optional<MutableComponent> updateForEntity(@Nullable CommandSourceStack source, Optional<Component> component, @Nullable Entity entity, int recursionDepth) throws CommandSyntaxException {
        return component.isPresent() ? Optional.of(ComponentUtils.updateForEntity(source, component.get(), entity, recursionDepth)) : Optional.empty();
    }

    public static MutableComponent updateForEntity(@Nullable CommandSourceStack source, Component component, @Nullable Entity entity, int recursionDepth) throws CommandSyntaxException {
        if (recursionDepth > 100) {
            return component.copy();
        }
        MutableComponent result = component.getContents().resolve(source, entity, recursionDepth + 1);
        for (Component sibling : component.getSiblings()) {
            result.append(ComponentUtils.updateForEntity(source, sibling, entity, recursionDepth + 1));
        }
        return result.withStyle(ComponentUtils.resolveStyle(source, component.getStyle(), entity, recursionDepth));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static Style resolveStyle(@Nullable CommandSourceStack source, Style style, @Nullable Entity entity, int recursionDepth) throws CommandSyntaxException {
        HoverEvent hoverEvent = style.getHoverEvent();
        if (!(hoverEvent instanceof HoverEvent.ShowText)) return style;
        HoverEvent.ShowText showText = (HoverEvent.ShowText)hoverEvent;
        try {
            Component component;
            Component text = component = showText.value();
            HoverEvent.ShowText resolved = new HoverEvent.ShowText(ComponentUtils.updateForEntity(source, text, entity, recursionDepth + 1));
            return style.withHoverEvent(resolved);
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
    }

    public static Component formatList(Collection<String> values) {
        return ComponentUtils.formatAndSortList(values, v -> Component.literal(v).withStyle(ChatFormatting.GREEN));
    }

    public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> values, Function<T, Component> formatter) {
        if (values.isEmpty()) {
            return CommonComponents.EMPTY;
        }
        if (values.size() == 1) {
            return formatter.apply((Comparable)values.iterator().next());
        }
        ArrayList sorted = Lists.newArrayList(values);
        sorted.sort(Comparable::compareTo);
        return ComponentUtils.formatList(sorted, formatter);
    }

    public static <T> Component formatList(Collection<? extends T> values, Function<T, Component> formatter) {
        return ComponentUtils.formatList(values, DEFAULT_SEPARATOR, formatter);
    }

    public static <T> MutableComponent formatList(Collection<? extends T> values, Optional<? extends Component> separator, Function<T, Component> formatter) {
        return ComponentUtils.formatList(values, (Component)DataFixUtils.orElse(separator, (Object)DEFAULT_SEPARATOR), formatter);
    }

    public static Component formatList(Collection<? extends Component> values, Component separator) {
        return ComponentUtils.formatList(values, separator, Function.identity());
    }

    public static <T> MutableComponent formatList(Collection<? extends T> values, Component separator, Function<T, Component> formatter) {
        if (values.isEmpty()) {
            return Component.empty();
        }
        if (values.size() == 1) {
            return formatter.apply(values.iterator().next()).copy();
        }
        MutableComponent result = Component.empty();
        boolean first = true;
        for (T value : values) {
            if (!first) {
                result.append(separator);
            }
            result.append(formatter.apply(value));
            first = false;
        }
        return result;
    }

    public static MutableComponent wrapInSquareBrackets(Component inner) {
        return Component.translatable("chat.square_brackets", inner);
    }

    public static Component fromMessage(Message message) {
        if (message instanceof Component) {
            Component component = (Component)message;
            return component;
        }
        return Component.literal(message.getString());
    }

    public static boolean isTranslationResolvable(@Nullable Component component) {
        ComponentContents componentContents;
        if (component != null && (componentContents = component.getContents()) instanceof TranslatableContents) {
            TranslatableContents translatable = (TranslatableContents)componentContents;
            String key = translatable.getKey();
            String fallback = translatable.getFallback();
            return fallback != null || Language.getInstance().has(key);
        }
        return true;
    }

    public static MutableComponent copyOnClickText(String text) {
        return ComponentUtils.wrapInSquareBrackets(Component.literal(text).withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent.CopyToClipboard(text)).withHoverEvent(new HoverEvent.ShowText(Component.translatable("chat.copy.click"))).withInsertion(text)));
    }
}


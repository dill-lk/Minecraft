/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.client.gui.components;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mayaan.ChatFormatting;
import net.mayaan.client.ComponentCollector;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.Font;
import net.mayaan.locale.Language;
import net.mayaan.network.chat.FormattedText;
import net.mayaan.network.chat.Style;
import net.mayaan.util.FormattedCharSequence;

public class ComponentRenderUtils {
    private static final FormattedCharSequence INDENT = FormattedCharSequence.codepoint(32, Style.EMPTY);

    private static String stripColor(String input) {
        return Mayaan.getInstance().options.chatColors().get() != false ? input : ChatFormatting.stripFormatting(input);
    }

    public static List<FormattedCharSequence> wrapComponents(FormattedText message, int maxWidth, Font font) {
        ComponentCollector collector = new ComponentCollector();
        message.visit((style, contents) -> {
            collector.append(FormattedText.of(ComponentRenderUtils.stripColor(contents), style));
            return Optional.empty();
        }, Style.EMPTY);
        ArrayList result = Lists.newArrayList();
        font.getSplitter().splitLines(collector.getResultOrEmpty(), maxWidth, Style.EMPTY, (text, wrapped) -> {
            FormattedCharSequence reorderedText = Language.getInstance().getVisualOrder((FormattedText)text);
            result.add(wrapped != false ? FormattedCharSequence.composite(INDENT, reorderedText) : reorderedText);
        });
        if (result.isEmpty()) {
            return Lists.newArrayList((Object[])new FormattedCharSequence[]{FormattedCharSequence.EMPTY});
        }
        return result;
    }
}


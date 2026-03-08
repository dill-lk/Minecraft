/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.multiplayer.chat;

import java.util.List;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.components.ComponentRenderUtils;
import net.mayaan.client.multiplayer.chat.GuiMessageSource;
import net.mayaan.client.multiplayer.chat.GuiMessageTag;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MessageSignature;
import net.mayaan.util.FormattedCharSequence;
import org.jspecify.annotations.Nullable;

public record GuiMessage(int addedTime, Component content, @Nullable MessageSignature signature, GuiMessageSource source, @Nullable GuiMessageTag tag) {
    private static final int MESSAGE_TAG_MARGIN_LEFT = 4;

    public List<FormattedCharSequence> splitLines(Font font, int maxWidth) {
        if (this.tag != null && this.tag.icon() != null) {
            maxWidth -= this.tag.icon().width + 4 + 2;
        }
        return ComponentRenderUtils.wrapComponents(this.content, maxWidth, font);
    }

    public record Line(GuiMessage parent, FormattedCharSequence content, boolean endOfEntry) {
        public int getTagIconLeft(Font font) {
            return font.width(this.content) + 4;
        }

        public @Nullable GuiMessageTag tag() {
            return this.parent.tag;
        }

        public int addedTime() {
            return this.parent.addedTime;
        }
    }
}


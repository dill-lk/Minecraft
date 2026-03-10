/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 */
package net.mayaan.server.packs;

import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Optional;
import net.mayaan.ChatFormatting;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentUtils;
import net.mayaan.network.chat.HoverEvent;
import net.mayaan.server.packs.repository.KnownPack;
import net.mayaan.server.packs.repository.PackSource;

public record PackLocationInfo(String id, Component title, PackSource source, Optional<KnownPack> knownPackInfo) {
    public Component createChatLink(boolean enabled, Component description) {
        return ComponentUtils.wrapInSquareBrackets(this.source.decorate(Component.literal(this.id))).withStyle(s -> s.withColor(enabled ? ChatFormatting.GREEN : ChatFormatting.RED).withInsertion(StringArgumentType.escapeIfRequired((String)this.id)).withHoverEvent(new HoverEvent.ShowText(Component.empty().append(this.title).append("\n").append(description))));
    }
}


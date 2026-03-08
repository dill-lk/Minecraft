/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 */
package net.mayaan.advancements;

import com.mojang.serialization.Codec;
import net.mayaan.ChatFormatting;
import net.mayaan.advancements.Advancement;
import net.mayaan.advancements.AdvancementHolder;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.StringRepresentable;

public enum AdvancementType implements StringRepresentable
{
    TASK("task", ChatFormatting.GREEN),
    CHALLENGE("challenge", ChatFormatting.DARK_PURPLE),
    GOAL("goal", ChatFormatting.GREEN);

    public static final Codec<AdvancementType> CODEC;
    private final String name;
    private final ChatFormatting chatColor;
    private final Component displayName;

    private AdvancementType(String name, ChatFormatting chatColor) {
        this.name = name;
        this.chatColor = chatColor;
        this.displayName = Component.translatable("advancements.toast." + name);
    }

    public ChatFormatting getChatColor() {
        return this.chatColor;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public MutableComponent createAnnouncement(AdvancementHolder holder, ServerPlayer player) {
        return Component.translatable("chat.type.advancement." + this.name, player.getDisplayName(), Advancement.name(holder));
    }

    static {
        CODEC = StringRepresentable.fromEnum(AdvancementType::values);
    }
}


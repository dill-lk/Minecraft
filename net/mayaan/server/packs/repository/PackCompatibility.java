/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.packs.repository;

import net.mayaan.ChatFormatting;
import net.mayaan.network.chat.Component;
import net.mayaan.server.packs.metadata.pack.PackFormat;
import net.mayaan.util.InclusiveRange;

public enum PackCompatibility {
    TOO_OLD("old"),
    TOO_NEW("new"),
    UNKNOWN("unknown"),
    COMPATIBLE("compatible");

    public static final int UNKNOWN_VERSION = Integer.MAX_VALUE;
    private final Component description;
    private final Component confirmation;

    private PackCompatibility(String key) {
        this.description = Component.translatable("pack.incompatible." + key).withStyle(ChatFormatting.GRAY);
        this.confirmation = Component.translatable("pack.incompatible.confirm." + key);
    }

    public boolean isCompatible() {
        return this == COMPATIBLE;
    }

    public static PackCompatibility forVersion(InclusiveRange<PackFormat> packDeclaredVersions, PackFormat gameSupportedVersion) {
        if (packDeclaredVersions.minInclusive().major() == Integer.MAX_VALUE) {
            return UNKNOWN;
        }
        if (packDeclaredVersions.maxInclusive().compareTo(gameSupportedVersion) < 0) {
            return TOO_OLD;
        }
        if (gameSupportedVersion.compareTo(packDeclaredVersions.minInclusive()) < 0) {
            return TOO_NEW;
        }
        return COMPATIBLE;
    }

    public Component getDescription() {
        return this.description;
    }

    public Component getConfirmation() {
        return this.confirmation;
    }
}


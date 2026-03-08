/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.packs;

import net.mayaan.server.packs.repository.Pack;

public record PackSelectionConfig(boolean required, Pack.Position defaultPosition, boolean fixedPosition) {
}


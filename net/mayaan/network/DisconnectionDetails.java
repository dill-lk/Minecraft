/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network;

import java.net.URI;
import java.nio.file.Path;
import java.util.Optional;
import net.mayaan.network.chat.Component;

public record DisconnectionDetails(Component reason, Optional<Path> report, Optional<URI> bugReportLink) {
    public DisconnectionDetails(Component reason) {
        this(reason, Optional.empty(), Optional.empty());
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.validation;

import java.nio.file.Path;

public record ForbiddenSymlinkInfo(Path link, Path target) {
}


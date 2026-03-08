/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.input;

import net.mayaan.util.StringUtil;

public record CharacterEvent(int codepoint) {
    public String codepointAsString() {
        return Character.toString(this.codepoint);
    }

    public boolean isAllowedChatCharacter() {
        return StringUtil.isAllowedChatCharacter(this.codepoint);
    }
}


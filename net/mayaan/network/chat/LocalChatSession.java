/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.chat;

import java.util.UUID;
import net.mayaan.network.chat.RemoteChatSession;
import net.mayaan.network.chat.SignedMessageChain;
import net.mayaan.util.Signer;
import net.mayaan.world.entity.player.ProfileKeyPair;

public record LocalChatSession(UUID sessionId, ProfileKeyPair keyPair) {
    public static LocalChatSession create(ProfileKeyPair keyPair) {
        return new LocalChatSession(UUID.randomUUID(), keyPair);
    }

    public SignedMessageChain.Encoder createMessageEncoder(UUID profileId) {
        return new SignedMessageChain(profileId, this.sessionId).encoder(Signer.from(this.keyPair.privateKey(), "SHA256withRSA"));
    }

    public RemoteChatSession asRemote() {
        return new RemoteChatSession(this.sessionId, this.keyPair.publicKey());
    }
}


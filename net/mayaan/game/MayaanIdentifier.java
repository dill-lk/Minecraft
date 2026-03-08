package net.mayaan.game;

import net.mayaan.resources.Identifier;

/**
 * Helper for creating Mayaan-namespaced resource identifiers.
 * All Mayaan-specific content uses the "mayaan" namespace.
 */
public final class MayaanIdentifier {
    public static final String NAMESPACE = "mayaan";

    private MayaanIdentifier() {}

    /**
     * Creates an identifier in the "mayaan" namespace.
     *
     * @param path the resource path
     * @return an Identifier with namespace "mayaan"
     */
    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, path);
    }
}

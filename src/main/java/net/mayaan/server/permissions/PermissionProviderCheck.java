/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.permissions;

import java.util.function.Predicate;
import net.mayaan.server.permissions.PermissionCheck;
import net.mayaan.server.permissions.PermissionSetSupplier;

public record PermissionProviderCheck<T extends PermissionSetSupplier>(PermissionCheck test) implements Predicate<T>
{
    @Override
    public boolean test(T t) {
        return this.test.check(t.permissions());
    }
}


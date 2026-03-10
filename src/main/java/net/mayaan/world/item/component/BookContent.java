/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.component;

import java.util.List;
import net.mayaan.server.network.Filterable;

public interface BookContent<T, C> {
    public List<Filterable<T>> pages();

    public C withReplacedPages(List<Filterable<T>> var1);
}


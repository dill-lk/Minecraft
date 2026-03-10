/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.crafting.display;

import java.util.List;
import net.mayaan.core.Holder;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;

public interface DisplayContentsFactory<T> {

    public static interface ForRemainders<T>
    extends DisplayContentsFactory<T> {
        public T addRemainder(T var1, List<T> var2);
    }

    public static interface ForStacks<T>
    extends DisplayContentsFactory<T> {
        default public T forStack(Holder<Item> item) {
            return this.forStack(new ItemStack(item));
        }

        default public T forStack(Item item) {
            return this.forStack(new ItemStack(item));
        }

        public T forStack(ItemStack var1);
    }
}


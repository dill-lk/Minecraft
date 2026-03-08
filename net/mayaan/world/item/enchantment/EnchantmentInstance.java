/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item.enchantment;

import net.mayaan.core.Holder;
import net.mayaan.world.item.enchantment.Enchantment;

public record EnchantmentInstance(Holder<Enchantment> enchantment, int level) {
    public int weight() {
        return this.enchantment().value().getWeight();
    }
}


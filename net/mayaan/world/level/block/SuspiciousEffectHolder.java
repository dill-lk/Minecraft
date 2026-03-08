/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.world.flag.FeatureElement;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.component.SuspiciousStewEffects;
import net.mayaan.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public interface SuspiciousEffectHolder {
    public SuspiciousStewEffects getSuspiciousEffects();

    public static List<SuspiciousEffectHolder> getAllEffectHolders() {
        return BuiltInRegistries.ITEM.stream().map(SuspiciousEffectHolder::tryGet).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static @Nullable SuspiciousEffectHolder tryGet(ItemLike item) {
        BlockItem blockItem;
        FeatureElement featureElement = item.asItem();
        if (featureElement instanceof BlockItem && (featureElement = (blockItem = (BlockItem)featureElement).getBlock()) instanceof SuspiciousEffectHolder) {
            SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)((Object)featureElement);
            return effectHolder;
        }
        Item item2 = item.asItem();
        if (item2 instanceof SuspiciousEffectHolder) {
            SuspiciousEffectHolder effectHolder = (SuspiciousEffectHolder)((Object)item2);
            return effectHolder;
        }
        return null;
    }
}


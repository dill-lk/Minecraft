/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import net.mayaan.core.component.DataComponents;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.animal.sheep.Sheep;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.DyeColor;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.SignApplicator;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.entity.SignBlockEntity;

public class DyeItem
extends Item
implements SignApplicator {
    public DyeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand type) {
        DyeColor dyeColor;
        Sheep sheep;
        if (target instanceof Sheep && (sheep = (Sheep)target).isAlive() && !sheep.isSheared() && (dyeColor = itemStack.get(DataComponents.DYE)) != null && sheep.getColor() != dyeColor) {
            sheep.level().playSound((Entity)player, sheep, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
            if (!player.level().isClientSide()) {
                sheep.setColor(dyeColor);
                itemStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean tryApplyToSign(Level level, SignBlockEntity sign, boolean isFrontText, ItemStack item, Player player) {
        DyeColor dye = item.get(DataComponents.DYE);
        if (dye != null && sign.updateText(text -> text.setColor(dye), isFrontText)) {
            level.playSound(null, sign.getBlockPos(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0f, 1.0f);
            return true;
        }
        return false;
    }
}


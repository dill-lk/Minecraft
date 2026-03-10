/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.item;

import java.util.Optional;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.decoration.GlowItemFrame;
import net.mayaan.world.entity.decoration.HangingEntity;
import net.mayaan.world.entity.decoration.ItemFrame;
import net.mayaan.world.entity.decoration.painting.Painting;
import net.mayaan.world.entity.decoration.painting.PaintingVariant;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.TooltipFlag;
import net.mayaan.world.item.component.TooltipDisplay;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.gameevent.GameEvent;

public class HangingEntityItem
extends Item {
    private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);
    private final EntityType<? extends HangingEntity> type;

    public HangingEntityItem(EntityType<? extends HangingEntity> type, Item.Properties properties) {
        super(properties);
        this.type = type;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        HangingEntity entity;
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockPos blockPos = pos.relative(clickedFace);
        Player player = context.getPlayer();
        ItemStack itemInHand = context.getItemInHand();
        if (player != null && !this.mayPlace(player, clickedFace, itemInHand, blockPos)) {
            return InteractionResult.FAIL;
        }
        Level level = context.getLevel();
        if (this.type == EntityType.PAINTING) {
            Optional<Painting> painting = Painting.create(level, blockPos, clickedFace);
            if (painting.isEmpty()) {
                return InteractionResult.CONSUME;
            }
            entity = painting.get();
        } else if (this.type == EntityType.ITEM_FRAME) {
            entity = new ItemFrame(level, blockPos, clickedFace);
        } else if (this.type == EntityType.GLOW_ITEM_FRAME) {
            entity = new GlowItemFrame(level, blockPos, clickedFace);
        } else {
            return InteractionResult.SUCCESS;
        }
        EntityType.createDefaultStackConfig(level, itemInHand, player).accept(entity);
        if (entity.survives()) {
            if (!level.isClientSide()) {
                entity.playPlacementSound();
                level.gameEvent((Entity)player, GameEvent.ENTITY_PLACE, entity.position());
                level.addFreshEntity(entity);
            }
            itemInHand.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    protected boolean mayPlace(Player player, Direction direction, ItemStack itemStack, BlockPos blockPos) {
        return !direction.getAxis().isVertical() && player.mayUseItemAt(blockPos, direction, itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (this.type == EntityType.PAINTING && display.shows(DataComponents.PAINTING_VARIANT)) {
            Holder<PaintingVariant> variant = itemStack.get(DataComponents.PAINTING_VARIANT);
            if (variant != null) {
                variant.value().title().ifPresent(builder);
                variant.value().author().ifPresent(builder);
                builder.accept(Component.translatable("painting.dimensions", variant.value().width(), variant.value().height()));
            } else if (tooltipFlag.isCreative()) {
                builder.accept(TOOLTIP_RANDOM_VARIANT);
            }
        }
    }
}


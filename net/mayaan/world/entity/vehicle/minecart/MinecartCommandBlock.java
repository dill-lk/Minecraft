/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.minecart;

import java.util.Objects;
import net.mayaan.commands.CommandSource;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BaseCommandBlock;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.Vec3;

public class MinecartCommandBlock
extends AbstractMinecart {
    private static final EntityDataAccessor<String> DATA_ID_COMMAND_NAME = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Component> DATA_ID_LAST_OUTPUT = SynchedEntityData.defineId(MinecartCommandBlock.class, EntityDataSerializers.COMPONENT);
    private final BaseCommandBlock commandBlock = new MinecartCommandBase(this);
    private static final int ACTIVATION_DELAY = 4;
    private int lastActivated;

    public MinecartCommandBlock(EntityType<? extends MinecartCommandBlock> type, Level level) {
        super(type, level);
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.COMMAND_BLOCK_MINECART);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_ID_COMMAND_NAME, "");
        entityData.define(DATA_ID_LAST_OUTPUT, CommonComponents.EMPTY);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.commandBlock.load(input);
        this.getEntityData().set(DATA_ID_COMMAND_NAME, this.getCommandBlock().getCommand());
        this.getEntityData().set(DATA_ID_LAST_OUTPUT, this.getCommandBlock().getLastOutput());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.commandBlock.save(output);
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.COMMAND_BLOCK.defaultBlockState();
    }

    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }

    @Override
    public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {
        if (state && this.tickCount - this.lastActivated >= 4) {
            this.getCommandBlock().performCommand(level);
            this.lastActivated = this.tickCount;
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        if (!player.canUseGameMasterBlocks()) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide()) {
            player.openMinecartCommandBlock(this);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_ID_LAST_OUTPUT.equals(accessor)) {
            try {
                this.commandBlock.setLastOutput(this.getEntityData().get(DATA_ID_LAST_OUTPUT));
            }
            catch (Throwable throwable) {}
        } else if (DATA_ID_COMMAND_NAME.equals(accessor)) {
            this.commandBlock.setCommand(this.getEntityData().get(DATA_ID_COMMAND_NAME));
        }
    }

    private class MinecartCommandBase
    extends BaseCommandBlock {
        final /* synthetic */ MinecartCommandBlock this$0;

        private MinecartCommandBase(MinecartCommandBlock minecartCommandBlock) {
            MinecartCommandBlock minecartCommandBlock2 = minecartCommandBlock;
            Objects.requireNonNull(minecartCommandBlock2);
            this.this$0 = minecartCommandBlock2;
        }

        @Override
        public void onUpdated(ServerLevel level) {
            this.this$0.getEntityData().set(DATA_ID_COMMAND_NAME, this.getCommand());
            this.this$0.getEntityData().set(DATA_ID_LAST_OUTPUT, this.getLastOutput());
        }

        @Override
        public CommandSourceStack createCommandSourceStack(ServerLevel level, CommandSource source) {
            return new CommandSourceStack(source, this.this$0.position(), this.this$0.getRotationVector(), level, LevelBasedPermissionSet.GAMEMASTER, this.getName().getString(), this.this$0.getDisplayName(), level.getServer(), this.this$0);
        }

        @Override
        public boolean isValid() {
            return !this.this$0.isRemoved();
        }
    }
}


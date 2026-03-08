/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.vehicle.minecart;

import java.util.Objects;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.BaseSpawner;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

public class MinecartSpawner
extends AbstractMinecart {
    private final BaseSpawner spawner = new BaseSpawner(this){
        final /* synthetic */ MinecartSpawner this$0;
        {
            MinecartSpawner minecartSpawner = this$0;
            Objects.requireNonNull(minecartSpawner);
            this.this$0 = minecartSpawner;
        }

        @Override
        public void broadcastEvent(Level level, BlockPos pos, int id) {
            level.broadcastEntityEvent(this.this$0, (byte)id);
        }
    };
    private final Runnable ticker;

    public MinecartSpawner(EntityType<? extends MinecartSpawner> type, Level level) {
        super(type, level);
        this.ticker = this.createTicker(level);
    }

    @Override
    protected Item getDropItem() {
        return Items.MINECART;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.MINECART);
    }

    private Runnable createTicker(Level level) {
        return level instanceof ServerLevel ? () -> this.spawner.serverTick((ServerLevel)level, this.blockPosition()) : () -> this.spawner.clientTick(level, this.blockPosition());
    }

    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.SPAWNER.defaultBlockState();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.spawner.load(this.level(), this.blockPosition(), input);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.spawner.save(output);
    }

    @Override
    public void handleEntityEvent(byte id) {
        this.spawner.onEventTriggered(this.level(), id);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticker.run();
    }

    public BaseSpawner getSpawner() {
        return this.spawner;
    }
}


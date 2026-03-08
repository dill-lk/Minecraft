/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  io.netty.buffer.ByteBuf
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.fish;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import java.util.function.IntFunction;
import net.mayaan.core.component.DataComponentGetter;
import net.mayaan.core.component.DataComponentType;
import net.mayaan.core.component.DataComponents;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.ByIdMap;
import net.mayaan.util.StringRepresentable;
import net.mayaan.util.random.WeightedList;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.animal.Bucketable;
import net.mayaan.world.entity.animal.fish.AbstractSchoolingFish;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public class Salmon
extends AbstractSchoolingFish {
    private static final String TAG_TYPE = "type";
    private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);

    public Salmon(EntityType<? extends Salmon> type, Level level) {
        super((EntityType<? extends AbstractSchoolingFish>)type, level);
        this.refreshDimensions();
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.SALMON_BUCKET);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_TYPE, Variant.DEFAULT.id());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_TYPE.equals(accessor)) {
            this.refreshDimensions();
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store(TAG_TYPE, Variant.CODEC, this.getVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setVariant(input.read(TAG_TYPE, Variant.CODEC).orElse(Variant.DEFAULT));
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        bucket.copyFrom(DataComponents.SALMON_SIZE, this);
    }

    private void setVariant(Variant variant) {
        this.entityData.set(DATA_TYPE, variant.id);
    }

    public Variant getVariant() {
        return Variant.BY_ID.apply(this.entityData.get(DATA_TYPE));
    }

    @Override
    public <T> @Nullable T get(DataComponentType<? extends T> type) {
        if (type == DataComponents.SALMON_SIZE) {
            return Salmon.castComponentValue(type, this.getVariant());
        }
        return super.get(type);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponents.SALMON_SIZE);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
        if (type == DataComponents.SALMON_SIZE) {
            this.setVariant(Salmon.castComponentValue(DataComponents.SALMON_SIZE, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        WeightedList.Builder<Variant> builder = WeightedList.builder();
        builder.add(Variant.SMALL, 30);
        builder.add(Variant.MEDIUM, 50);
        builder.add(Variant.LARGE, 15);
        builder.build().getRandom(this.random).ifPresent(this::setVariant);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    public float getSalmonScale() {
        return this.getVariant().boundingBoxScale;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return super.getDefaultDimensions(pose).scale(this.getSalmonScale());
    }

    public static enum Variant implements StringRepresentable
    {
        SMALL("small", 0, 0.5f),
        MEDIUM("medium", 1, 1.0f),
        LARGE("large", 2, 1.5f);

        public static final Variant DEFAULT;
        public static final Codec<Variant> CODEC;
        private static final IntFunction<Variant> BY_ID;
        public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
        private final String name;
        private final int id;
        private final float boundingBoxScale;

        private Variant(String name, int id, float boundingBoxScale) {
            this.name = name;
            this.id = id;
            this.boundingBoxScale = boundingBoxScale;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        private int id() {
            return this.id;
        }

        static {
            DEFAULT = MEDIUM;
            CODEC = StringRepresentable.fromEnum(Variant::values);
            BY_ID = ByIdMap.continuous(Variant::id, Variant.values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
            STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
        }
    }
}


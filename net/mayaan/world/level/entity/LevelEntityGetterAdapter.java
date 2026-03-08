/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.entity;

import java.util.UUID;
import java.util.function.Consumer;
import net.mayaan.util.AbortableIterationConsumer;
import net.mayaan.world.level.entity.EntityAccess;
import net.mayaan.world.level.entity.EntityLookup;
import net.mayaan.world.level.entity.EntitySectionStorage;
import net.mayaan.world.level.entity.EntityTypeTest;
import net.mayaan.world.level.entity.LevelEntityGetter;
import net.mayaan.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class LevelEntityGetterAdapter<T extends EntityAccess>
implements LevelEntityGetter<T> {
    private final EntityLookup<T> visibleEntities;
    private final EntitySectionStorage<T> sectionStorage;

    public LevelEntityGetterAdapter(EntityLookup<T> visibleEntities, EntitySectionStorage<T> sectionStorage) {
        this.visibleEntities = visibleEntities;
        this.sectionStorage = sectionStorage;
    }

    @Override
    public @Nullable T get(int id) {
        return this.visibleEntities.getEntity(id);
    }

    @Override
    public @Nullable T get(UUID id) {
        return this.visibleEntities.getEntity(id);
    }

    @Override
    public Iterable<T> getAll() {
        return this.visibleEntities.getAllEntities();
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> type, AbortableIterationConsumer<U> consumer) {
        this.visibleEntities.getEntities(type, consumer);
    }

    @Override
    public void get(AABB bb, Consumer<T> output) {
        this.sectionStorage.getEntities(bb, AbortableIterationConsumer.forConsumer(output));
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> type, AABB bb, AbortableIterationConsumer<U> consumer) {
        this.sectionStorage.getEntities(type, bb, consumer);
    }
}


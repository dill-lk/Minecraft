/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.network.chat.contents.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.stream.Stream;
import net.mayaan.advancements.criterion.NbtPredicate;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.arguments.selector.EntitySelector;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.chat.contents.data.DataSource;
import net.mayaan.util.CompilableString;
import net.mayaan.world.entity.Entity;

public record EntityDataSource(CompilableString<EntitySelector> selector) implements DataSource
{
    public static final MapCodec<EntityDataSource> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)EntitySelector.COMPILABLE_CODEC.fieldOf("entity").forGetter(EntityDataSource::selector)).apply((Applicative)i, EntityDataSource::new));

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack sender) throws CommandSyntaxException {
        List<? extends Entity> entities = this.selector.compiled().findEntities(sender);
        return entities.stream().map(NbtPredicate::getEntityTagToCompare);
    }

    public MapCodec<EntityDataSource> codec() {
        return MAP_CODEC;
    }
}


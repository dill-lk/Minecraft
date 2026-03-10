/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.CompoundTagArgument;
import net.mayaan.commands.arguments.ResourceArgument;
import net.mayaan.commands.arguments.coordinates.Vec3Argument;
import net.mayaan.commands.synchronization.SuggestionProviders;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.Registries;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.Difficulty;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.level.Level;
import net.mayaan.world.phys.Vec3;

public class SummonCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed"));
    private static final SimpleCommandExceptionType ERROR_FAILED_PEACEFUL = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.peaceful"));
    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)Component.translatable("commands.summon.invalidPosition"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(((RequiredArgumentBuilder)Commands.argument("entity", ResourceArgument.resource(context, Registries.ENTITY_TYPE)).suggests(SuggestionProviders.cast(SuggestionProviders.SUMMONABLE_ENTITIES)).executes(c -> SummonCommand.spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType((CommandContext<CommandSourceStack>)c, "entity"), ((CommandSourceStack)c.getSource()).getPosition(), new CompoundTag(), true))).then(((RequiredArgumentBuilder)Commands.argument("pos", Vec3Argument.vec3()).executes(c -> SummonCommand.spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType((CommandContext<CommandSourceStack>)c, "entity"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)c, "pos"), new CompoundTag(), true))).then(Commands.argument("nbt", CompoundTagArgument.compoundTag()).executes(c -> SummonCommand.spawnEntity((CommandSourceStack)c.getSource(), ResourceArgument.getSummonableEntityType((CommandContext<CommandSourceStack>)c, "entity"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)c, "pos"), CompoundTagArgument.getCompoundTag(c, "nbt"), false))))));
    }

    public static Entity createEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
        BlockPos blockPos = BlockPos.containing(pos);
        if (!Level.isInSpawnableBounds(blockPos)) {
            throw INVALID_POSITION.create();
        }
        if (source.getLevel().getDifficulty() == Difficulty.PEACEFUL && !type.value().isAllowedInPeaceful()) {
            throw ERROR_FAILED_PEACEFUL.create();
        }
        CompoundTag entityTag = nbt.copy();
        entityTag.putString("id", type.key().identifier().toString());
        ServerLevel level = source.getLevel();
        Entity entity = EntityType.loadEntityRecursive(entityTag, (Level)level, EntitySpawnReason.COMMAND, e -> {
            e.snapTo(pos.x, pos.y, pos.z, e.getYRot(), e.getXRot());
            return e;
        });
        if (entity == null) {
            throw ERROR_FAILED.create();
        }
        if (finalize && entity instanceof Mob) {
            Mob mob = (Mob)entity;
            mob.finalizeSpawn(source.getLevel(), source.getLevel().getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.COMMAND, null);
        }
        if (!level.tryAddFreshEntityWithPassengers(entity)) {
            throw ERROR_DUPLICATE_UUID.create();
        }
        return entity;
    }

    private static int spawnEntity(CommandSourceStack source, Holder.Reference<EntityType<?>> type, Vec3 pos, CompoundTag nbt, boolean finalize) throws CommandSyntaxException {
        Entity entity = SummonCommand.createEntity(source, type, pos, nbt, finalize);
        source.sendSuccess(() -> Component.translatable("commands.summon.success", entity.getDisplayName()), true);
        return 1;
    }
}


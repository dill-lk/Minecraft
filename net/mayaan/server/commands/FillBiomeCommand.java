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
 *  com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  com.mojang.datafixers.util.Either
 *  org.apache.commons.lang3.mutable.MutableInt
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.ResourceArgument;
import net.mayaan.commands.arguments.ResourceOrTagArgument;
import net.mayaan.commands.arguments.coordinates.BlockPosArgument;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.core.QuartPos;
import net.mayaan.core.SectionPos;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.biome.BiomeResolver;
import net.mayaan.world.level.chunk.ChunkAccess;
import net.mayaan.world.level.chunk.status.ChunkStatus;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.levelgen.structure.BoundingBox;
import org.apache.commons.lang3.mutable.MutableInt;

public class FillBiomeCommand {
    public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType((Message)Component.translatable("argument.pos.unloaded"));
    private static final Dynamic2CommandExceptionType ERROR_VOLUME_TOO_LARGE = new Dynamic2CommandExceptionType((max, count) -> Component.translatableEscape("commands.fillbiome.toobig", max, count));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fillbiome").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("from", BlockPosArgument.blockPos()).then(Commands.argument("to", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)Commands.argument("biome", ResourceArgument.resource(context, Registries.BIOME)).executes(c -> FillBiomeCommand.fill((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)c, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)c, "to"), ResourceArgument.getResource((CommandContext<CommandSourceStack>)c, "biome", Registries.BIOME), b -> true))).then(Commands.literal("replace").then(Commands.argument("filter", ResourceOrTagArgument.resourceOrTag(context, Registries.BIOME)).executes(c -> FillBiomeCommand.fill((CommandSourceStack)c.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)c, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)c, "to"), ResourceArgument.getResource((CommandContext<CommandSourceStack>)c, "biome", Registries.BIOME), ResourceOrTagArgument.getResourceOrTag((CommandContext<CommandSourceStack>)c, "filter", Registries.BIOME)))))))));
    }

    private static int quantize(int blockCoord) {
        return QuartPos.toBlock(QuartPos.fromBlock(blockCoord));
    }

    private static BlockPos quantize(BlockPos block) {
        return new BlockPos(FillBiomeCommand.quantize(block.getX()), FillBiomeCommand.quantize(block.getY()), FillBiomeCommand.quantize(block.getZ()));
    }

    private static BiomeResolver makeResolver(MutableInt count, ChunkAccess chunk, BoundingBox region, Holder<Biome> toFill, Predicate<Holder<Biome>> filter) {
        return (quartX, quartY, quartZ, sampler) -> {
            int blockX = QuartPos.toBlock(quartX);
            int blockY = QuartPos.toBlock(quartY);
            int blockZ = QuartPos.toBlock(quartZ);
            Holder<Biome> currentBiome = chunk.getNoiseBiome(quartX, quartY, quartZ);
            if (region.isInside(blockX, blockY, blockZ) && filter.test(currentBiome)) {
                count.increment();
                return toFill;
            }
            return currentBiome;
        };
    }

    public static Either<Integer, CommandSyntaxException> fill(ServerLevel level, BlockPos rawFrom, BlockPos rawTo, Holder<Biome> biome) {
        return FillBiomeCommand.fill(level, rawFrom, rawTo, biome, b -> true, m -> {});
    }

    public static Either<Integer, CommandSyntaxException> fill(ServerLevel level, BlockPos rawFrom, BlockPos rawTo, Holder<Biome> biome, Predicate<Holder<Biome>> filter, Consumer<Supplier<Component>> successMessageConsumer) {
        int limit;
        BlockPos to;
        BlockPos from = FillBiomeCommand.quantize(rawFrom);
        BoundingBox region = BoundingBox.fromCorners(from, to = FillBiomeCommand.quantize(rawTo));
        int volume = region.getXSpan() * region.getYSpan() * region.getZSpan();
        if (volume > (limit = level.getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS).intValue())) {
            return Either.right((Object)((Object)ERROR_VOLUME_TOO_LARGE.create((Object)limit, (Object)volume)));
        }
        ArrayList<ChunkAccess> chunks = new ArrayList<ChunkAccess>();
        for (int chunkZ = SectionPos.blockToSectionCoord(region.minZ()); chunkZ <= SectionPos.blockToSectionCoord(region.maxZ()); ++chunkZ) {
            for (int chunkX = SectionPos.blockToSectionCoord(region.minX()); chunkX <= SectionPos.blockToSectionCoord(region.maxX()); ++chunkX) {
                ChunkAccess chunk = level.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                if (chunk == null) {
                    return Either.right((Object)((Object)ERROR_NOT_LOADED.create()));
                }
                chunks.add(chunk);
            }
        }
        MutableInt changedCount = new MutableInt(0);
        for (ChunkAccess chunk : chunks) {
            chunk.fillBiomesFromNoise(FillBiomeCommand.makeResolver(changedCount, chunk, region, biome, filter), level.getChunkSource().randomState().sampler());
            chunk.markUnsaved();
        }
        level.getChunkSource().chunkMap.resendBiomesForChunks(chunks);
        successMessageConsumer.accept(() -> Component.translatable("commands.fillbiome.success.count", changedCount.intValue(), region.minX(), region.minY(), region.minZ(), region.maxX(), region.maxY(), region.maxZ()));
        return Either.left((Object)changedCount.intValue());
    }

    private static int fill(CommandSourceStack source, BlockPos rawFrom, BlockPos rawTo, Holder.Reference<Biome> biome, Predicate<Holder<Biome>> filter) throws CommandSyntaxException {
        Either<Integer, CommandSyntaxException> result = FillBiomeCommand.fill(source.getLevel(), rawFrom, rawTo, biome, filter, m -> source.sendSuccess((Supplier<Component>)m, true));
        Optional exception = result.right();
        if (exception.isPresent()) {
            throw (CommandSyntaxException)((Object)exception.get());
        }
        return (Integer)result.left().get();
    }
}


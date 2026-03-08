/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.network.chat.Component;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.packs.repository.PackRepository;
import net.mayaan.world.level.storage.WorldData;
import org.slf4j.Logger;

public class ReloadCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void reloadPacks(Collection<String> selectedPacks, CommandSourceStack source) {
        source.getServer().reloadResources(selectedPacks).exceptionally(throwable -> {
            LOGGER.warn("Failed to execute reload", throwable);
            source.sendFailure(Component.translatable("commands.reload.failure"));
            return null;
        });
    }

    private static Collection<String> discoverNewPacks(PackRepository packRepository, WorldData worldData, Collection<String> currentPacks) {
        packRepository.reload();
        ArrayList selected = Lists.newArrayList(currentPacks);
        List<String> disabled = worldData.getDataConfiguration().dataPacks().getDisabled();
        for (String pack : packRepository.getAvailableIds()) {
            if (disabled.contains(pack) || selected.contains(pack)) continue;
            selected.add(pack);
        }
        return selected;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reload").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).executes(s -> {
            CommandSourceStack source = (CommandSourceStack)s.getSource();
            MayaanServer server = source.getServer();
            PackRepository packRepository = server.getPackRepository();
            WorldData worldData = server.getWorldData();
            Collection<String> currentPacks = packRepository.getSelectedIds();
            Collection<String> newSelectedPacks = ReloadCommand.discoverNewPacks(packRepository, worldData, currentPacks);
            source.sendSuccess(() -> Component.translatable("commands.reload.success"), true);
            ReloadCommand.reloadPacks(newSelectedPacks, source);
            return 0;
        }));
    }
}


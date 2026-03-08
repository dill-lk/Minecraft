/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  com.mojang.logging.LogUtils
 *  org.apache.commons.io.FileUtils
 *  org.slf4j.Logger
 */
package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.Consumer;
import net.mayaan.SharedConstants;
import net.mayaan.SystemReport;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.network.chat.Component;
import net.mayaan.server.MayaanServer;
import net.mayaan.util.FileUtil;
import net.mayaan.util.FileZipper;
import net.mayaan.util.TimeUtil;
import net.mayaan.util.Util;
import net.mayaan.util.profiling.EmptyProfileResults;
import net.mayaan.util.profiling.ProfileResults;
import net.mayaan.util.profiling.metrics.storage.MetricsPersister;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class PerfCommand {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final SimpleCommandExceptionType ERROR_NOT_RUNNING = new SimpleCommandExceptionType((Message)Component.translatable("commands.perf.notRunning"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_RUNNING = new SimpleCommandExceptionType((Message)Component.translatable("commands.perf.alreadyRunning"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("perf").requires(Commands.hasPermission(Commands.LEVEL_OWNERS))).then(Commands.literal("start").executes(c -> PerfCommand.startProfilingDedicatedServer((CommandSourceStack)c.getSource())))).then(Commands.literal("stop").executes(c -> PerfCommand.stopProfilingDedicatedServer((CommandSourceStack)c.getSource()))));
    }

    private static int startProfilingDedicatedServer(CommandSourceStack source) throws CommandSyntaxException {
        MayaanServer server = source.getServer();
        if (server.isRecordingMetrics()) {
            throw ERROR_ALREADY_RUNNING.create();
        }
        Consumer<ProfileResults> onStopped = results -> PerfCommand.whenStopped(source, results);
        Consumer<Path> onReportFinished = profilingLogs -> PerfCommand.saveResults(source, profilingLogs, server);
        server.startRecordingMetrics(onStopped, onReportFinished);
        source.sendSuccess(() -> Component.translatable("commands.perf.started"), false);
        return 0;
    }

    private static int stopProfilingDedicatedServer(CommandSourceStack source) throws CommandSyntaxException {
        MayaanServer server = source.getServer();
        if (!server.isRecordingMetrics()) {
            throw ERROR_NOT_RUNNING.create();
        }
        server.finishRecordingMetrics();
        return 0;
    }

    private static void saveResults(CommandSourceStack source, Path report, MayaanServer server) {
        String zipFile;
        String profilingName = String.format(Locale.ROOT, "%s-%s-%s", Util.getFilenameFormattedDateTime(), server.getWorldData().getLevelName(), SharedConstants.getCurrentVersion().id());
        try {
            zipFile = FileUtil.findAvailableName(MetricsPersister.PROFILING_RESULTS_DIR, profilingName, ".zip");
        }
        catch (IOException e) {
            source.sendFailure(Component.translatable("commands.perf.reportFailed"));
            LOGGER.error("Failed to create report name", (Throwable)e);
            return;
        }
        try (FileZipper fileZipper = new FileZipper(MetricsPersister.PROFILING_RESULTS_DIR.resolve(zipFile));){
            fileZipper.add(Paths.get("system.txt", new String[0]), server.fillSystemReport(new SystemReport()).toLineSeparatedString());
            fileZipper.add(report);
        }
        try {
            FileUtils.forceDelete((File)report.toFile());
        }
        catch (IOException e) {
            LOGGER.warn("Failed to delete temporary profiling file {}", (Object)report, (Object)e);
        }
        source.sendSuccess(() -> Component.translatable("commands.perf.reportSaved", zipFile), false);
    }

    private static void whenStopped(CommandSourceStack source, ProfileResults results) {
        if (results == EmptyProfileResults.EMPTY) {
            return;
        }
        int ticks = results.getTickDuration();
        double durationInSeconds = (double)results.getNanoDuration() / (double)TimeUtil.NANOSECONDS_PER_SECOND;
        source.sendSuccess(() -> Component.translatable("commands.perf.stopped", String.format(Locale.ROOT, "%.2f", durationInSeconds), ticks, String.format(Locale.ROOT, "%.2f", (double)ticks / durationInSeconds)), false);
    }
}


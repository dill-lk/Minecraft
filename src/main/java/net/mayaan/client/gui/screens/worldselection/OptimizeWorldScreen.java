/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DataFixer
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.Dynamic
 *  it.unimi.dsi.fastutil.booleans.BooleanConsumer
 *  it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.client.gui.screens.worldselection;

import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import java.util.function.ToIntFunction;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.worldselection.WorldOpenFlows;
import net.mayaan.core.RegistryAccess;
import net.mayaan.nbt.NbtUtils;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.WorldStem;
import net.mayaan.server.packs.repository.PackRepository;
import net.mayaan.server.packs.repository.ServerPacksSource;
import net.mayaan.util.Mth;
import net.mayaan.util.Util;
import net.mayaan.util.datafix.DataFixTypes;
import net.mayaan.util.datafix.DataFixers;
import net.mayaan.util.worldupdate.WorldUpgrader;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.LevelStorageSource;
import net.mayaan.world.level.storage.WorldData;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class OptimizeWorldScreen
extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ToIntFunction<ResourceKey<Level>> DIMENSION_COLORS = (ToIntFunction)Util.make(new Reference2IntOpenHashMap(), map -> {
        map.put(Level.OVERWORLD, -13408734);
        map.put(Level.NETHER, -10075085);
        map.put(Level.END, -8943531);
        map.defaultReturnValue(-2236963);
    });
    private final BooleanConsumer callback;
    private final WorldUpgrader upgrader;

    public static @Nullable OptimizeWorldScreen create(Mayaan minecraft, BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSourceAccess, boolean eraseCache) {
        WorldOpenFlows worldOpenFlows = minecraft.createWorldOpenFlows();
        PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
        Dynamic<?> unfixedDataTag = levelSourceAccess.getUnfixedDataTagWithFallback();
        int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
        if (DataFixers.getFileFixer().requiresFileFixing(dataVersion)) {
            throw new IllegalStateException("Can't optimize world before file fixing; shouldn't be able to get here");
        }
        Dynamic<?> dataTag = DataFixTypes.LEVEL.updateToCurrentVersion(DataFixers.getDataFixer(), unfixedDataTag, dataVersion);
        WorldStem worldStem = worldOpenFlows.loadWorldStem(levelSourceAccess, dataTag, false, packRepository);
        try {
            WorldData worldData = worldStem.worldDataAndGenSettings().data();
            RegistryAccess.Frozen registryAccess = worldStem.registries().compositeAccess();
            levelSourceAccess.saveDataTag(worldData);
            OptimizeWorldScreen optimizeWorldScreen = new OptimizeWorldScreen(callback, dataFixer, levelSourceAccess, worldData, eraseCache, registryAccess);
            if (worldStem != null) {
                worldStem.close();
            }
            return optimizeWorldScreen;
        }
        catch (Throwable throwable) {
            try {
                if (worldStem != null) {
                    try {
                        worldStem.close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
            catch (Exception e) {
                LOGGER.warn("Failed to load datapacks, can't optimize world", (Throwable)e);
                return null;
            }
        }
    }

    private OptimizeWorldScreen(BooleanConsumer callback, DataFixer dataFixer, LevelStorageSource.LevelStorageAccess levelSource, WorldData worldData, boolean eraseCache, RegistryAccess registryAccess) {
        super(Component.translatable("optimizeWorld.title", worldData.getLevelSettings().levelName()));
        this.callback = callback;
        this.upgrader = new WorldUpgrader(levelSource, dataFixer, registryAccess, eraseCache, false);
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
            this.upgrader.cancel();
            this.callback.accept(false);
        }).bounds(this.width / 2 - 100, this.height / 4 + 150, 200, 20).build());
    }

    @Override
    public void tick() {
        if (this.upgrader.isFinished()) {
            this.callback.accept(true);
        }
    }

    @Override
    public void onClose() {
        this.callback.accept(false);
    }

    @Override
    public void removed() {
        this.upgrader.cancel();
        this.upgrader.close();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, -1);
        int x0 = this.width / 2 - 150;
        int x1 = this.width / 2 + 150;
        int y0 = this.height / 4 + 100;
        int y1 = y0 + 10;
        graphics.drawCenteredString(this.font, this.upgrader.getStatus(), this.width / 2, y0 - this.font.lineHeight - 2, -6250336);
        if (this.upgrader.getTotalChunks() > 0) {
            graphics.fill(x0 - 1, y0 - 1, x1 + 1, y1 + 1, -16777216);
            graphics.drawString(this.font, Component.translatable("optimizeWorld.info.converted", this.upgrader.getConverted()), x0, 40, -6250336);
            graphics.drawString(this.font, Component.translatable("optimizeWorld.info.skipped", this.upgrader.getSkipped()), x0, 40 + this.font.lineHeight + 3, -6250336);
            graphics.drawString(this.font, Component.translatable("optimizeWorld.info.total", this.upgrader.getTotalChunks()), x0, 40 + (this.font.lineHeight + 3) * 2, -6250336);
            int progress = 0;
            for (ResourceKey<Level> dimension : this.upgrader.levels()) {
                int length = Mth.floor(this.upgrader.dimensionProgress(dimension) * (float)(x1 - x0));
                graphics.fill(x0 + progress, y0, x0 + progress + length, y1, DIMENSION_COLORS.applyAsInt(dimension));
                progress += length;
            }
            int totalProgress = this.upgrader.getConverted() + this.upgrader.getSkipped();
            MutableComponent countStr = Component.translatable("optimizeWorld.progress.counter", totalProgress, this.upgrader.getTotalChunks());
            MutableComponent progressStr = Component.translatable("optimizeWorld.progress.percentage", Mth.floor(this.upgrader.getTotalProgress() * 100.0f));
            graphics.drawCenteredString(this.font, countStr, this.width / 2, y0 + 2 * this.font.lineHeight + 2, -6250336);
            graphics.drawCenteredString(this.font, progressStr, this.width / 2, y0 + (y1 - y0) / 2 - this.font.lineHeight / 2, -6250336);
        }
    }
}


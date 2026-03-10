/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.worldselection;

import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.util.Mth;
import net.mayaan.util.worldupdate.UpgradeProgress;

public class FileFixerProgressScreen
extends Screen {
    private static final int PROGRESS_BAR_WIDTH = 200;
    private static final int PROGRESS_BAR_HEIGHT = 2;
    private static final int LINE_SPACING = 3;
    private static final int SECTION_SPACING = 30;
    private static final Component SCANNING = Component.translatable("upgradeWorld.info.scanning");
    private final UpgradeProgress upgradeProgress;
    private Button cancelButton;

    public FileFixerProgressScreen(UpgradeProgress upgradeProgress) {
        super(Component.translatable("upgradeWorld.title"));
        this.upgradeProgress = upgradeProgress;
    }

    @Override
    protected void init() {
        super.init();
        this.cancelButton = Button.builder(CommonComponents.GUI_CANCEL, button -> {
            this.upgradeProgress.setCanceled();
            button.active = false;
        }).bounds((this.width - 200) / 2, this.height / 2 + 100, 200, 20).build();
        this.addRenderableWidget(this.cancelButton);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        int xCenter = this.width / 2;
        int yCenter = this.height / 2;
        int textTop = yCenter - 50;
        this.renderTitle(graphics, xCenter, textTop);
        int totalFiles = this.upgradeProgress.getTotalFileFixStats().totalOperations();
        if (totalFiles > 0) {
            this.renderProgress(graphics, xCenter, textTop);
        } else {
            this.renderScanning(graphics, xCenter, textTop);
        }
    }

    private void renderTitle(GuiGraphics graphics, int xCenter, int yTop) {
        graphics.drawCenteredString(this.font, this.title, xCenter, yTop, -1);
    }

    private void renderProgress(GuiGraphics graphics, int xCenter, int textTop) {
        UpgradeProgress.FileFixStats typeFileStats = this.upgradeProgress.getTypeFileFixStats();
        UpgradeProgress.FileFixStats totalFileStats = this.upgradeProgress.getTotalFileFixStats();
        UpgradeProgress.FileFixStats runningFileFixerStats = this.upgradeProgress.getRunningFileFixerStats();
        int y = textTop + this.font.lineHeight + 3;
        this.renderProgressBar(graphics, xCenter, y, runningFileFixerStats.getProgress());
        this.renderFileStats(graphics, xCenter, y += 7, totalFileStats.finishedOperations(), totalFileStats.totalOperations());
        this.renderFileFixerCount(graphics, xCenter, y += this.font.lineHeight * 2 + 6, runningFileFixerStats.finishedOperations(), runningFileFixerStats.totalOperations());
        this.renderTypeText(graphics, xCenter, y += this.font.lineHeight + 30 - 5);
        this.renderProgressBar(graphics, xCenter, y += this.font.lineHeight + 3, typeFileStats.getProgress());
        this.renderTypeProgress(graphics, xCenter, y += 7, typeFileStats.getProgress());
    }

    private void renderProgressBar(GuiGraphics graphics, int xCenter, int y, float progress) {
        int barLeft = xCenter - 100;
        int barRight = barLeft + 200;
        int barBottom = y + 2;
        graphics.fill(barLeft, y, barRight, barBottom, -16777216);
        graphics.fill(barLeft, y, barLeft + Math.round(progress * 200.0f), barBottom, -16711936);
    }

    private void renderTypeText(GuiGraphics graphics, int xCenter, int y) {
        UpgradeProgress.Type upgradeProgressType = this.upgradeProgress.getType();
        if (upgradeProgressType != null) {
            graphics.drawCenteredString(this.font, upgradeProgressType.label(), xCenter, y, -6250336);
        }
    }

    private void renderTypeProgress(GuiGraphics graphics, int xCenter, int y, float progress) {
        MutableComponent percentageText = Component.translatable("upgradeWorld.progress.percentage", Mth.floor(progress * 100.0f));
        graphics.drawCenteredString(this.font, percentageText, xCenter, y, -6250336);
    }

    private void renderFileStats(GuiGraphics graphics, int xCenter, int yStart, int converted, int total) {
        int lineHeight = this.font.lineHeight + 3;
        graphics.drawCenteredString(this.font, Component.translatable("upgradeWorld.info.converted", converted), xCenter, yStart, -6250336);
        graphics.drawCenteredString(this.font, Component.translatable("upgradeWorld.info.total", total), xCenter, yStart + lineHeight, -6250336);
    }

    private void renderScanning(GuiGraphics graphics, int xCenter, int textTop) {
        graphics.drawCenteredString(this.font, SCANNING, xCenter, textTop + this.font.lineHeight + 3, -6250336);
    }

    private void renderFileFixerCount(GuiGraphics graphics, int xCenter, int y, int current, int total) {
        MutableComponent percentageText = Component.translatable("upgradeWorld.info.file_fix_stage", current, total);
        graphics.drawCenteredString(this.font, percentageText, xCenter, y, -6250336);
    }
}


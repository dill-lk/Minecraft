/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.reporting;

import java.util.UUID;
import java.util.function.Supplier;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineEditBox;
import net.mayaan.client.gui.components.PlayerSkinWidget;
import net.mayaan.client.gui.layouts.CommonLayouts;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.reporting.AbstractReportScreen;
import net.mayaan.client.gui.screens.reporting.ReportReasonSelectionScreen;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.multiplayer.chat.report.ReportReason;
import net.mayaan.client.multiplayer.chat.report.ReportType;
import net.mayaan.client.multiplayer.chat.report.ReportingContext;
import net.mayaan.client.multiplayer.chat.report.SkinReport;
import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.player.PlayerSkin;

public class SkinReportScreen
extends AbstractReportScreen<SkinReport.Builder> {
    private static final int SKIN_WIDTH = 85;
    private static final int FORM_WIDTH = 178;
    private static final Component TITLE = Component.translatable("gui.abuseReport.skin.title");
    private MultiLineEditBox commentBox;
    private Button selectReasonButton;

    private SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, SkinReport.Builder reportBuilder) {
        super(TITLE, lastScreen, reportingContext, reportBuilder);
    }

    public SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, UUID playerId, Supplier<PlayerSkin> skinGetter) {
        this(lastScreen, reportingContext, new SkinReport.Builder(playerId, skinGetter, reportingContext.sender().reportLimits()));
    }

    public SkinReportScreen(Screen lastScreen, ReportingContext reportingContext, SkinReport draft) {
        this(lastScreen, reportingContext, new SkinReport.Builder(draft, reportingContext.sender().reportLimits()));
    }

    @Override
    protected void addContent() {
        LinearLayout contentLayout = this.layout.addChild(LinearLayout.horizontal().spacing(8));
        contentLayout.defaultCellSetting().alignVerticallyMiddle();
        contentLayout.addChild(new PlayerSkinWidget(85, 120, this.minecraft.getEntityModels(), ((SkinReport)((SkinReport.Builder)this.reportBuilder).report()).getSkinGetter()));
        LinearLayout formLayout = contentLayout.addChild(LinearLayout.vertical().spacing(8));
        this.selectReasonButton = Button.builder(SELECT_REASON, b -> this.minecraft.setScreen(new ReportReasonSelectionScreen(this, ((SkinReport.Builder)this.reportBuilder).reason(), ReportType.SKIN, reason -> {
            ((SkinReport.Builder)this.reportBuilder).setReason((ReportReason)((Object)((Object)reason)));
            this.onReportChanged();
        }))).width(178).build();
        formLayout.addChild(CommonLayouts.labeledElement(this.font, this.selectReasonButton, OBSERVED_WHAT_LABEL));
        this.commentBox = this.createCommentBox(178, this.font.lineHeight * 8, comments -> {
            ((SkinReport.Builder)this.reportBuilder).setComments((String)comments);
            this.onReportChanged();
        });
        formLayout.addChild(CommonLayouts.labeledElement(this.font, this.commentBox, MORE_COMMENTS_LABEL, s -> s.paddingBottom(12)));
    }

    @Override
    protected void onReportChanged() {
        ReportReason reportReason = ((SkinReport.Builder)this.reportBuilder).reason();
        if (reportReason != null) {
            this.selectReasonButton.setMessage(reportReason.title());
        } else {
            this.selectReasonButton.setMessage(SELECT_REASON);
        }
        super.onReportChanged();
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (super.mouseReleased(event)) {
            return true;
        }
        return this.commentBox.mouseReleased(event);
    }
}


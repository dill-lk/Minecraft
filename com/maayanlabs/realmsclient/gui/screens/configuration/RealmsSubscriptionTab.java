/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package com.maayanlabs.realmsclient.gui.screens.configuration;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.RealmsMainScreen;
import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.dto.Subscription;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.gui.screens.RealmsPopups;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigurationTab;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.maayanlabs.realmsclient.util.RealmsUtil;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;
import net.mayaan.ChatFormatting;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.FocusableTextWidget;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.components.tabs.GridLayoutTab;
import net.mayaan.client.gui.layouts.GridLayout;
import net.mayaan.client.gui.layouts.LayoutSettings;
import net.mayaan.client.gui.layouts.SpacerElement;
import net.mayaan.client.gui.screens.ConfirmLinkScreen;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.util.CommonLinks;
import net.mayaan.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

class RealmsSubscriptionTab
extends GridLayoutTab
implements RealmsConfigurationTab {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int DEFAULT_COMPONENT_WIDTH = 200;
    private static final int EXTRA_SPACING = 2;
    private static final int DEFAULT_SPACING = 6;
    static final Component TITLE = Component.translatable("mco.configure.world.subscription.tab");
    private static final Component SUBSCRIPTION_START_LABEL = Component.translatable("mco.configure.world.subscription.start");
    private static final Component TIME_LEFT_LABEL = Component.translatable("mco.configure.world.subscription.timeleft");
    private static final Component DAYS_LEFT_LABEL = Component.translatable("mco.configure.world.subscription.recurring.daysleft");
    private static final Component SUBSCRIPTION_EXPIRED_TEXT = Component.translatable("mco.configure.world.subscription.expired").withStyle(ChatFormatting.GRAY);
    private static final Component SUBSCRIPTION_LESS_THAN_A_DAY_TEXT = Component.translatable("mco.configure.world.subscription.less_than_a_day").withStyle(ChatFormatting.GRAY);
    private static final Component UNKNOWN = Component.translatable("mco.configure.world.subscription.unknown");
    private static final Component RECURRING_INFO = Component.translatable("mco.configure.world.subscription.recurring.info");
    private final RealmsConfigureWorldScreen configurationScreen;
    private final Mayaan minecraft;
    private final Button deleteButton;
    private final FocusableTextWidget subscriptionInfo;
    private final StringWidget startDateWidget;
    private final StringWidget daysLeftLabelWidget;
    private final StringWidget daysLeftWidget;
    private RealmsServer serverData;
    private Component daysLeft = UNKNOWN;
    private Component startDate = UNKNOWN;
    private @Nullable Subscription.SubscriptionType type;

    RealmsSubscriptionTab(RealmsConfigureWorldScreen configurationScreen, Mayaan minecraft, RealmsServer serverData) {
        super(TITLE);
        this.configurationScreen = configurationScreen;
        this.minecraft = minecraft;
        this.serverData = serverData;
        GridLayout.RowHelper helper = this.layout.rowSpacing(6).createRowHelper(1);
        Font font = configurationScreen.getFont();
        helper.addChild(new StringWidget(200, font.lineHeight, SUBSCRIPTION_START_LABEL, font));
        this.startDateWidget = helper.addChild(new StringWidget(200, font.lineHeight, this.startDate, font));
        helper.addChild(SpacerElement.height(2));
        this.daysLeftLabelWidget = helper.addChild(new StringWidget(200, font.lineHeight, TIME_LEFT_LABEL, font));
        this.daysLeftWidget = helper.addChild(new StringWidget(200, font.lineHeight, this.daysLeft, font));
        helper.addChild(SpacerElement.height(2));
        helper.addChild(Button.builder(Component.translatable("mco.configure.world.subscription.extend"), button -> ConfirmLinkScreen.confirmLinkNow((Screen)configurationScreen, CommonLinks.extendRealms(serverData.remoteSubscriptionId, minecraft.getUser().getProfileId()))).bounds(0, 0, 200, 20).build());
        helper.addChild(SpacerElement.height(2));
        this.deleteButton = helper.addChild(Button.builder(Component.translatable("mco.configure.world.delete.button"), button -> minecraft.setScreen(RealmsPopups.warningPopupScreen(configurationScreen, Component.translatable("mco.configure.world.delete.question.line1"), popup -> this.deleteRealm()))).bounds(0, 0, 200, 20).build());
        helper.addChild(SpacerElement.height(2));
        this.subscriptionInfo = helper.addChild(FocusableTextWidget.builder(Component.empty(), font).maxWidth(200).build(), LayoutSettings.defaults().alignHorizontallyCenter());
        this.subscriptionInfo.setCentered(false);
        this.updateData(serverData);
    }

    private void deleteRealm() {
        RealmsUtil.runAsync(client -> client.deleteRealm(this.serverData.id), RealmsUtil.openScreenAndLogOnFailure(this.configurationScreen::createErrorScreen, "Couldn't delete world")).thenRunAsync(() -> this.minecraft.setScreen(this.configurationScreen.getLastScreen()), this.minecraft);
        this.minecraft.setScreen(this.configurationScreen);
    }

    private void getSubscription(long realmId) {
        RealmsClient client = RealmsClient.getOrCreate();
        try {
            Subscription subscription = client.subscriptionFor(realmId);
            this.daysLeft = this.daysLeftPresentation(subscription.daysLeft());
            this.startDate = RealmsSubscriptionTab.localPresentation(subscription.startDate());
            this.type = subscription.type();
        }
        catch (RealmsServiceException e) {
            LOGGER.error("Couldn't get subscription", (Throwable)e);
            this.minecraft.setScreen(this.configurationScreen.createErrorScreen(e));
        }
    }

    private static Component localPresentation(Instant time) {
        String formattedDate = ZonedDateTime.ofInstant(time, ZoneId.systemDefault()).format(Util.localizedDateFormatter(FormatStyle.MEDIUM));
        return Component.literal(formattedDate).withStyle(ChatFormatting.GRAY);
    }

    private Component daysLeftPresentation(int daysLeft) {
        boolean showDays;
        if (daysLeft < 0 && this.serverData.expired) {
            return SUBSCRIPTION_EXPIRED_TEXT;
        }
        if (daysLeft <= 1) {
            return SUBSCRIPTION_LESS_THAN_A_DAY_TEXT;
        }
        int months = daysLeft / 30;
        int days = daysLeft % 30;
        boolean showMonths = months > 0;
        boolean bl = showDays = days > 0;
        if (showMonths && showDays) {
            return Component.translatable("mco.configure.world.subscription.remaining.months.days", months, days).withStyle(ChatFormatting.GRAY);
        }
        if (showMonths) {
            return Component.translatable("mco.configure.world.subscription.remaining.months", months).withStyle(ChatFormatting.GRAY);
        }
        if (showDays) {
            return Component.translatable("mco.configure.world.subscription.remaining.days", days).withStyle(ChatFormatting.GRAY);
        }
        return Component.empty();
    }

    @Override
    public void updateData(RealmsServer serverData) {
        this.serverData = serverData;
        this.getSubscription(serverData.id);
        this.startDateWidget.setMessage(this.startDate);
        if (this.type == Subscription.SubscriptionType.NORMAL) {
            this.daysLeftLabelWidget.setMessage(TIME_LEFT_LABEL);
        } else if (this.type == Subscription.SubscriptionType.RECURRING) {
            this.daysLeftLabelWidget.setMessage(DAYS_LEFT_LABEL);
        }
        this.daysLeftWidget.setMessage(this.daysLeft);
        boolean snapshotWorld = RealmsMainScreen.isSnapshot() && serverData.parentWorldName != null;
        this.deleteButton.active = serverData.expired;
        if (snapshotWorld) {
            this.subscriptionInfo.setMessage(Component.translatable("mco.snapshot.subscription.info", serverData.parentWorldName));
        } else {
            this.subscriptionInfo.setMessage(RECURRING_INFO);
        }
        this.layout.arrangeElements();
    }

    @Override
    public Component getTabExtraNarration() {
        return CommonComponents.joinLines(TITLE, SUBSCRIPTION_START_LABEL, this.startDate, TIME_LEFT_LABEL, this.daysLeft);
    }
}


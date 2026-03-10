/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package com.maayanlabs.realmsclient.gui.screens.configuration;

import com.maayanlabs.realmsclient.dto.RealmsRegion;
import com.maayanlabs.realmsclient.dto.RealmsServer;
import com.maayanlabs.realmsclient.dto.RegionSelectionPreference;
import com.maayanlabs.realmsclient.dto.RegionSelectionPreferenceDto;
import com.maayanlabs.realmsclient.dto.ServiceQuality;
import com.maayanlabs.realmsclient.gui.screens.RealmsPopups;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigurationTab;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
import com.maayanlabs.realmsclient.gui.screens.configuration.RealmsPreferredRegionSelectionScreen;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.mayaan.ChatFormatting;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.EditBox;
import net.mayaan.client.gui.components.ImageWidget;
import net.mayaan.client.gui.components.StringWidget;
import net.mayaan.client.gui.components.Tooltip;
import net.mayaan.client.gui.components.tabs.GridLayoutTab;
import net.mayaan.client.gui.layouts.EqualSpacingLayout;
import net.mayaan.client.gui.layouts.GridLayout;
import net.mayaan.client.gui.layouts.SpacerElement;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class RealmsSettingsTab
extends GridLayoutTab
implements RealmsConfigurationTab {
    private static final int COMPONENT_WIDTH = 212;
    private static final int EXTRA_SPACING = 2;
    private static final int DEFAULT_SPACING = 6;
    static final Component TITLE = Component.translatable("mco.configure.world.settings.title");
    private static final Component NAME_LABEL = Component.translatable("mco.configure.world.name");
    private static final Component DESCRIPTION_LABEL = Component.translatable("mco.configure.world.description");
    private static final Component REGION_PREFERENCE_LABEL = Component.translatable("mco.configure.world.region_preference");
    private static final Tooltip REALM_NAME_VALIDATION_ERROR_TOOLTIP = Tooltip.create(Component.translatable("mco.configure.world.name.validation.whitespace"));
    private final RealmsConfigureWorldScreen configurationScreen;
    private final Mayaan minecraft;
    private RealmsServer serverData;
    private final Map<RealmsRegion, ServiceQuality> regionServiceQuality;
    final Button closeOpenButton;
    private final EditBox descEdit;
    private final EditBox nameEdit;
    private final StringWidget selectedRegionStringWidget;
    private final ImageWidget selectedRegionImageWidget;
    private RegionSelection preferredRegionSelection;

    RealmsSettingsTab(RealmsConfigureWorldScreen configurationScreen, Mayaan minecraft, RealmsServer serverData, Map<RealmsRegion, ServiceQuality> regionServiceQuality) {
        super(TITLE);
        this.configurationScreen = configurationScreen;
        this.minecraft = minecraft;
        this.serverData = serverData;
        this.regionServiceQuality = regionServiceQuality;
        GridLayout.RowHelper helper = this.layout.rowSpacing(6).createRowHelper(1);
        helper.addChild(new StringWidget(NAME_LABEL, configurationScreen.getFont()));
        this.nameEdit = new EditBox(minecraft.font, 0, 0, 212, 20, Component.translatable("mco.configure.world.name"));
        this.nameEdit.setMaxLength(32);
        this.nameEdit.setResponder(value -> {
            if (!this.isRealmNameValid()) {
                this.nameEdit.setTextColor(-2142128);
                this.nameEdit.setTooltip(REALM_NAME_VALIDATION_ERROR_TOOLTIP);
                return;
            }
            this.nameEdit.setTooltip(null);
            this.nameEdit.setTextColor(-2039584);
        });
        helper.addChild(this.nameEdit);
        helper.addChild(SpacerElement.height(2));
        helper.addChild(new StringWidget(DESCRIPTION_LABEL, configurationScreen.getFont()));
        this.descEdit = new EditBox(minecraft.font, 0, 0, 212, 20, Component.translatable("mco.configure.world.description"));
        this.descEdit.setMaxLength(32);
        helper.addChild(this.descEdit);
        helper.addChild(SpacerElement.height(2));
        helper.addChild(new StringWidget(REGION_PREFERENCE_LABEL, configurationScreen.getFont()));
        EqualSpacingLayout selectedRegion = new EqualSpacingLayout(0, 0, 212, configurationScreen.getFont().lineHeight, EqualSpacingLayout.Orientation.HORIZONTAL);
        this.selectedRegionStringWidget = selectedRegion.addChild(new StringWidget(192, configurationScreen.getFont().lineHeight, Component.empty(), configurationScreen.getFont()));
        this.selectedRegionImageWidget = selectedRegion.addChild(ImageWidget.sprite(10, 8, ServiceQuality.UNKNOWN.getIcon()));
        helper.addChild(selectedRegion);
        helper.addChild(Button.builder(Component.translatable("mco.configure.world.buttons.region_preference"), button -> this.openPreferenceSelector()).bounds(0, 0, 212, 20).build());
        helper.addChild(SpacerElement.height(2));
        this.closeOpenButton = helper.addChild(Button.builder(Component.empty(), button -> {
            if (serverData.state == RealmsServer.State.OPEN) {
                minecraft.setScreen(RealmsPopups.customPopupScreen(configurationScreen, Component.translatable("mco.configure.world.close.question.title"), Component.translatable("mco.configure.world.close.question.line1"), popup -> {
                    this.save();
                    configurationScreen.closeTheWorld();
                }));
            } else {
                this.save();
                configurationScreen.openTheWorld(false);
            }
        }).bounds(0, 0, 212, 20).build());
        this.closeOpenButton.active = false;
        this.updateData(serverData);
    }

    private static MutableComponent getTranslatableFromPreference(RegionSelection regionSelection) {
        return (regionSelection.preference().equals((Object)RegionSelectionPreference.MANUAL) && regionSelection.region() != null ? Component.translatable(regionSelection.region().translationKey) : Component.translatable(regionSelection.preference().translationKey)).withStyle(ChatFormatting.GRAY);
    }

    private static Identifier getServiceQualityIcon(RegionSelection regionSelection, Map<RealmsRegion, ServiceQuality> regionServiceQuality) {
        if (regionSelection.region() != null && regionServiceQuality.containsKey((Object)regionSelection.region())) {
            ServiceQuality serviceQuality = regionServiceQuality.getOrDefault((Object)regionSelection.region(), ServiceQuality.UNKNOWN);
            return serviceQuality.getIcon();
        }
        return ServiceQuality.UNKNOWN.getIcon();
    }

    private boolean isRealmNameValid() {
        String name = this.nameEdit.getValue();
        String trimmedName = name.trim();
        return !trimmedName.isEmpty() && name.length() == trimmedName.length();
    }

    private void openPreferenceSelector() {
        this.minecraft.setScreen(new RealmsPreferredRegionSelectionScreen(this.configurationScreen, this::applyRegionPreferenceSelection, this.regionServiceQuality, this.preferredRegionSelection));
    }

    private void applyRegionPreferenceSelection(RegionSelectionPreference preference, RealmsRegion region) {
        this.preferredRegionSelection = new RegionSelection(preference, region);
        this.updateRegionPreferenceValues();
    }

    private void updateRegionPreferenceValues() {
        this.selectedRegionStringWidget.setMessage(RealmsSettingsTab.getTranslatableFromPreference(this.preferredRegionSelection));
        this.selectedRegionImageWidget.updateResource(RealmsSettingsTab.getServiceQualityIcon(this.preferredRegionSelection, this.regionServiceQuality));
        this.selectedRegionImageWidget.visible = this.preferredRegionSelection.preference == RegionSelectionPreference.MANUAL;
    }

    @Override
    public void onSelected(RealmsServer serverData) {
        this.updateData(serverData);
    }

    @Override
    public void updateData(RealmsServer serverData) {
        this.serverData = serverData;
        if (serverData.regionSelectionPreference == null) {
            serverData.regionSelectionPreference = RegionSelectionPreferenceDto.DEFAULT;
        }
        if (serverData.regionSelectionPreference.regionSelectionPreference == RegionSelectionPreference.MANUAL && serverData.regionSelectionPreference.preferredRegion == null) {
            Optional first = this.regionServiceQuality.keySet().stream().findFirst();
            first.ifPresent(region -> {
                serverData.regionSelectionPreference.preferredRegion = region;
            });
        }
        String key = serverData.state == RealmsServer.State.OPEN ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
        this.closeOpenButton.setMessage(Component.translatable(key));
        this.closeOpenButton.active = true;
        this.preferredRegionSelection = new RegionSelection(serverData.regionSelectionPreference.regionSelectionPreference, serverData.regionSelectionPreference.preferredRegion);
        this.nameEdit.setValue(Objects.requireNonNullElse(serverData.getName(), ""));
        this.descEdit.setValue(serverData.getDescription());
        this.updateRegionPreferenceValues();
    }

    @Override
    public void onDeselected(RealmsServer serverData) {
        this.save();
    }

    public void save() {
        String realmName = this.nameEdit.getValue().trim();
        if (this.serverData.regionSelectionPreference != null && Objects.equals(realmName, this.serverData.name) && Objects.equals(this.descEdit.getValue(), this.serverData.motd) && this.preferredRegionSelection.preference() == this.serverData.regionSelectionPreference.regionSelectionPreference && this.preferredRegionSelection.region() == this.serverData.regionSelectionPreference.preferredRegion) {
            return;
        }
        this.configurationScreen.saveSettings(realmName, this.descEdit.getValue(), this.preferredRegionSelection.preference(), this.preferredRegionSelection.region());
    }

    public record RegionSelection(RegionSelectionPreference preference, @Nullable RealmsRegion region) {
    }
}


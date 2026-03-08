/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2BooleanMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.worldselection;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.mayaan.ChatFormatting;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.MultiLineTextWidget;
import net.mayaan.client.gui.components.ScrollableLayout;
import net.mayaan.client.gui.layouts.HeaderAndFooterLayout;
import net.mayaan.client.gui.layouts.Layout;
import net.mayaan.client.gui.layouts.LinearLayout;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.gui.screens.worldselection.SwitchGrid;
import net.mayaan.client.resources.language.I18n;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.server.packs.repository.Pack;
import net.mayaan.server.packs.repository.PackRepository;
import net.mayaan.server.packs.repository.PackSource;
import org.jspecify.annotations.Nullable;

public class ExperimentsScreen
extends Screen {
    private static final Component TITLE = Component.translatable("selectWorld.experiments");
    private static final Component INFO = Component.translatable("selectWorld.experiments.info").withStyle(ChatFormatting.RED);
    private static final int MAIN_CONTENT_WIDTH = 310;
    private static final int SCROLL_AREA_MIN_HEIGHT = 130;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Screen parent;
    private final PackRepository packRepository;
    private final Consumer<PackRepository> output;
    private final Object2BooleanMap<Pack> packs = new Object2BooleanLinkedOpenHashMap();
    private @Nullable ScrollableLayout scrollArea;

    public ExperimentsScreen(Screen parent, PackRepository packRepository, Consumer<PackRepository> output) {
        super(TITLE);
        this.parent = parent;
        this.packRepository = packRepository;
        this.output = output;
        for (Pack pack : packRepository.getAvailablePacks()) {
            if (pack.getPackSource() != PackSource.FEATURE) continue;
            this.packs.put((Object)pack, packRepository.getSelectedPacks().contains(pack));
        }
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        LinearLayout content = this.layout.addToContents(LinearLayout.vertical());
        content.addChild(new MultiLineTextWidget(INFO, this.font).setMaxWidth(310), s -> s.paddingBottom(15));
        SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(299).withInfoUnderneath(2, true).withRowSpacing(4);
        this.packs.forEach((pack, selected) -> switchGridBuilder.addSwitch(ExperimentsScreen.getHumanReadableTitle(pack), () -> this.packs.getBoolean(pack), newSelected -> this.packs.put(pack, newSelected.booleanValue())).withInfo(pack.getDescription()));
        Layout switchGridLayout = switchGridBuilder.build().layout();
        this.scrollArea = new ScrollableLayout(this.minecraft, switchGridLayout, 130);
        this.scrollArea.setMinWidth(310);
        content.addChild(this.scrollArea);
        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
        ExperimentsScreen experimentsScreen = this;
        this.layout.visitWidgets(x$0 -> experimentsScreen.addRenderableWidget(x$0));
        this.repositionElements();
    }

    private static Component getHumanReadableTitle(Pack pack) {
        String translationKey = "dataPack." + pack.getId() + ".name";
        return I18n.exists(translationKey) ? Component.translatable(translationKey) : pack.getTitle();
    }

    @Override
    protected void repositionElements() {
        this.scrollArea.setMaxHeight(130);
        this.layout.arrangeElements();
        int availableExtraHeight = this.height - this.layout.getFooterHeight() - this.scrollArea.getRectangle().bottom();
        this.scrollArea.setMaxHeight(this.scrollArea.getHeight() + availableExtraHeight);
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), INFO);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    private void onDone() {
        ArrayList<Pack> selectedPacks = new ArrayList<Pack>(this.packRepository.getSelectedPacks());
        ArrayList selectedFeatures = new ArrayList();
        this.packs.forEach((pack, selected) -> {
            selectedPacks.remove(pack);
            if (selected) {
                selectedFeatures.add(pack);
            }
        });
        selectedPacks.addAll(Lists.reverse(selectedFeatures));
        this.packRepository.setSelected(selectedPacks.stream().map(Pack::getId).toList());
        this.output.accept(this.packRepository);
    }
}


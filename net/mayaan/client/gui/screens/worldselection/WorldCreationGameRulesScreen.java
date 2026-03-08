/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.screens.worldselection;

import java.util.Optional;
import java.util.function.Consumer;
import net.mayaan.client.gui.screens.worldselection.AbstractGameRulesScreen;
import net.mayaan.world.level.gamerules.GameRules;

public class WorldCreationGameRulesScreen
extends AbstractGameRulesScreen {
    public WorldCreationGameRulesScreen(GameRules gameRules, Consumer<Optional<GameRules>> exitCallback) {
        super(gameRules, exitCallback);
    }

    @Override
    protected void initContent() {
        this.ruleList = this.layout.addToContents(new AbstractGameRulesScreen.RuleList(this, this.gameRules));
    }

    @Override
    protected void onDone() {
        this.closeAndApplyChanges();
    }

    @Override
    public void onClose() {
        this.closeAndDiscardChanges();
    }
}


/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components;

import java.util.function.Supplier;
import net.mayaan.client.gui.ComponentPath;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.AbstractWidget;
import net.mayaan.client.gui.narration.NarrationElementOutput;
import net.mayaan.client.gui.navigation.FocusNavigationEvent;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.sounds.SoundManager;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.player.PlayerModelType;
import net.mayaan.world.entity.player.PlayerSkin;
import org.jspecify.annotations.Nullable;

public class PlayerSkinWidget
extends AbstractWidget {
    private static final float MODEL_HEIGHT = 2.125f;
    private static final float FIT_SCALE = 0.97f;
    private static final float ROTATION_SENSITIVITY = 2.5f;
    private static final float DEFAULT_ROTATION_X = -5.0f;
    private static final float DEFAULT_ROTATION_Y = 30.0f;
    private static final float ROTATION_X_LIMIT = 50.0f;
    private final PlayerModel wideModel;
    private final PlayerModel slimModel;
    private final Supplier<PlayerSkin> skin;
    private float rotationX = -5.0f;
    private float rotationY = 30.0f;

    public PlayerSkinWidget(int width, int height, EntityModelSet models, Supplier<PlayerSkin> skin) {
        super(0, 0, width, height, CommonComponents.EMPTY);
        this.wideModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER), false);
        this.slimModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.skin = skin;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        float scale = 0.97f * (float)this.getHeight() / 2.125f;
        float pivotY = -1.0625f;
        PlayerSkin skin = this.skin.get();
        PlayerModel model = skin.model() == PlayerModelType.SLIM ? this.slimModel : this.wideModel;
        graphics.submitSkinRenderState(model, skin.body().texturePath(), scale, this.rotationX, this.rotationY, -1.0625f, this.getX(), this.getY(), this.getRight(), this.getBottom());
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        this.rotationX = Mth.clamp(this.rotationX - (float)dy * 2.5f, -50.0f, 50.0f);
        this.rotationY += (float)dx * 2.5f;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
        return null;
    }
}


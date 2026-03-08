/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.screens.inventory;

import java.util.stream.IntStream;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.Button;
import net.mayaan.client.gui.components.IMEPreeditOverlay;
import net.mayaan.client.gui.components.TextCursorUtils;
import net.mayaan.client.gui.font.TextFieldHelper;
import net.mayaan.client.gui.screens.Screen;
import net.mayaan.client.input.CharacterEvent;
import net.mayaan.client.input.KeyEvent;
import net.mayaan.client.input.PreeditEvent;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.client.renderer.blockentity.AbstractSignRenderer;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.protocol.game.ServerboundSignUpdatePacket;
import net.mayaan.util.ARGB;
import net.mayaan.util.Util;
import net.mayaan.world.level.block.SignBlock;
import net.mayaan.world.level.block.entity.SignBlockEntity;
import net.mayaan.world.level.block.entity.SignText;
import net.mayaan.world.level.block.state.properties.WoodType;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public abstract class AbstractSignEditScreen
extends Screen {
    protected final SignBlockEntity sign;
    private SignText text;
    private final String[] messages;
    private final boolean isFrontText;
    protected final WoodType woodType;
    private long cursorBlinkStartTime;
    private int line;
    private @Nullable TextFieldHelper signField;
    private @Nullable IMEPreeditOverlay preeditOverlay;
    private final Vector2f cursorPosScratch = new Vector2f();

    public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
        this(sign, isFrontText, shouldFilter, Component.translatable("sign.edit"));
    }

    public AbstractSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter, Component title) {
        super(title);
        this.sign = sign;
        this.text = sign.getText(isFrontText);
        this.isFrontText = isFrontText;
        this.woodType = SignBlock.getWoodType(sign.getBlockState().getBlock());
        this.messages = (String[])IntStream.range(0, 4).mapToObj(index -> this.text.getMessage(index, shouldFilter)).map(Component::getString).toArray(String[]::new);
    }

    @Override
    protected void init() {
        this.minecraft.getWindow().startTextInput();
        this.cursorBlinkStartTime = Util.getMillis();
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone()).bounds(this.width / 2 - 100, this.height / 4 + 144, 200, 20).build());
        this.signField = new TextFieldHelper(() -> this.messages[this.line], this::setMessage, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), s -> this.minecraft.font.width((String)s) <= this.sign.getMaxTextLineWidth());
    }

    @Override
    public void tick() {
        if (!this.isValid()) {
            this.onDone();
        }
    }

    private boolean isValid() {
        return this.minecraft.player != null && !this.sign.isRemoved() && !this.sign.playerIsTooFarAwayToEdit(this.minecraft.player.getUUID());
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isUp()) {
            this.line = this.line - 1 & 3;
            this.signField.setCursorToEnd();
            return true;
        }
        if (event.isDown() || event.isConfirmation()) {
            this.line = this.line + 1 & 3;
            this.signField.setCursorToEnd();
            return true;
        }
        if (this.signField.keyPressed(event)) {
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        this.signField.charTyped(event);
        return true;
    }

    @Override
    public boolean preeditUpdated(@Nullable PreeditEvent event) {
        this.preeditOverlay = event != null ? new IMEPreeditOverlay(event, this.font, this.sign.getTextLineHeight()) : null;
        return true;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
        super.render(graphics, mouseX, mouseY, a);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, -1);
        this.renderSign(graphics);
    }

    @Override
    public void onClose() {
        this.onDone();
    }

    @Override
    public void removed() {
        ClientPacketListener connection = this.minecraft.getConnection();
        if (connection != null) {
            connection.send(new ServerboundSignUpdatePacket(this.sign.getBlockPos(), this.isFrontText, this.messages[0], this.messages[1], this.messages[2], this.messages[3]));
        }
        this.minecraft.getWindow().stopTextInput();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    protected abstract void renderSignBackground(GuiGraphics var1);

    protected abstract Vector3f getSignTextScale();

    protected abstract float getSignYOffset();

    private void renderSign(GuiGraphics graphics) {
        graphics.pose().pushMatrix();
        float offsetX = (float)this.width / 2.0f;
        float offsetY = this.getSignYOffset();
        graphics.pose().translate(offsetX, offsetY);
        graphics.pose().pushMatrix();
        this.renderSignBackground(graphics);
        graphics.pose().popMatrix();
        Vector3f textScale = this.getSignTextScale();
        graphics.pose().scale(textScale.x(), textScale.y());
        this.cursorPosScratch.zero();
        this.renderSignText(graphics, this.cursorPosScratch);
        graphics.pose().popMatrix();
        if (this.preeditOverlay != null) {
            this.cursorPosScratch.mul(textScale.x(), textScale.y()).add(offsetX, offsetY);
            this.preeditOverlay.updateInputPosition((int)this.cursorPosScratch.x, (int)this.cursorPosScratch.y);
            graphics.setPreeditOverlay(this.preeditOverlay);
        }
    }

    private void renderSignText(GuiGraphics graphics, Vector2f cursorPosOutput) {
        String line;
        int i;
        int color = this.text.hasGlowingText() ? this.text.getColor().getTextColor() : AbstractSignRenderer.getDarkColor(this.text);
        boolean showCursor = TextCursorUtils.isCursorVisible(Util.getMillis() - this.cursorBlinkStartTime);
        boolean needsValidCursorPos = this.preeditOverlay != null;
        int cursorPos = this.signField.getCursorPos();
        int selectionPos = this.signField.getSelectionPos();
        int signMidpoint = 4 * this.sign.getTextLineHeight() / 2;
        int cursorY = this.line * this.sign.getTextLineHeight() - signMidpoint;
        for (i = 0; i < this.messages.length; ++i) {
            line = this.messages[i];
            if (line == null) continue;
            if (this.font.isBidirectional()) {
                line = this.font.bidirectionalShaping(line);
            }
            int x1 = -this.font.width(line) / 2;
            graphics.drawString(this.font, line, x1, i * this.sign.getTextLineHeight() - signMidpoint, color, false);
            if (i != this.line || cursorPos < 0 || !showCursor && !needsValidCursorPos) continue;
            int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
            int cursorX = cursorPosition - this.font.width(line) / 2;
            if (cursorPos < line.length()) continue;
            if (showCursor) {
                TextCursorUtils.drawAppendCursor(graphics, this.font, cursorX, cursorY, color, false);
            }
            cursorPosOutput.set((float)cursorX, (float)cursorY);
        }
        for (i = 0; i < this.messages.length; ++i) {
            line = this.messages[i];
            if (line == null || i != this.line || cursorPos < 0) continue;
            int cursorPosition = this.font.width(line.substring(0, Math.max(Math.min(cursorPos, line.length()), 0)));
            int cursorX = cursorPosition - this.font.width(line) / 2;
            if (cursorPos < line.length()) {
                if (showCursor) {
                    TextCursorUtils.drawInsertCursor(graphics, cursorX, cursorY, ARGB.opaque(color), this.sign.getTextLineHeight());
                }
                cursorPosOutput.set((float)cursorX, (float)cursorY);
            }
            if (selectionPos == cursorPos) continue;
            int startIndex = Math.min(cursorPos, selectionPos);
            int endIndex = Math.max(cursorPos, selectionPos);
            int startPosX = this.font.width(line.substring(0, startIndex)) - this.font.width(line) / 2;
            int endPosX = this.font.width(line.substring(0, endIndex)) - this.font.width(line) / 2;
            int fromX = Math.min(startPosX, endPosX);
            int toX = Math.max(startPosX, endPosX);
            graphics.textHighlight(fromX, cursorY, toX, cursorY + this.sign.getTextLineHeight(), true);
        }
    }

    private void setMessage(String message) {
        this.messages[this.line] = message;
        this.text = this.text.setMessage(this.line, Component.literal(message));
        this.sign.setText(this.text, this.isFrontText);
    }

    private void onDone() {
        this.minecraft.setScreen(null);
    }
}


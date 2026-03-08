/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.toasts;

import net.mayaan.client.Mayaan;
import net.mayaan.client.Options;
import net.mayaan.client.color.ColorLerper;
import net.mayaan.client.gui.Font;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.components.toasts.Toast;
import net.mayaan.client.gui.components.toasts.ToastManager;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.network.chat.Component;
import net.mayaan.resources.Identifier;
import net.mayaan.world.item.DyeColor;
import org.jspecify.annotations.Nullable;

public class NowPlayingToast
implements Toast {
    private static final Identifier NOW_PLAYING_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/now_playing");
    private static final Identifier MUSIC_NOTES_SPRITE = Identifier.parse("icon/music_notes");
    private static final int PADDING = 7;
    private static final int MUSIC_NOTES_SIZE = 16;
    private static final int HEIGHT = 30;
    private static final int MUSIC_NOTES_SPACE = 30;
    private static final int VISIBILITY_DURATION = 5000;
    private static final int TEXT_COLOR = DyeColor.LIGHT_GRAY.getTextColor();
    private static final long MUSIC_COLOR_CHANGE_FREQUENCY_MS = 25L;
    private static int musicNoteColorTick;
    private static long lastMusicNoteColorChange;
    private static int musicNoteColor;
    private boolean updateToast;
    private double notificationDisplayTimeMultiplier;
    private final Mayaan minecraft;
    private Toast.Visibility wantedVisibility = Toast.Visibility.HIDE;

    public NowPlayingToast() {
        this.minecraft = Mayaan.getInstance();
    }

    public static void renderToast(GuiGraphics graphics, Font font) {
        String currentSong = NowPlayingToast.getCurrentSongName();
        if (currentSong != null) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOW_PLAYING_BACKGROUND_SPRITE, 0, 0, NowPlayingToast.getWidth(currentSong, font), 30);
            int notesOffset = 7;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, MUSIC_NOTES_SPRITE, 7, 7, 16, 16, musicNoteColor);
            graphics.drawString(font, NowPlayingToast.getNowPlayingString(currentSong), 30, 15 - font.lineHeight / 2, TEXT_COLOR);
        }
    }

    private static @Nullable String getCurrentSongName() {
        return Mayaan.getInstance().getMusicManager().getCurrentMusicTranslationKey();
    }

    public static void tickMusicNotes() {
        long now;
        if (NowPlayingToast.getCurrentSongName() != null && (now = System.currentTimeMillis()) > lastMusicNoteColorChange + 25L) {
            lastMusicNoteColorChange = now;
            musicNoteColor = ColorLerper.getLerpedColor(ColorLerper.Type.MUSIC_NOTE, ++musicNoteColorTick);
        }
    }

    private static Component getNowPlayingString(@Nullable String currentSongKey) {
        if (currentSongKey == null) {
            return Component.empty();
        }
        return Component.translatable(currentSongKey.replace("/", "."));
    }

    public void showToast(Options options) {
        this.updateToast = true;
        this.notificationDisplayTimeMultiplier = options.notificationDisplayTime().get();
        this.setWantedVisibility(Toast.Visibility.SHOW);
    }

    @Override
    public void update(ToastManager manager, long fullyVisibleForMs) {
        if (this.updateToast) {
            this.wantedVisibility = (double)fullyVisibleForMs < 5000.0 * this.notificationDisplayTimeMultiplier ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
            NowPlayingToast.tickMusicNotes();
        }
    }

    @Override
    public void render(GuiGraphics graphics, Font font, long fullyVisibleForMs) {
        NowPlayingToast.renderToast(graphics, font);
    }

    @Override
    public void onFinishedRendering() {
        this.updateToast = false;
    }

    @Override
    public int width() {
        return NowPlayingToast.getWidth(NowPlayingToast.getCurrentSongName(), this.minecraft.font);
    }

    private static int getWidth(@Nullable String currentSong, Font font) {
        return 30 + font.width(NowPlayingToast.getNowPlayingString(currentSong)) + 7;
    }

    @Override
    public int height() {
        return 30;
    }

    @Override
    public float xPos(int screenWidth, float visiblePortion) {
        return (float)this.width() * visiblePortion - (float)this.width();
    }

    @Override
    public float yPos(int firstSlotIndex) {
        return 0.0f;
    }

    @Override
    public Toast.Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    public void setWantedVisibility(Toast.Visibility visibility) {
        this.wantedVisibility = visibility;
    }

    static {
        musicNoteColor = -1;
    }
}


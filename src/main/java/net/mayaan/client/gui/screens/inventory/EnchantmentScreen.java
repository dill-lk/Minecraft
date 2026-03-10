/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.client.gui.screens.inventory;

import com.google.common.collect.Lists;
import com.maayanlabs.blaze3d.platform.cursor.CursorTypes;
import java.util.ArrayList;
import java.util.Optional;
import net.mayaan.ChatFormatting;
import net.mayaan.client.gui.GuiGraphics;
import net.mayaan.client.gui.screens.inventory.AbstractContainerScreen;
import net.mayaan.client.gui.screens.inventory.EnchantmentNames;
import net.mayaan.client.input.MouseButtonEvent;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.book.BookModel;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.core.registries.Registries;
import net.mayaan.network.chat.CommonComponents;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.FormattedText;
import net.mayaan.network.chat.MutableComponent;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ARGB;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.player.Inventory;
import net.mayaan.world.inventory.EnchantmentMenu;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.enchantment.Enchantment;

public class EnchantmentScreen
extends AbstractContainerScreen<EnchantmentMenu> {
    private static final Identifier[] ENABLED_LEVEL_SPRITES = new Identifier[]{Identifier.withDefaultNamespace("container/enchanting_table/level_1"), Identifier.withDefaultNamespace("container/enchanting_table/level_2"), Identifier.withDefaultNamespace("container/enchanting_table/level_3")};
    private static final Identifier[] DISABLED_LEVEL_SPRITES = new Identifier[]{Identifier.withDefaultNamespace("container/enchanting_table/level_1_disabled"), Identifier.withDefaultNamespace("container/enchanting_table/level_2_disabled"), Identifier.withDefaultNamespace("container/enchanting_table/level_3_disabled")};
    private static final Identifier ENCHANTMENT_SLOT_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_disabled");
    private static final Identifier ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot_highlighted");
    private static final Identifier ENCHANTMENT_SLOT_SPRITE = Identifier.withDefaultNamespace("container/enchanting_table/enchantment_slot");
    private static final Identifier ENCHANTING_TABLE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/enchanting_table.png");
    private static final Identifier ENCHANTING_BOOK_LOCATION = Identifier.withDefaultNamespace("textures/entity/enchantment/enchanting_table_book.png");
    private final RandomSource random = RandomSource.create();
    private BookModel bookModel;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;

    public EnchantmentScreen(EnchantmentMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.minecraft.player.experienceDisplayStartTick = this.minecraft.player.tickCount;
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        for (int i = 0; i < 3; ++i) {
            double xx = event.x() - (double)(xo + 60);
            double yy = event.y() - (double)(yo + 14 + 19 * i);
            if (!(xx >= 0.0) || !(yy >= 0.0) || !(xx < 108.0) || !(yy < 19.0) || !((EnchantmentMenu)this.menu).clickMenuButton(this.minecraft.player, i)) continue;
            this.minecraft.gameMode.handleInventoryButtonClick(((EnchantmentMenu)this.menu).containerId, i);
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float ignored, int xm, int ym) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, ENCHANTING_TABLE_LOCATION, xo, yo, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
        this.renderBook(graphics, xo, yo);
        EnchantmentNames.getInstance().initSeed(((EnchantmentMenu)this.menu).getEnchantmentSeed());
        int goldCount = ((EnchantmentMenu)this.menu).getGoldCount();
        for (int i = 0; i < 3; ++i) {
            int leftPos = xo + 60;
            int leftPosText = leftPos + 20;
            int cost = ((EnchantmentMenu)this.menu).costs[i];
            if (cost == 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                continue;
            }
            String costText = "" + cost;
            int textWidth = 86 - this.font.width(costText);
            FormattedText message = EnchantmentNames.getInstance().getRandomName(this.font, textWidth);
            int col = -9937334;
            if (!(goldCount >= i + 1 && this.minecraft.player.experienceLevel >= cost || this.minecraft.player.hasInfiniteMaterials())) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_DISABLED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, DISABLED_LEVEL_SPRITES[i], leftPos + 1, yo + 15 + 19 * i, 16, 16);
                graphics.drawWordWrap(this.font, message, leftPosText, yo + 16 + 19 * i, textWidth, ARGB.opaque((col & 0xFEFEFE) >> 1), false);
                col = -12550384;
            } else {
                int xx = xm - (xo + 60);
                int yy = ym - (yo + 14 + 19 * i);
                if (xx >= 0 && yy >= 0 && xx < 108 && yy < 19) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_HIGHLIGHTED_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                    graphics.requestCursor(CursorTypes.POINTING_HAND);
                    col = -128;
                } else {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENCHANTMENT_SLOT_SPRITE, leftPos, yo + 14 + 19 * i, 108, 19);
                }
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ENABLED_LEVEL_SPRITES[i], leftPos + 1, yo + 15 + 19 * i, 16, 16);
                graphics.drawWordWrap(this.font, message, leftPosText, yo + 16 + 19 * i, textWidth, col, false);
                col = -8323296;
            }
            graphics.drawString(this.font, costText, leftPosText + 86 - this.font.width(costText), yo + 16 + 19 * i + 7, col);
        }
    }

    private void renderBook(GuiGraphics graphics, int left, int top) {
        float a = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float open = Mth.lerp(a, this.oOpen, this.open);
        float flip = Mth.lerp(a, this.oFlip, this.flip);
        int x0 = left + 14;
        int y0 = top + 14;
        int x1 = x0 + 38;
        int y1 = y0 + 31;
        graphics.submitBookModelRenderState(this.bookModel, ENCHANTING_BOOK_LOCATION, 40.0f, open, flip, x0, y0, x1, y1);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ignored) {
        float a = this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        super.render(graphics, mouseX, mouseY, a);
        boolean infiniteMaterials = this.minecraft.player.hasInfiniteMaterials();
        int gold = ((EnchantmentMenu)this.menu).getGoldCount();
        for (int i = 0; i < 3; ++i) {
            int minLevel = ((EnchantmentMenu)this.menu).costs[i];
            Optional enchant = this.minecraft.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(((EnchantmentMenu)this.menu).enchantClue[i]);
            if (enchant.isEmpty()) continue;
            int enchantLevel = ((EnchantmentMenu)this.menu).levelClue[i];
            int cost = i + 1;
            if (!this.isHovering(60, 14 + 19 * i, 108, 17, mouseX, mouseY) || minLevel <= 0 || enchantLevel < 0) continue;
            ArrayList texts = Lists.newArrayList();
            texts.add(Component.translatable("container.enchant.clue", Enchantment.getFullname(enchant.get(), enchantLevel)).withStyle(ChatFormatting.WHITE));
            if (!infiniteMaterials) {
                texts.add(CommonComponents.EMPTY);
                if (this.minecraft.player.experienceLevel < minLevel) {
                    texts.add(Component.translatable("container.enchant.level.requirement", ((EnchantmentMenu)this.menu).costs[i]).withStyle(ChatFormatting.RED));
                } else {
                    MutableComponent lapisCost = cost == 1 ? Component.translatable("container.enchant.lapis.one") : Component.translatable("container.enchant.lapis.many", cost);
                    texts.add(lapisCost.withStyle(gold >= cost ? ChatFormatting.GRAY : ChatFormatting.RED));
                    MutableComponent levelCost = cost == 1 ? Component.translatable("container.enchant.level.one") : Component.translatable("container.enchant.level.many", cost);
                    texts.add(levelCost.withStyle(ChatFormatting.GRAY));
                }
            }
            graphics.setComponentTooltipForNextFrame(this.font, texts, mouseX, mouseY);
            break;
        }
    }

    public void tickBook() {
        ItemStack current = ((EnchantmentMenu)this.menu).getSlot(0).getItem();
        if (!ItemStack.matches(current, this.last)) {
            this.last = current;
            do {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while (this.flip <= this.flipT + 1.0f && this.flip >= this.flipT - 1.0f);
        }
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean shouldBeOpen = false;
        for (int i = 0; i < 3; ++i) {
            if (((EnchantmentMenu)this.menu).costs[i] == 0) continue;
            shouldBeOpen = true;
            break;
        }
        this.open = shouldBeOpen ? (this.open += 0.2f) : (this.open -= 0.2f);
        this.open = Mth.clamp(this.open, 0.0f, 1.0f);
        float diff = (this.flipT - this.flip) * 0.4f;
        float max = 0.2f;
        diff = Mth.clamp(diff, -0.2f, 0.2f);
        this.flipA += (diff - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
}


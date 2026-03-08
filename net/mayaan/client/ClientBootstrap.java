/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client;

import net.mayaan.client.color.item.ItemTintSources;
import net.mayaan.client.gui.screens.dialog.DialogScreens;
import net.mayaan.client.gui.screens.dialog.body.DialogBodyHandlers;
import net.mayaan.client.gui.screens.dialog.input.InputControlHandlers;
import net.mayaan.client.renderer.item.ItemModels;
import net.mayaan.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.mayaan.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.mayaan.client.renderer.item.properties.select.SelectItemModelProperties;
import net.mayaan.client.renderer.special.SpecialModelRenderers;
import net.mayaan.client.renderer.texture.atlas.SpriteSources;

public class ClientBootstrap {
    private static volatile boolean isBootstrapped;

    public static void bootstrap() {
        if (isBootstrapped) {
            return;
        }
        isBootstrapped = true;
        ItemModels.bootstrap();
        SpecialModelRenderers.bootstrap();
        ItemTintSources.bootstrap();
        SelectItemModelProperties.bootstrap();
        ConditionalItemModelProperties.bootstrap();
        RangeSelectItemModelProperties.bootstrap();
        SpriteSources.bootstrap();
        DialogScreens.bootstrap();
        InputControlHandlers.bootstrap();
        DialogBodyHandlers.bootstrap();
    }
}


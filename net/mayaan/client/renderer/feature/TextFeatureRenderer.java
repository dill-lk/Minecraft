/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.feature;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.Font;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.SubmitNodeCollection;
import net.mayaan.client.renderer.SubmitNodeStorage;

public class TextFeatureRenderer {
    public void renderTranslucent(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource) {
        Font font = Mayaan.getInstance().font;
        for (SubmitNodeStorage.TextSubmit textSubmit : nodeCollection.getTextSubmits()) {
            if (textSubmit.outlineColor() == 0) {
                font.drawInBatch(textSubmit.string(), textSubmit.x(), textSubmit.y(), textSubmit.color(), textSubmit.dropShadow(), textSubmit.pose(), (MultiBufferSource)bufferSource, textSubmit.displayMode(), textSubmit.backgroundColor(), textSubmit.lightCoords());
                continue;
            }
            font.drawInBatch8xOutline(textSubmit.string(), textSubmit.x(), textSubmit.y(), textSubmit.color(), textSubmit.outlineColor(), textSubmit.pose(), bufferSource, textSubmit.lightCoords());
        }
    }
}


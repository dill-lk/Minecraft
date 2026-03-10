/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.rendertype;

import com.maayanlabs.blaze3d.pipeline.RenderTarget;
import java.util.function.Supplier;
import net.mayaan.client.Mayaan;
import org.jspecify.annotations.Nullable;

public class OutputTarget {
    private final String name;
    private final Supplier<@Nullable RenderTarget> renderTargetSupplier;
    public static final OutputTarget MAIN_TARGET = new OutputTarget("main_target", () -> Mayaan.getInstance().getMainRenderTarget());
    public static final OutputTarget OUTLINE_TARGET = new OutputTarget("outline_target", () -> Mayaan.getInstance().levelRenderer.entityOutlineTarget());
    public static final OutputTarget WEATHER_TARGET = new OutputTarget("weather_target", () -> Mayaan.getInstance().levelRenderer.getWeatherTarget());
    public static final OutputTarget ITEM_ENTITY_TARGET = new OutputTarget("item_entity_target", () -> Mayaan.getInstance().levelRenderer.getItemEntityTarget());

    public OutputTarget(String name, Supplier<@Nullable RenderTarget> renderTargetSupplier) {
        this.name = name;
        this.renderTargetSupplier = renderTargetSupplier;
    }

    public RenderTarget getRenderTarget() {
        RenderTarget preferredTarget = this.renderTargetSupplier.get();
        return preferredTarget != null ? preferredTarget : Mayaan.getInstance().getMainRenderTarget();
    }

    public String toString() {
        return "OutputTarget[" + this.name + "]";
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.input;

import com.maayanlabs.blaze3d.platform.InputConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.mayaan.client.input.InputWithModifiers;

public record KeyEvent(@InputConstants.Value int key, int scancode, @InputWithModifiers.Modifiers int modifiers) implements InputWithModifiers
{
    @Override
    public int input() {
        return this.key;
    }

    @Retention(value=RetentionPolicy.CLASS)
    @Target(value={ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
    public static @interface Action {
    }
}


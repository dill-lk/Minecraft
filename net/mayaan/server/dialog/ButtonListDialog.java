/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.server.dialog;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.server.dialog.ActionButton;
import net.mayaan.server.dialog.Dialog;
import net.mayaan.server.dialog.action.Action;

public interface ButtonListDialog
extends Dialog {
    public MapCodec<? extends ButtonListDialog> codec();

    public int columns();

    public Optional<ActionButton> exitAction();

    @Override
    default public Optional<Action> onCancel() {
        return this.exitAction().flatMap(ActionButton::action);
    }
}


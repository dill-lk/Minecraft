/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 */
package net.mayaan.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mayaan.commands.arguments.NbtPathArgument;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.Tag;
import net.mayaan.network.chat.Component;

public interface DataAccessor {
    public void setData(CompoundTag var1) throws CommandSyntaxException;

    public CompoundTag getData() throws CommandSyntaxException;

    public Component getModifiedSuccess();

    public Component getPrintSuccess(Tag var1);

    public Component getPrintSuccess(NbtPathArgument.NbtPath var1, double var2, int var4);
}


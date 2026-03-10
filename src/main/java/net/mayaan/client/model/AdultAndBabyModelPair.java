/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import net.mayaan.client.model.Model;

public record AdultAndBabyModelPair<T extends Model<?>>(T adultModel, T babyModel) {
    public T getModel(boolean isBaby) {
        return isBaby ? this.babyModel : this.adultModel;
    }
}


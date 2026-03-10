/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.types.templates.List$ListType
 */
package net.mayaan.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List;
import net.mayaan.util.datafix.ExtraDataFixUtils;
import net.mayaan.util.datafix.fixes.References;

public class MapBannerBlockPosFormatFix
extends DataFix {
    public MapBannerBlockPosFormatFix(Schema outputSchema) {
        super(outputSchema, false);
    }

    protected TypeRewriteRule makeRule() {
        Type type = this.getInputSchema().getType(References.SAVED_DATA_MAP_DATA);
        OpticFinder dataF = type.findField("data");
        OpticFinder bannersF = dataF.type().findField("banners");
        OpticFinder bannerF = DSL.typeFinder((Type)((List.ListType)bannersF.type()).getElement());
        return this.fixTypeEverywhereTyped("MapBannerBlockPosFormatFix", type, input -> input.updateTyped(dataF, data -> data.updateTyped(bannersF, banners -> banners.updateTyped(bannerF, banner -> banner.update(DSL.remainderFinder(), bannerTag -> bannerTag.update("Pos", ExtraDataFixUtils::fixBlockPos))))));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.data.worldgen;

import net.mayaan.data.worldgen.BootstrapContext;
import net.mayaan.data.worldgen.DesertVillagePools;
import net.mayaan.data.worldgen.PlainVillagePools;
import net.mayaan.data.worldgen.SavannaVillagePools;
import net.mayaan.data.worldgen.SnowyVillagePools;
import net.mayaan.data.worldgen.TaigaVillagePools;
import net.mayaan.world.level.levelgen.structure.pools.StructureTemplatePool;

public class VillagePools {
    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        PlainVillagePools.bootstrap(context);
        SnowyVillagePools.bootstrap(context);
        SavannaVillagePools.bootstrap(context);
        DesertVillagePools.bootstrap(context);
        TaigaVillagePools.bootstrap(context);
    }
}


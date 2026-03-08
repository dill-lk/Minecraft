/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 */
package net.mayaan.world.entity.ai.behavior;

import com.mojang.datafixers.kinds.Applicative;
import java.util.Optional;
import net.mayaan.core.GlobalPos;
import net.mayaan.core.Holder;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.server.MayaanServer;
import net.mayaan.world.entity.ai.behavior.BehaviorControl;
import net.mayaan.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.village.poi.PoiType;
import net.mayaan.world.entity.npc.villager.Villager;
import net.mayaan.world.entity.npc.villager.VillagerProfession;

public class AssignProfessionFromJobSite {
    public static BehaviorControl<Villager> create() {
        return BehaviorBuilder.create(i -> i.group(i.present(MemoryModuleType.POTENTIAL_JOB_SITE), i.registered(MemoryModuleType.JOB_SITE)).apply((Applicative)i, (potentialJobSite, jobSite) -> (level, body, timestamp) -> {
            GlobalPos pos = (GlobalPos)i.get(potentialJobSite);
            if (!pos.pos().closerToCenterThan(body.position(), 2.0) && !body.assignProfessionWhenSpawned()) {
                return false;
            }
            potentialJobSite.erase();
            jobSite.set(pos);
            level.broadcastEntityEvent(body, (byte)14);
            if (!body.getVillagerData().profession().is(VillagerProfession.NONE)) {
                return true;
            }
            MayaanServer server = level.getServer();
            Optional.ofNullable(server.getLevel(pos.dimension())).flatMap(l -> l.getPoiManager().getType(pos.pos())).flatMap(poiType -> BuiltInRegistries.VILLAGER_PROFESSION.listElements().filter(profession -> ((VillagerProfession)profession.value()).heldJobSite().test((Holder<PoiType>)poiType)).findFirst()).ifPresent(profession -> {
                body.setVillagerData(body.getVillagerData().withProfession((Holder<VillagerProfession>)profession));
                body.refreshBrain(level);
            });
            return true;
        }));
    }
}


/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.core.dispenser;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.core.dispenser.BlockSource;
import net.mayaan.core.dispenser.DefaultDispenseItemBehavior;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.block.DispenserBlock;
import net.mayaan.world.phys.AABB;

public class EquipmentDispenseItemBehavior
extends DefaultDispenseItemBehavior {
    public static final EquipmentDispenseItemBehavior INSTANCE = new EquipmentDispenseItemBehavior();

    @Override
    protected ItemStack execute(BlockSource source, ItemStack dispensed) {
        return EquipmentDispenseItemBehavior.dispenseEquipment(source, dispensed) ? dispensed : super.execute(source, dispensed);
    }

    public static boolean dispenseEquipment(BlockSource source, ItemStack dispensed) {
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
        List<LivingEntity> entities = source.level().getEntitiesOfClass(LivingEntity.class, new AABB(pos), entity -> entity.canEquipWithDispenser(dispensed));
        if (entities.isEmpty()) {
            return false;
        }
        LivingEntity target = (LivingEntity)entities.getFirst();
        EquipmentSlot slot = target.getEquipmentSlotForItem(dispensed);
        ItemStack equip = dispensed.split(1);
        target.setItemSlot(slot, equip);
        if (target instanceof Mob) {
            Mob targetMob = (Mob)target;
            targetMob.setGuaranteedDrop(slot);
            targetMob.setPersistenceRequired();
        }
        return true;
    }
}


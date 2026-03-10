/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.monster.breeze;

import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.monster.breeze.Breeze;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;

public class BreezeUtil {
    private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 50.0;

    public static Vec3 randomPointBehindTarget(LivingEntity enemy, RandomSource random) {
        int spreadDegrees = 90;
        float viewAngle = enemy.yHeadRot + 180.0f + (float)random.nextGaussian() * 90.0f / 2.0f;
        float r = Mth.lerp(random.nextFloat(), 4.0f, 8.0f);
        Vec3 direction = Vec3.directionFromRotation(0.0f, viewAngle).scale(r);
        return enemy.position().add(direction);
    }

    public static boolean hasLineOfSight(Breeze breeze, Vec3 target) {
        Vec3 from = new Vec3(breeze.getX(), breeze.getY(), breeze.getZ());
        if (target.distanceTo(from) > BreezeUtil.getMaxLineOfSightTestRange(breeze)) {
            return false;
        }
        return breeze.level().clip(new ClipContext(from, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, breeze)).getType() == HitResult.Type.MISS;
    }

    private static double getMaxLineOfSightTestRange(Breeze breeze) {
        return Math.max(50.0, breeze.getAttributeValue(Attributes.FOLLOW_RANGE));
    }
}


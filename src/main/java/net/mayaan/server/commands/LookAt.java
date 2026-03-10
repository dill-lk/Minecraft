/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.commands;

import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.arguments.EntityAnchorArgument;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.phys.Vec3;

@FunctionalInterface
public interface LookAt {
    public void perform(CommandSourceStack var1, Entity var2);

    public record LookAtPosition(Vec3 position) implements LookAt
    {
        @Override
        public void perform(CommandSourceStack source, Entity target) {
            target.lookAt(source.getAnchor(), this.position);
        }
    }

    public record LookAtEntity(Entity entity, EntityAnchorArgument.Anchor anchor) implements LookAt
    {
        @Override
        public void perform(CommandSourceStack source, Entity target) {
            if (target instanceof ServerPlayer) {
                ServerPlayer targetPlayer = (ServerPlayer)target;
                targetPlayer.lookAt(source.getAnchor(), this.entity, this.anchor);
            } else {
                target.lookAt(source.getAnchor(), this.anchor.apply(this.entity));
            }
        }
    }
}


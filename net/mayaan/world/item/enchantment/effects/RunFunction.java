/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.logging.LogUtils
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  org.slf4j.Logger
 */
package net.mayaan.world.item.enchantment.effects;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.functions.CommandFunction;
import net.mayaan.resources.Identifier;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.ServerFunctionManager;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.enchantment.EnchantedItemInUse;
import net.mayaan.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.mayaan.world.phys.Vec3;
import org.slf4j.Logger;

public record RunFunction(Identifier function) implements EnchantmentEntityEffect
{
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final MapCodec<RunFunction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("function").forGetter(RunFunction::function)).apply((Applicative)i, RunFunction::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        MayaanServer server = serverLevel.getServer();
        ServerFunctionManager functions = server.getFunctions();
        Optional<CommandFunction<CommandSourceStack>> function = functions.get(this.function);
        if (function.isPresent()) {
            CommandSourceStack source = server.createCommandSourceStack().withPermission(LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withEntity(entity).withLevel(serverLevel).withPosition(position).withRotation(entity.getRotationVector());
            functions.execute(function.get(), source);
        } else {
            LOGGER.error("Enchantment run_function effect failed for non-existent function {}", (Object)this.function);
        }
    }

    public MapCodec<RunFunction> codec() {
        return CODEC;
    }
}


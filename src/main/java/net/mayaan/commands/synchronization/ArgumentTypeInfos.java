/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.arguments.FloatArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.LongArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 */
package net.mayaan.commands.synchronization;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Locale;
import java.util.Map;
import net.mayaan.commands.arguments.AngleArgument;
import net.mayaan.commands.arguments.ColorArgument;
import net.mayaan.commands.arguments.ComponentArgument;
import net.mayaan.commands.arguments.CompoundTagArgument;
import net.mayaan.commands.arguments.DimensionArgument;
import net.mayaan.commands.arguments.EntityAnchorArgument;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.commands.arguments.GameModeArgument;
import net.mayaan.commands.arguments.GameProfileArgument;
import net.mayaan.commands.arguments.HeightmapTypeArgument;
import net.mayaan.commands.arguments.HexColorArgument;
import net.mayaan.commands.arguments.IdentifierArgument;
import net.mayaan.commands.arguments.MessageArgument;
import net.mayaan.commands.arguments.NbtPathArgument;
import net.mayaan.commands.arguments.NbtTagArgument;
import net.mayaan.commands.arguments.ObjectiveArgument;
import net.mayaan.commands.arguments.ObjectiveCriteriaArgument;
import net.mayaan.commands.arguments.OperationArgument;
import net.mayaan.commands.arguments.ParticleArgument;
import net.mayaan.commands.arguments.RangeArgument;
import net.mayaan.commands.arguments.ResourceArgument;
import net.mayaan.commands.arguments.ResourceKeyArgument;
import net.mayaan.commands.arguments.ResourceOrIdArgument;
import net.mayaan.commands.arguments.ResourceOrTagArgument;
import net.mayaan.commands.arguments.ResourceOrTagKeyArgument;
import net.mayaan.commands.arguments.ResourceSelectorArgument;
import net.mayaan.commands.arguments.ScoreHolderArgument;
import net.mayaan.commands.arguments.ScoreboardSlotArgument;
import net.mayaan.commands.arguments.SlotArgument;
import net.mayaan.commands.arguments.SlotsArgument;
import net.mayaan.commands.arguments.StyleArgument;
import net.mayaan.commands.arguments.TeamArgument;
import net.mayaan.commands.arguments.TemplateMirrorArgument;
import net.mayaan.commands.arguments.TemplateRotationArgument;
import net.mayaan.commands.arguments.TimeArgument;
import net.mayaan.commands.arguments.UuidArgument;
import net.mayaan.commands.arguments.blocks.BlockPredicateArgument;
import net.mayaan.commands.arguments.blocks.BlockStateArgument;
import net.mayaan.commands.arguments.coordinates.BlockPosArgument;
import net.mayaan.commands.arguments.coordinates.ColumnPosArgument;
import net.mayaan.commands.arguments.coordinates.RotationArgument;
import net.mayaan.commands.arguments.coordinates.SwizzleArgument;
import net.mayaan.commands.arguments.coordinates.Vec2Argument;
import net.mayaan.commands.arguments.coordinates.Vec3Argument;
import net.mayaan.commands.arguments.item.FunctionArgument;
import net.mayaan.commands.arguments.item.ItemArgument;
import net.mayaan.commands.arguments.item.ItemPredicateArgument;
import net.mayaan.commands.synchronization.ArgumentTypeInfo;
import net.mayaan.commands.synchronization.SingletonArgumentInfo;
import net.mayaan.commands.synchronization.brigadier.DoubleArgumentInfo;
import net.mayaan.commands.synchronization.brigadier.FloatArgumentInfo;
import net.mayaan.commands.synchronization.brigadier.IntegerArgumentInfo;
import net.mayaan.commands.synchronization.brigadier.LongArgumentInfo;
import net.mayaan.commands.synchronization.brigadier.StringArgumentSerializer;
import net.mayaan.core.Registry;

public class ArgumentTypeInfos {
    private static final Map<Class<?>, ArgumentTypeInfo<?, ?>> BY_CLASS = Maps.newHashMap();

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> registry, String id, Class<? extends A> brigadierType, ArgumentTypeInfo<A, T> info) {
        BY_CLASS.put(brigadierType, info);
        return Registry.register(registry, id, info);
    }

    public static ArgumentTypeInfo<?, ?> bootstrap(Registry<ArgumentTypeInfo<?, ?>> registry) {
        ArgumentTypeInfos.register(registry, "brigadier:bool", BoolArgumentType.class, SingletonArgumentInfo.contextFree(BoolArgumentType::bool));
        ArgumentTypeInfos.register(registry, "brigadier:float", FloatArgumentType.class, new FloatArgumentInfo());
        ArgumentTypeInfos.register(registry, "brigadier:double", DoubleArgumentType.class, new DoubleArgumentInfo());
        ArgumentTypeInfos.register(registry, "brigadier:integer", IntegerArgumentType.class, new IntegerArgumentInfo());
        ArgumentTypeInfos.register(registry, "brigadier:long", LongArgumentType.class, new LongArgumentInfo());
        ArgumentTypeInfos.register(registry, "brigadier:string", StringArgumentType.class, new StringArgumentSerializer());
        ArgumentTypeInfos.register(registry, "entity", EntityArgument.class, new EntityArgument.Info());
        ArgumentTypeInfos.register(registry, "game_profile", GameProfileArgument.class, SingletonArgumentInfo.contextFree(GameProfileArgument::gameProfile));
        ArgumentTypeInfos.register(registry, "block_pos", BlockPosArgument.class, SingletonArgumentInfo.contextFree(BlockPosArgument::blockPos));
        ArgumentTypeInfos.register(registry, "column_pos", ColumnPosArgument.class, SingletonArgumentInfo.contextFree(ColumnPosArgument::columnPos));
        ArgumentTypeInfos.register(registry, "vec3", Vec3Argument.class, SingletonArgumentInfo.contextFree(Vec3Argument::vec3));
        ArgumentTypeInfos.register(registry, "vec2", Vec2Argument.class, SingletonArgumentInfo.contextFree(Vec2Argument::vec2));
        ArgumentTypeInfos.register(registry, "block_state", BlockStateArgument.class, SingletonArgumentInfo.contextAware(BlockStateArgument::block));
        ArgumentTypeInfos.register(registry, "block_predicate", BlockPredicateArgument.class, SingletonArgumentInfo.contextAware(BlockPredicateArgument::blockPredicate));
        ArgumentTypeInfos.register(registry, "item_stack", ItemArgument.class, SingletonArgumentInfo.contextAware(ItemArgument::item));
        ArgumentTypeInfos.register(registry, "item_predicate", ItemPredicateArgument.class, SingletonArgumentInfo.contextAware(ItemPredicateArgument::itemPredicate));
        ArgumentTypeInfos.register(registry, "color", ColorArgument.class, SingletonArgumentInfo.contextFree(ColorArgument::color));
        ArgumentTypeInfos.register(registry, "hex_color", HexColorArgument.class, SingletonArgumentInfo.contextFree(HexColorArgument::hexColor));
        ArgumentTypeInfos.register(registry, "component", ComponentArgument.class, SingletonArgumentInfo.contextAware(ComponentArgument::textComponent));
        ArgumentTypeInfos.register(registry, "style", StyleArgument.class, SingletonArgumentInfo.contextAware(StyleArgument::style));
        ArgumentTypeInfos.register(registry, "message", MessageArgument.class, SingletonArgumentInfo.contextFree(MessageArgument::message));
        ArgumentTypeInfos.register(registry, "nbt_compound_tag", CompoundTagArgument.class, SingletonArgumentInfo.contextFree(CompoundTagArgument::compoundTag));
        ArgumentTypeInfos.register(registry, "nbt_tag", NbtTagArgument.class, SingletonArgumentInfo.contextFree(NbtTagArgument::nbtTag));
        ArgumentTypeInfos.register(registry, "nbt_path", NbtPathArgument.class, SingletonArgumentInfo.contextFree(NbtPathArgument::nbtPath));
        ArgumentTypeInfos.register(registry, "objective", ObjectiveArgument.class, SingletonArgumentInfo.contextFree(ObjectiveArgument::objective));
        ArgumentTypeInfos.register(registry, "objective_criteria", ObjectiveCriteriaArgument.class, SingletonArgumentInfo.contextFree(ObjectiveCriteriaArgument::criteria));
        ArgumentTypeInfos.register(registry, "operation", OperationArgument.class, SingletonArgumentInfo.contextFree(OperationArgument::operation));
        ArgumentTypeInfos.register(registry, "particle", ParticleArgument.class, SingletonArgumentInfo.contextAware(ParticleArgument::particle));
        ArgumentTypeInfos.register(registry, "angle", AngleArgument.class, SingletonArgumentInfo.contextFree(AngleArgument::angle));
        ArgumentTypeInfos.register(registry, "rotation", RotationArgument.class, SingletonArgumentInfo.contextFree(RotationArgument::rotation));
        ArgumentTypeInfos.register(registry, "scoreboard_slot", ScoreboardSlotArgument.class, SingletonArgumentInfo.contextFree(ScoreboardSlotArgument::displaySlot));
        ArgumentTypeInfos.register(registry, "score_holder", ScoreHolderArgument.class, new ScoreHolderArgument.Info());
        ArgumentTypeInfos.register(registry, "swizzle", SwizzleArgument.class, SingletonArgumentInfo.contextFree(SwizzleArgument::swizzle));
        ArgumentTypeInfos.register(registry, "team", TeamArgument.class, SingletonArgumentInfo.contextFree(TeamArgument::team));
        ArgumentTypeInfos.register(registry, "item_slot", SlotArgument.class, SingletonArgumentInfo.contextFree(SlotArgument::slot));
        ArgumentTypeInfos.register(registry, "item_slots", SlotsArgument.class, SingletonArgumentInfo.contextFree(SlotsArgument::slots));
        ArgumentTypeInfos.register(registry, "resource_location", IdentifierArgument.class, SingletonArgumentInfo.contextFree(IdentifierArgument::id));
        ArgumentTypeInfos.register(registry, "function", FunctionArgument.class, SingletonArgumentInfo.contextFree(FunctionArgument::functions));
        ArgumentTypeInfos.register(registry, "entity_anchor", EntityAnchorArgument.class, SingletonArgumentInfo.contextFree(EntityAnchorArgument::anchor));
        ArgumentTypeInfos.register(registry, "int_range", RangeArgument.Ints.class, SingletonArgumentInfo.contextFree(RangeArgument::intRange));
        ArgumentTypeInfos.register(registry, "float_range", RangeArgument.Floats.class, SingletonArgumentInfo.contextFree(RangeArgument::floatRange));
        ArgumentTypeInfos.register(registry, "dimension", DimensionArgument.class, SingletonArgumentInfo.contextFree(DimensionArgument::dimension));
        ArgumentTypeInfos.register(registry, "gamemode", GameModeArgument.class, SingletonArgumentInfo.contextFree(GameModeArgument::gameMode));
        ArgumentTypeInfos.register(registry, "time", TimeArgument.class, new TimeArgument.Info());
        ArgumentTypeInfos.register(registry, "resource_or_tag", ArgumentTypeInfos.fixClassType(ResourceOrTagArgument.class), new ResourceOrTagArgument.Info());
        ArgumentTypeInfos.register(registry, "resource_or_tag_key", ArgumentTypeInfos.fixClassType(ResourceOrTagKeyArgument.class), new ResourceOrTagKeyArgument.Info());
        ArgumentTypeInfos.register(registry, "resource", ArgumentTypeInfos.fixClassType(ResourceArgument.class), new ResourceArgument.Info());
        ArgumentTypeInfos.register(registry, "resource_key", ArgumentTypeInfos.fixClassType(ResourceKeyArgument.class), new ResourceKeyArgument.Info());
        ArgumentTypeInfos.register(registry, "resource_selector", ArgumentTypeInfos.fixClassType(ResourceSelectorArgument.class), new ResourceSelectorArgument.Info());
        ArgumentTypeInfos.register(registry, "template_mirror", TemplateMirrorArgument.class, SingletonArgumentInfo.contextFree(TemplateMirrorArgument::templateMirror));
        ArgumentTypeInfos.register(registry, "template_rotation", TemplateRotationArgument.class, SingletonArgumentInfo.contextFree(TemplateRotationArgument::templateRotation));
        ArgumentTypeInfos.register(registry, "heightmap", HeightmapTypeArgument.class, SingletonArgumentInfo.contextFree(HeightmapTypeArgument::heightmap));
        ArgumentTypeInfos.register(registry, "loot_table", ResourceOrIdArgument.LootTableArgument.class, SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootTable));
        ArgumentTypeInfos.register(registry, "loot_predicate", ResourceOrIdArgument.LootPredicateArgument.class, SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootPredicate));
        ArgumentTypeInfos.register(registry, "loot_modifier", ResourceOrIdArgument.LootModifierArgument.class, SingletonArgumentInfo.contextAware(ResourceOrIdArgument::lootModifier));
        ArgumentTypeInfos.register(registry, "dialog", ResourceOrIdArgument.DialogArgument.class, SingletonArgumentInfo.contextAware(ResourceOrIdArgument::dialog));
        return ArgumentTypeInfos.register(registry, "uuid", UuidArgument.class, SingletonArgumentInfo.contextFree(UuidArgument::uuid));
    }

    private static <T extends ArgumentType<?>> Class<T> fixClassType(Class<? super T> cls) {
        return cls;
    }

    public static boolean isClassRecognized(Class<?> cls) {
        return BY_CLASS.containsKey(cls);
    }

    public static <A extends ArgumentType<?>> ArgumentTypeInfo<A, ?> byClass(A argumentType) {
        ArgumentTypeInfo<?, ?> result = BY_CLASS.get(argumentType.getClass());
        if (result == null) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Unrecognized argument type %s (%s)", argumentType, argumentType.getClass()));
        }
        return result;
    }

    public static <A extends ArgumentType<?>> ArgumentTypeInfo.Template<A> unpack(A argumentType) {
        return ArgumentTypeInfos.byClass(argumentType).unpack(argumentType);
    }
}


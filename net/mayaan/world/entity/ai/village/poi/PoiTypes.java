/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Maps
 */
package net.mayaan.world.entity.ai.village.poi;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.mayaan.core.Holder;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.Identifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.util.Util;
import net.mayaan.world.entity.ai.village.poi.PoiType;
import net.mayaan.world.level.block.BedBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.properties.BedPart;

public class PoiTypes {
    public static final ResourceKey<PoiType> ARMORER = PoiTypes.createKey("armorer");
    public static final ResourceKey<PoiType> BUTCHER = PoiTypes.createKey("butcher");
    public static final ResourceKey<PoiType> CARTOGRAPHER = PoiTypes.createKey("cartographer");
    public static final ResourceKey<PoiType> CLERIC = PoiTypes.createKey("cleric");
    public static final ResourceKey<PoiType> FARMER = PoiTypes.createKey("farmer");
    public static final ResourceKey<PoiType> FISHERMAN = PoiTypes.createKey("fisherman");
    public static final ResourceKey<PoiType> FLETCHER = PoiTypes.createKey("fletcher");
    public static final ResourceKey<PoiType> LEATHERWORKER = PoiTypes.createKey("leatherworker");
    public static final ResourceKey<PoiType> LIBRARIAN = PoiTypes.createKey("librarian");
    public static final ResourceKey<PoiType> MASON = PoiTypes.createKey("mason");
    public static final ResourceKey<PoiType> SHEPHERD = PoiTypes.createKey("shepherd");
    public static final ResourceKey<PoiType> TOOLSMITH = PoiTypes.createKey("toolsmith");
    public static final ResourceKey<PoiType> WEAPONSMITH = PoiTypes.createKey("weaponsmith");
    public static final ResourceKey<PoiType> HOME = PoiTypes.createKey("home");
    public static final ResourceKey<PoiType> MEETING = PoiTypes.createKey("meeting");
    public static final ResourceKey<PoiType> BEEHIVE = PoiTypes.createKey("beehive");
    public static final ResourceKey<PoiType> BEE_NEST = PoiTypes.createKey("bee_nest");
    public static final ResourceKey<PoiType> NETHER_PORTAL = PoiTypes.createKey("nether_portal");
    public static final ResourceKey<PoiType> LODESTONE = PoiTypes.createKey("lodestone");
    public static final ResourceKey<PoiType> LIGHTNING_ROD = PoiTypes.createKey("lightning_rod");
    public static final ResourceKey<PoiType> TEST_INSTANCE = PoiTypes.createKey("test_instance");
    private static final Set<BlockState> BEDS = (Set)ImmutableList.of((Object)Blocks.RED_BED, (Object)Blocks.BLACK_BED, (Object)Blocks.BLUE_BED, (Object)Blocks.BROWN_BED, (Object)Blocks.CYAN_BED, (Object)Blocks.GRAY_BED, (Object)Blocks.GREEN_BED, (Object)Blocks.LIGHT_BLUE_BED, (Object)Blocks.LIGHT_GRAY_BED, (Object)Blocks.LIME_BED, (Object)Blocks.MAGENTA_BED, (Object)Blocks.ORANGE_BED, (Object[])new Block[]{Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED}).stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).filter(state -> state.getValue(BedBlock.PART) == BedPart.HEAD).collect(ImmutableSet.toImmutableSet());
    private static final Set<BlockState> CAULDRONS = (Set)ImmutableList.of((Object)Blocks.CAULDRON, (Object)Blocks.LAVA_CAULDRON, (Object)Blocks.WATER_CAULDRON, (Object)Blocks.POWDER_SNOW_CAULDRON).stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());
    private static final Set<BlockState> LIGHTNING_RODS = (Set)ImmutableList.of((Object)Blocks.LIGHTNING_ROD, (Object)Blocks.EXPOSED_LIGHTNING_ROD, (Object)Blocks.WEATHERED_LIGHTNING_ROD, (Object)Blocks.OXIDIZED_LIGHTNING_ROD, (Object)Blocks.WAXED_LIGHTNING_ROD, (Object)Blocks.WAXED_EXPOSED_LIGHTNING_ROD, (Object)Blocks.WAXED_WEATHERED_LIGHTNING_ROD, (Object)Blocks.WAXED_OXIDIZED_LIGHTNING_ROD).stream().flatMap(block -> block.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet());
    private static final Map<BlockState, Holder<PoiType>> TYPE_BY_STATE = Maps.newHashMap();

    private static Set<BlockState> getBlockStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }

    private static ResourceKey<PoiType> createKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.withDefaultNamespace(name));
    }

    private static PoiType register(Registry<PoiType> registry, ResourceKey<PoiType> id, Set<BlockState> matchingStates, int maxTickets, int validRange) {
        PoiType value = new PoiType(matchingStates, maxTickets, validRange);
        Registry.register(registry, id, value);
        PoiTypes.registerBlockStates(registry.getOrThrow(id), matchingStates);
        return value;
    }

    private static void registerBlockStates(Holder<PoiType> type, Set<BlockState> matchingStates) {
        matchingStates.forEach(blockState -> {
            Holder<PoiType> previous = TYPE_BY_STATE.put((BlockState)blockState, type);
            if (previous != null) {
                throw Util.pauseInIde(new IllegalStateException(String.format(Locale.ROOT, "%s is defined in more than one PoI type", blockState)));
            }
        });
    }

    public static Optional<Holder<PoiType>> forState(BlockState state) {
        return Optional.ofNullable(TYPE_BY_STATE.get(state));
    }

    public static boolean hasPoi(BlockState state) {
        return TYPE_BY_STATE.containsKey(state);
    }

    public static PoiType bootstrap(Registry<PoiType> registry) {
        PoiTypes.register(registry, ARMORER, PoiTypes.getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
        PoiTypes.register(registry, BUTCHER, PoiTypes.getBlockStates(Blocks.SMOKER), 1, 1);
        PoiTypes.register(registry, CARTOGRAPHER, PoiTypes.getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
        PoiTypes.register(registry, CLERIC, PoiTypes.getBlockStates(Blocks.BREWING_STAND), 1, 1);
        PoiTypes.register(registry, FARMER, PoiTypes.getBlockStates(Blocks.COMPOSTER), 1, 1);
        PoiTypes.register(registry, FISHERMAN, PoiTypes.getBlockStates(Blocks.BARREL), 1, 1);
        PoiTypes.register(registry, FLETCHER, PoiTypes.getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
        PoiTypes.register(registry, LEATHERWORKER, CAULDRONS, 1, 1);
        PoiTypes.register(registry, LIBRARIAN, PoiTypes.getBlockStates(Blocks.LECTERN), 1, 1);
        PoiTypes.register(registry, MASON, PoiTypes.getBlockStates(Blocks.STONECUTTER), 1, 1);
        PoiTypes.register(registry, SHEPHERD, PoiTypes.getBlockStates(Blocks.LOOM), 1, 1);
        PoiTypes.register(registry, TOOLSMITH, PoiTypes.getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
        PoiTypes.register(registry, WEAPONSMITH, PoiTypes.getBlockStates(Blocks.GRINDSTONE), 1, 1);
        PoiTypes.register(registry, HOME, BEDS, 1, 1);
        PoiTypes.register(registry, MEETING, PoiTypes.getBlockStates(Blocks.BELL), 32, 6);
        PoiTypes.register(registry, BEEHIVE, PoiTypes.getBlockStates(Blocks.BEEHIVE), 0, 1);
        PoiTypes.register(registry, BEE_NEST, PoiTypes.getBlockStates(Blocks.BEE_NEST), 0, 1);
        PoiTypes.register(registry, NETHER_PORTAL, PoiTypes.getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
        PoiTypes.register(registry, LODESTONE, PoiTypes.getBlockStates(Blocks.LODESTONE), 0, 1);
        PoiTypes.register(registry, TEST_INSTANCE, PoiTypes.getBlockStates(Blocks.TEST_INSTANCE_BLOCK), 0, 1);
        return PoiTypes.register(registry, LIGHTNING_ROD, LIGHTNING_RODS, 0, 1);
    }
}


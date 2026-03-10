/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableBiMap
 *  com.google.common.collect.ImmutableList
 *  org.apache.commons.lang3.function.TriFunction
 */
package net.mayaan.world.level.block;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.WeatheringCopper;
import net.mayaan.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

public record WeatheringCopperBlocks(Block unaffected, Block exposed, Block weathered, Block oxidized, Block waxed, Block waxedExposed, Block waxedWeathered, Block waxedOxidized) {
    public static <WaxedBlock extends Block, WeatheringBlock extends Block> WeatheringCopperBlocks create(String id, TriFunction<String, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Block> register, Function<BlockBehaviour.Properties, WaxedBlock> waxedBlockFactory, BiFunction<WeatheringCopper.WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory, Function<WeatheringCopper.WeatherState, BlockBehaviour.Properties> propertiesSupplier) {
        return new WeatheringCopperBlocks((Block)register.apply((Object)id, p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.UNAFFECTED, (BlockBehaviour.Properties)p), (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply((Object)("exposed_" + id), p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.EXPOSED, (BlockBehaviour.Properties)p), (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply((Object)("weathered_" + id), p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.WEATHERED, (BlockBehaviour.Properties)p), (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply((Object)("oxidized_" + id), p -> (Block)weatheringFactory.apply(WeatheringCopper.WeatherState.OXIDIZED, (BlockBehaviour.Properties)p), (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)), (Block)register.apply((Object)("waxed_" + id), waxedBlockFactory::apply, (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.UNAFFECTED)), (Block)register.apply((Object)("waxed_exposed_" + id), waxedBlockFactory::apply, (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.EXPOSED)), (Block)register.apply((Object)("waxed_weathered_" + id), waxedBlockFactory::apply, (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.WEATHERED)), (Block)register.apply((Object)("waxed_oxidized_" + id), waxedBlockFactory::apply, (Object)propertiesSupplier.apply(WeatheringCopper.WeatherState.OXIDIZED)));
    }

    public ImmutableBiMap<Block, Block> weatheringMapping() {
        return ImmutableBiMap.of((Object)this.unaffected, (Object)this.exposed, (Object)this.exposed, (Object)this.weathered, (Object)this.weathered, (Object)this.oxidized);
    }

    public ImmutableBiMap<Block, Block> waxedMapping() {
        return ImmutableBiMap.of((Object)this.unaffected, (Object)this.waxed, (Object)this.exposed, (Object)this.waxedExposed, (Object)this.weathered, (Object)this.waxedWeathered, (Object)this.oxidized, (Object)this.waxedOxidized);
    }

    public ImmutableList<Block> asList() {
        return ImmutableList.of((Object)this.unaffected, (Object)this.waxed, (Object)this.exposed, (Object)this.waxedExposed, (Object)this.weathered, (Object)this.waxedWeathered, (Object)this.oxidized, (Object)this.waxedOxidized);
    }

    public void forEach(Consumer<Block> consumer) {
        consumer.accept(this.unaffected);
        consumer.accept(this.exposed);
        consumer.accept(this.weathered);
        consumer.accept(this.oxidized);
        consumer.accept(this.waxed);
        consumer.accept(this.waxedExposed);
        consumer.accept(this.waxedWeathered);
        consumer.accept(this.waxedOxidized);
    }
}


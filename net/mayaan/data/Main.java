/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  joptsimple.AbstractOptionSpec
 *  joptsimple.ArgumentAcceptingOptionSpec
 *  joptsimple.OptionParser
 *  joptsimple.OptionSet
 *  joptsimple.OptionSpec
 *  joptsimple.OptionSpecBuilder
 */
package net.mayaan.data;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import joptsimple.AbstractOptionSpec;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;
import net.mayaan.SharedConstants;
import net.mayaan.SuppressForbidden;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.RegistrySetBuilder;
import net.mayaan.data.DataGenerator;
import net.mayaan.data.DataProvider;
import net.mayaan.data.PackOutput;
import net.mayaan.data.advancements.packs.VanillaAdvancementProvider;
import net.mayaan.data.info.BiomeParametersDumpReport;
import net.mayaan.data.info.BlockListReport;
import net.mayaan.data.info.CommandsReport;
import net.mayaan.data.info.DatapackStructureReport;
import net.mayaan.data.info.PacketReport;
import net.mayaan.data.info.RegistryComponentsReport;
import net.mayaan.data.info.RegistryDumpReport;
import net.mayaan.data.loot.packs.TradeRebalanceLootTableProvider;
import net.mayaan.data.loot.packs.VanillaLootTableProvider;
import net.mayaan.data.metadata.PackMetadataGenerator;
import net.mayaan.data.recipes.packs.VanillaRecipeProvider;
import net.mayaan.data.registries.RegistriesDatapackGenerator;
import net.mayaan.data.registries.TradeRebalanceRegistries;
import net.mayaan.data.registries.VanillaRegistries;
import net.mayaan.data.structures.NbtToSnbt;
import net.mayaan.data.structures.SnbtToNbt;
import net.mayaan.data.structures.StructureUpdater;
import net.mayaan.data.tags.BannerPatternTagsProvider;
import net.mayaan.data.tags.BiomeTagsProvider;
import net.mayaan.data.tags.DamageTypeTagsProvider;
import net.mayaan.data.tags.DialogTagsProvider;
import net.mayaan.data.tags.EntityTypeTagsProvider;
import net.mayaan.data.tags.FlatLevelGeneratorPresetTagsProvider;
import net.mayaan.data.tags.FluidTagsProvider;
import net.mayaan.data.tags.GameEventTagsProvider;
import net.mayaan.data.tags.InstrumentTagsProvider;
import net.mayaan.data.tags.PaintingVariantTagsProvider;
import net.mayaan.data.tags.PoiTypeTagsProvider;
import net.mayaan.data.tags.PotionTagsProvider;
import net.mayaan.data.tags.StructureTagsProvider;
import net.mayaan.data.tags.TagsProvider;
import net.mayaan.data.tags.TimelineTagsProvider;
import net.mayaan.data.tags.TradeRebalanceEnchantmentTagsProvider;
import net.mayaan.data.tags.TradeRebalanceTradeTagsProvider;
import net.mayaan.data.tags.VanillaBlockTagsProvider;
import net.mayaan.data.tags.VanillaEnchantmentTagsProvider;
import net.mayaan.data.tags.VanillaItemTagsProvider;
import net.mayaan.data.tags.VillagerTradesTagsProvider;
import net.mayaan.data.tags.WorldPresetTagsProvider;
import net.mayaan.network.chat.Component;
import net.mayaan.server.jsonrpc.dataprovider.JsonRpcApiSchema;
import net.mayaan.util.Util;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.flag.FeatureFlags;

public class Main {
    @SuppressForbidden(reason="System.out needed before bootstrap")
    public static void main(String[] args) throws IOException {
        SharedConstants.tryDetectVersion();
        OptionParser parser = new OptionParser();
        AbstractOptionSpec helpOption = parser.accepts("help", "Show the help menu").forHelp();
        OptionSpecBuilder serverOption = parser.accepts("server", "Include server generators");
        OptionSpecBuilder devOption = parser.accepts("dev", "Include development tools");
        OptionSpecBuilder reportsOption = parser.accepts("reports", "Include data reports");
        parser.accepts("validate", "Validate inputs");
        OptionSpecBuilder allOption = parser.accepts("all", "Include all generators");
        ArgumentAcceptingOptionSpec outputOption = parser.accepts("output", "Output folder").withRequiredArg().defaultsTo((Object)"generated", (Object[])new String[0]);
        ArgumentAcceptingOptionSpec inputOption = parser.accepts("input", "Input folder").withRequiredArg();
        OptionSet optionSet = parser.parse(args);
        if (optionSet.has((OptionSpec)helpOption) || !optionSet.hasOptions()) {
            parser.printHelpOn((OutputStream)System.out);
            return;
        }
        Path output = Paths.get((String)outputOption.value(optionSet), new String[0]);
        boolean allOptions = optionSet.has((OptionSpec)allOption);
        boolean server = allOptions || optionSet.has((OptionSpec)serverOption);
        boolean dev = allOptions || optionSet.has((OptionSpec)devOption);
        boolean reports = allOptions || optionSet.has((OptionSpec)reportsOption);
        List<Path> input = optionSet.valuesOf((OptionSpec)inputOption).stream().map(x$0 -> Paths.get(x$0, new String[0])).toList();
        DataGenerator.Cached generator = new DataGenerator.Cached(output, SharedConstants.getCurrentVersion(), true);
        Main.addServerDefinitionProviders(generator, server, reports);
        Main.addServerConverters(generator, input, server, dev);
        ((DataGenerator)generator).run();
        Util.shutdownExecutors();
    }

    private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> target, CompletableFuture<HolderLookup.Provider> registries) {
        return output -> (DataProvider)target.apply(output, registries);
    }

    public static void addServerConverters(DataGenerator generator, Collection<Path> input, boolean server, boolean dev) {
        DataGenerator.PackGenerator commonVanillaPack = generator.getVanillaPack(server);
        commonVanillaPack.addProvider(o -> new SnbtToNbt(o, input).addFilter(new StructureUpdater()));
        DataGenerator.PackGenerator devVanillaPack = generator.getVanillaPack(dev);
        devVanillaPack.addProvider(o -> new NbtToSnbt(o, input));
    }

    public static void addServerDefinitionProviders(DataGenerator generator, boolean server, boolean reports) {
        CompletableFuture<HolderLookup.Provider> vanillaRegistries = CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor());
        DataGenerator.PackGenerator serverVanillaPack = generator.getVanillaPack(server);
        serverVanillaPack.addProvider(Main.bindRegistries(RegistriesDatapackGenerator::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(VanillaAdvancementProvider::create, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(VanillaLootTableProvider::create, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(VanillaRecipeProvider.Runner::new, vanillaRegistries));
        TagsProvider vanillaBlockTagsProvider = serverVanillaPack.addProvider(Main.bindRegistries(VanillaBlockTagsProvider::new, vanillaRegistries));
        TagsProvider vanillaItemTagsProvider = serverVanillaPack.addProvider(Main.bindRegistries(VanillaItemTagsProvider::new, vanillaRegistries));
        TagsProvider vanillaBiomeTagsProvider = serverVanillaPack.addProvider(Main.bindRegistries(BiomeTagsProvider::new, vanillaRegistries));
        TagsProvider vanillaBannerPatternTagsProvider = serverVanillaPack.addProvider(Main.bindRegistries(BannerPatternTagsProvider::new, vanillaRegistries));
        TagsProvider vanillaStructureTagsProvider = serverVanillaPack.addProvider(Main.bindRegistries(StructureTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(DamageTypeTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(DialogTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(EntityTypeTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(FlatLevelGeneratorPresetTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(FluidTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(GameEventTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(InstrumentTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(PaintingVariantTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(PoiTypeTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(WorldPresetTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(VanillaEnchantmentTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(TimelineTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(PotionTagsProvider::new, vanillaRegistries));
        serverVanillaPack.addProvider(Main.bindRegistries(VillagerTradesTagsProvider::new, vanillaRegistries));
        DataGenerator.PackGenerator reportsVanillaPack = generator.getVanillaPack(reports);
        reportsVanillaPack.addProvider(Main.bindRegistries(BiomeParametersDumpReport::new, vanillaRegistries));
        reportsVanillaPack.addProvider(Main.bindRegistries(RegistryComponentsReport::new, vanillaRegistries));
        reportsVanillaPack.addProvider(Main.bindRegistries(BlockListReport::new, vanillaRegistries));
        reportsVanillaPack.addProvider(Main.bindRegistries(CommandsReport::new, vanillaRegistries));
        reportsVanillaPack.addProvider(RegistryDumpReport::new);
        reportsVanillaPack.addProvider(PacketReport::new);
        reportsVanillaPack.addProvider(DatapackStructureReport::new);
        reportsVanillaPack.addProvider(JsonRpcApiSchema::new);
        CompletableFuture<RegistrySetBuilder.PatchedRegistries> tradeRebalanceRegistries = TradeRebalanceRegistries.createLookup(vanillaRegistries);
        CompletionStage patchedRegistrySet = tradeRebalanceRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::patches);
        DataGenerator.PackGenerator tradeRebalancePack = generator.getBuiltinDatapack(server, "trade_rebalance");
        tradeRebalancePack.addProvider(Main.bindRegistries(RegistriesDatapackGenerator::new, (CompletableFuture<HolderLookup.Provider>)patchedRegistrySet));
        tradeRebalancePack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.trade_rebalance.description"), FeatureFlagSet.of(FeatureFlags.TRADE_REBALANCE)));
        CompletionStage patchedRegistries = tradeRebalanceRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::full);
        tradeRebalancePack.addProvider(Main.bindRegistries(TradeRebalanceLootTableProvider::create, (CompletableFuture<HolderLookup.Provider>)patchedRegistries));
        tradeRebalancePack.addProvider(Main.bindRegistries(TradeRebalanceEnchantmentTagsProvider::new, (CompletableFuture<HolderLookup.Provider>)patchedRegistries));
        tradeRebalancePack.addProvider(Main.bindRegistries(TradeRebalanceTradeTagsProvider::new, (CompletableFuture<HolderLookup.Provider>)patchedRegistries));
        DataGenerator.PackGenerator redstoneChangesPack = generator.getBuiltinDatapack(server, "redstone_experiments");
        redstoneChangesPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.redstone_experiments.description"), FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS)));
        DataGenerator.PackGenerator minecartImprovementsPack = generator.getBuiltinDatapack(server, "minecart_improvements");
        minecartImprovementsPack.addProvider(o -> PackMetadataGenerator.forFeaturePack(o, Component.translatable("dataPack.minecart_improvements.description"), FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS)));
    }
}


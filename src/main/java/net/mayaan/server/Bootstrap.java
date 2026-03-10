/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.slf4j.Logger
 */
package net.mayaan.server;

import com.mojang.logging.LogUtils;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;
import net.mayaan.SharedConstants;
import net.mayaan.SuppressForbidden;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.selector.options.EntitySelectorOptions;
import net.mayaan.core.cauldron.CauldronInteractions;
import net.mayaan.core.dispenser.DispenseItemBehavior;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.locale.Language;
import net.mayaan.server.DebugLoggedPrintStream;
import net.mayaan.server.LoggedPrintStream;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ai.attributes.Attribute;
import net.mayaan.world.entity.ai.attributes.DefaultAttributes;
import net.mayaan.world.flag.FeatureFlags;
import net.mayaan.world.item.CreativeModeTabs;
import net.mayaan.world.item.Item;
import net.mayaan.world.level.block.ComposterBlock;
import net.mayaan.world.level.block.FireBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.gamerules.GameRule;
import net.mayaan.world.level.gamerules.GameRuleTypeVisitor;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.game.MayaanGame;
import org.slf4j.Logger;

@SuppressForbidden(reason="System.out setup")
public class Bootstrap {
    public static final PrintStream STDOUT = System.out;
    private static volatile boolean isBootstrapped;
    private static final Logger LOGGER;
    public static final AtomicLong bootstrapDuration;

    public static void bootStrap() {
        if (isBootstrapped) {
            return;
        }
        isBootstrapped = true;
        Instant start = Instant.now();
        if (BuiltInRegistries.REGISTRY.keySet().isEmpty()) {
            throw new IllegalStateException("Unable to load registries");
        }
        FireBlock.bootStrap();
        ComposterBlock.bootStrap();
        if (EntityType.getKey(EntityType.PLAYER) == null) {
            throw new IllegalStateException("Failed loading EntityTypes");
        }
        EntitySelectorOptions.bootStrap();
        DispenseItemBehavior.bootStrap();
        CauldronInteractions.bootStrap();
        BuiltInRegistries.bootStrap();
        MayaanGame.bootstrap();
        CreativeModeTabs.validate();
        Bootstrap.wrapStreams();
        bootstrapDuration.set(Duration.between(start, Instant.now()).toMillis());
    }

    private static <T> void checkTranslations(Iterable<T> registry, Function<T, String> descriptionGetter, Set<String> output) {
        Language language = Language.getInstance();
        registry.forEach(t -> {
            String id = (String)descriptionGetter.apply(t);
            if (!language.has(id)) {
                output.add(id);
            }
        });
    }

    private static void checkGameruleTranslations(final Set<String> missing) {
        final Language language = Language.getInstance();
        GameRules rules = new GameRules(FeatureFlags.REGISTRY.allFlags());
        rules.visitGameRuleTypes(new GameRuleTypeVisitor(){

            @Override
            public <T> void visit(GameRule<T> gameRule) {
                if (!language.has(gameRule.getDescriptionId())) {
                    missing.add(gameRule.id());
                }
            }
        });
    }

    public static Set<String> getMissingTranslations() {
        TreeSet<String> missing = new TreeSet<String>();
        Bootstrap.checkTranslations(BuiltInRegistries.ATTRIBUTE, Attribute::getDescriptionId, missing);
        Bootstrap.checkTranslations(BuiltInRegistries.ENTITY_TYPE, EntityType::getDescriptionId, missing);
        Bootstrap.checkTranslations(BuiltInRegistries.MOB_EFFECT, MobEffect::getDescriptionId, missing);
        Bootstrap.checkTranslations(BuiltInRegistries.ITEM, Item::getDescriptionId, missing);
        Bootstrap.checkTranslations(BuiltInRegistries.BLOCK, BlockBehaviour::getDescriptionId, missing);
        Bootstrap.checkTranslations(BuiltInRegistries.CUSTOM_STAT, id -> "stat." + id.toString().replace(':', '.'), missing);
        Bootstrap.checkGameruleTranslations(missing);
        return missing;
    }

    public static void checkBootstrapCalled(Supplier<String> location) {
        if (!isBootstrapped) {
            throw Bootstrap.createBootstrapException(location);
        }
    }

    private static RuntimeException createBootstrapException(Supplier<String> location) {
        try {
            String resolvedLocation = location.get();
            return new IllegalArgumentException("Not bootstrapped (called from " + resolvedLocation + ")");
        }
        catch (Exception e) {
            IllegalArgumentException result = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
            result.addSuppressed(e);
            return result;
        }
    }

    public static void validate() {
        Bootstrap.checkBootstrapCalled(() -> "validate");
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            Bootstrap.getMissingTranslations().forEach(key -> LOGGER.error("Missing translations: {}", key));
            Commands.validate();
        }
        DefaultAttributes.validate();
    }

    private static void wrapStreams() {
        if (LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
        } else {
            System.setErr(new LoggedPrintStream("STDERR", System.err));
            System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
        }
    }

    public static void realStdoutPrintln(String string) {
        STDOUT.println(string);
    }

    public static void shutdownStdout() {
        STDOUT.close();
    }

    static {
        LOGGER = LogUtils.getLogger();
        bootstrapDuration = new AtomicLong(-1L);
    }
}


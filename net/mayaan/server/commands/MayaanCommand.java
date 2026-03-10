package net.mayaan.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.UUID;
import net.mayaan.commands.CommandBuildContext;
import net.mayaan.commands.CommandSourceStack;
import net.mayaan.commands.Commands;
import net.mayaan.commands.arguments.EntityArgument;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.faction.FactionManager;
import net.mayaan.game.faction.FactionStanding;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.magic.PlayerAnimaData;
import net.mayaan.game.story.StoryChapter;
import net.mayaan.game.story.StoryManager;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerPlayer;

/**
 * The {@code /mayaan} admin command — a single root command with four sub-groups:
 *
 * <pre>
 * /mayaan glyph &lt;player&gt; info
 * /mayaan glyph &lt;player&gt; add &lt;glyph_type&gt; &lt;count&gt;
 *
 * /mayaan anima &lt;player&gt; info
 * /mayaan anima &lt;player&gt; set &lt;amount&gt;
 *
 * /mayaan story &lt;player&gt; info
 * /mayaan story &lt;player&gt; goal &lt;chapter_id&gt; &lt;goal_id&gt; complete
 *
 * /mayaan faction &lt;player&gt; info
 * /mayaan faction &lt;player&gt; &lt;faction&gt; add &lt;points&gt;
 * /mayaan faction &lt;player&gt; &lt;faction&gt; set &lt;points&gt;
 * </pre>
 *
 * <p>All sub-commands require {@link Commands#LEVEL_ADMINS} permission.
 *
 * <p>Register this command via
 * {@link #register(CommandDispatcher, CommandBuildContext)} from
 * {@link net.mayaan.commands.Commands}.
 */
public final class MayaanCommand {

    private static final SimpleCommandExceptionType ERROR_NO_SUCH_GLYPH =
            new SimpleCommandExceptionType(Component.translatable("commands.mayaan.error.no_such_glyph"));
    private static final SimpleCommandExceptionType ERROR_NO_SUCH_FACTION =
            new SimpleCommandExceptionType(Component.translatable("commands.mayaan.error.no_such_faction"));
    private static final SimpleCommandExceptionType ERROR_NO_SUCH_CHAPTER =
            new SimpleCommandExceptionType(Component.translatable("commands.mayaan.error.no_such_chapter"));
    private static final SimpleCommandExceptionType ERROR_NO_SUCH_GOAL =
            new SimpleCommandExceptionType(Component.translatable("commands.mayaan.error.no_such_goal"));
    private static final SimpleCommandExceptionType ERROR_INVALID_AMOUNT =
            new SimpleCommandExceptionType(Component.translatable("commands.mayaan.error.invalid_amount"));

    private MayaanCommand() {}

    /**
     * Registers the {@code /mayaan} command tree with the Brigadier dispatcher.
     *
     * @param dispatcher the command dispatcher
     * @param context    the command build context (unused currently; reserved for future arguments)
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext context) {
        dispatcher.register(
                (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("mayaan")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                        .then(buildGlyphSubcommand())
                        .then(buildAnimaSubcommand())
                        .then(buildStorySubcommand())
                        .then(buildFactionSubcommand()));
    }

    // ── glyph ─────────────────────────────────────────────────────────────────

    private static LiteralArgumentBuilder<CommandSourceStack> buildGlyphSubcommand() {
        return Commands.literal("glyph")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("info")
                                .executes(c -> glyphInfo(c.getSource(),
                                        EntityArgument.getPlayer(c, "player"))))
                        .then(Commands.literal("add")
                                .then(Commands.argument("glyph_type", StringArgumentType.word())
                                        .then(Commands.argument("count", IntegerArgumentType.integer(1, 100))
                                                .executes(c -> glyphAdd(c.getSource(),
                                                        EntityArgument.getPlayer(c, "player"),
                                                        StringArgumentType.getString(c, "glyph_type"),
                                                        IntegerArgumentType.getInteger(c, "count")))))));
    }

    private static int glyphInfo(CommandSourceStack source, ServerPlayer target) {
        UUID id = target.getUUID();
        StringBuilder sb = new StringBuilder();
        sb.append("Glyph Knowledge for ").append(target.getName().getString()).append(":\n");
        int score = GlyphKnowledgeManager.INSTANCE.getKnowledgeScore(id);
        sb.append("  Knowledge Score: ").append(score).append("/7\n");
        for (GlyphType type : GlyphType.values()) {
            GlyphMastery mastery = GlyphKnowledgeManager.INSTANCE.getMastery(id, type);
            int fragments = GlyphKnowledgeManager.INSTANCE.getFragmentCount(id, type);
            sb.append("  ").append(type.getId()).append(": ")
                    .append(mastery.name().toLowerCase()).append(" (")
                    .append(fragments).append(" fragments)\n");
        }
        source.sendSuccess(() -> Component.literal(sb.toString()), false);
        return 1;
    }

    private static int glyphAdd(CommandSourceStack source, ServerPlayer target,
            String glyphTypeName, int count) throws CommandSyntaxException {
        GlyphType glyphType = GlyphType.byId(glyphTypeName);
        if (glyphType == null) {
            throw ERROR_NO_SUCH_GLYPH.create();
        }
        UUID id = target.getUUID();
        GlyphMastery newMastery = GlyphKnowledgeManager.INSTANCE.awardFragments(id, glyphType, count);
        source.sendSuccess(() -> Component.literal(
                "Awarded " + count + " " + glyphType.getId() + " fragments to "
                        + target.getName().getString() + " → mastery: "
                        + newMastery.name().toLowerCase()), true);
        return 1;
    }

    // ── anima ─────────────────────────────────────────────────────────────────

    private static LiteralArgumentBuilder<CommandSourceStack> buildAnimaSubcommand() {
        return Commands.literal("anima")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("info")
                                .executes(c -> animaInfo(c.getSource(),
                                        EntityArgument.getPlayer(c, "player"))))
                        .then(Commands.literal("set")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0, 9999))
                                        .executes(c -> animaSet(c.getSource(),
                                                EntityArgument.getPlayer(c, "player"),
                                                IntegerArgumentType.getInteger(c, "amount"))))));
    }

    private static int animaInfo(CommandSourceStack source, ServerPlayer target) {
        UUID id = target.getUUID();
        PlayerAnimaData data = AnimaManager.INSTANCE.getAnimaData(id);
        boolean drought = data.isInDrought();
        source.sendSuccess(() -> Component.literal(
                "Anima for " + target.getName().getString() + ": "
                        + (int) data.getCurrentAnima() + " / " + data.getMaxAnima()
                        + (drought ? " [DROUGHT]" : "")), false);
        return 1;
    }

    private static int animaSet(CommandSourceStack source, ServerPlayer target, int amount) {
        UUID id = target.getUUID();
        PlayerAnimaData data = AnimaManager.INSTANCE.getAnimaData(id);
        data.setCurrentAnima(Math.min(amount, data.getMaxAnima()));
        source.sendSuccess(() -> Component.literal(
                "Set anima for " + target.getName().getString() + " to " + amount), true);
        return 1;
    }

    // ── story ─────────────────────────────────────────────────────────────────

    private static LiteralArgumentBuilder<CommandSourceStack> buildStorySubcommand() {
        return Commands.literal("story")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("info")
                                .executes(c -> storyInfo(c.getSource(),
                                        EntityArgument.getPlayer(c, "player"))))
                        .then(Commands.literal("goal")
                                .then(Commands.argument("chapter_id", StringArgumentType.word())
                                        .then(Commands.argument("goal_id", StringArgumentType.word())
                                                .then(Commands.literal("complete")
                                                        .executes(c -> storyGoalComplete(
                                                                c.getSource(),
                                                                EntityArgument.getPlayer(c, "player"),
                                                                StringArgumentType.getString(c, "chapter_id"),
                                                                StringArgumentType.getString(c, "goal_id"))))))));
    }

    private static int storyInfo(CommandSourceStack source, ServerPlayer target) {
        UUID id = target.getUUID();
        StoryChapter chapter = StoryManager.INSTANCE.getCurrentChapter(id);
        String act = StoryManager.INSTANCE.getCurrentAct(id).name();
        java.util.List<net.mayaan.game.story.StoryGoal> goals =
                StoryManager.INSTANCE.getActiveGoals(id);
        StringBuilder sb = new StringBuilder();
        sb.append("Story for ").append(target.getName().getString()).append(":\n");
        sb.append("  Act: ").append(act).append("  Chapter: ").append(chapter.getId()).append("\n");
        sb.append("  Active goals (").append(goals.size()).append("):\n");
        for (net.mayaan.game.story.StoryGoal goal : goals) {
            sb.append("    - ").append(goal.getId());
            if (goal.isRequired()) sb.append(" [required]");
            sb.append("\n");
        }
        source.sendSuccess(() -> Component.literal(sb.toString()), false);
        return 1;
    }

    private static int storyGoalComplete(CommandSourceStack source, ServerPlayer target,
            String chapterId, String goalId) throws CommandSyntaxException {
        StoryChapter chapter = StoryChapter.byId(chapterId);
        if (chapter == null) {
            throw ERROR_NO_SUCH_CHAPTER.create();
        }
        net.mayaan.game.story.StoryGoal goal = chapter.findGoal(goalId);
        if (goal == null) {
            throw ERROR_NO_SUCH_GOAL.create();
        }
        boolean advanced = StoryManager.INSTANCE.completeGoal(target.getUUID(), goal);
        source.sendSuccess(() -> Component.literal(
                "Completed goal [" + chapterId + ":" + goalId + "] for "
                        + target.getName().getString()
                        + (advanced ? " — chapter advanced!" : "")), true);
        return 1;
    }

    // ── faction ───────────────────────────────────────────────────────────────

    private static LiteralArgumentBuilder<CommandSourceStack> buildFactionSubcommand() {
        return Commands.literal("faction")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("info")
                                .executes(c -> factionInfo(c.getSource(),
                                        EntityArgument.getPlayer(c, "player"))))
                        .then(Commands.argument("faction_id", StringArgumentType.word())
                                .then(Commands.literal("add")
                                        .then(Commands.argument("points",
                                                IntegerArgumentType.integer(-2000, 2000))
                                                .executes(c -> factionAdjust(c.getSource(),
                                                        EntityArgument.getPlayer(c, "player"),
                                                        StringArgumentType.getString(c, "faction_id"),
                                                        IntegerArgumentType.getInteger(c, "points"),
                                                        false))))
                                .then(Commands.literal("set")
                                        .then(Commands.argument("points",
                                                IntegerArgumentType.integer(-2000, 2000))
                                                .executes(c -> factionAdjust(c.getSource(),
                                                        EntityArgument.getPlayer(c, "player"),
                                                        StringArgumentType.getString(c, "faction_id"),
                                                        IntegerArgumentType.getInteger(c, "points"),
                                                        true))))));
    }

    private static int factionInfo(CommandSourceStack source, ServerPlayer target) {
        UUID id = target.getUUID();
        StringBuilder sb = new StringBuilder();
        sb.append("Faction standings for ").append(target.getName().getString()).append(":\n");
        for (Faction faction : Faction.values()) {
            FactionStanding standing = FactionManager.INSTANCE.getStanding(id, faction);
            int points = FactionManager.INSTANCE.getPoints(id, faction);
            sb.append("  ").append(faction.getId()).append(": ")
                    .append(standing.getDisplayName())
                    .append(" (").append(points).append(" pts)\n");
        }
        source.sendSuccess(() -> Component.literal(sb.toString()), false);
        return 1;
    }

    private static int factionAdjust(CommandSourceStack source, ServerPlayer target,
            String factionId, int points, boolean absolute) throws CommandSyntaxException {
        Faction faction = Faction.byId(factionId);
        if (faction == null) {
            throw ERROR_NO_SUCH_FACTION.create();
        }
        UUID id = target.getUUID();
        FactionStanding newStanding;
        if (absolute) {
            // "set" — calculate delta to reach target
            int current = FactionManager.INSTANCE.getPoints(id, faction);
            int delta = points - current;
            newStanding = FactionManager.INSTANCE.adjustStanding(id, faction, delta);
        } else {
            // "add" — delta directly
            newStanding = FactionManager.INSTANCE.adjustStanding(id, faction, points);
        }
        FactionStanding finalStanding = newStanding;
        source.sendSuccess(() -> Component.literal(
                (absolute ? "Set" : "Adjusted") + " " + faction.getId() + " for "
                        + target.getName().getString() + " → "
                        + finalStanding.getDisplayName()), true);
        return 1;
    }
}

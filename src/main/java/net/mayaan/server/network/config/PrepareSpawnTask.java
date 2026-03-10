/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  java.lang.MatchException
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package net.mayaan.server.network.config;

import com.mojang.logging.LogUtils;
import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.HolderLookup;
import net.mayaan.network.Connection;
import net.mayaan.network.protocol.Packet;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.level.ChunkLoadCounter;
import net.mayaan.server.level.PlayerSpawnFinder;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.level.TicketType;
import net.mayaan.server.level.progress.LevelLoadListener;
import net.mayaan.server.network.CommonListenerCookie;
import net.mayaan.server.network.ConfigurationTask;
import net.mayaan.server.players.NameAndId;
import net.mayaan.util.ProblemReporter;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.storage.LevelData;
import net.mayaan.world.level.storage.TagValueInput;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.phys.Vec2;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class PrepareSpawnTask
implements ConfigurationTask {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type("prepare_spawn");
    public static final int PREPARE_CHUNK_RADIUS = 3;
    private final MayaanServer server;
    private final NameAndId nameAndId;
    private final LevelLoadListener loadListener;
    private @Nullable State state;

    public PrepareSpawnTask(MayaanServer server, NameAndId nameAndId) {
        this.server = server;
        this.nameAndId = nameAndId;
        this.loadListener = server.getLevelLoadListener();
    }

    @Override
    public void start(Consumer<Packet<?>> connection) {
        try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);){
            Optional<ValueInput> loadedData = this.server.getPlayerList().loadPlayerData(this.nameAndId).map(tag -> TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.server.registryAccess(), tag));
            ServerPlayer.SavedPosition loadedPosition = loadedData.flatMap(tag -> tag.read(ServerPlayer.SavedPosition.MAP_CODEC)).orElse(ServerPlayer.SavedPosition.EMPTY);
            LevelData.RespawnData respawnData = this.server.getWorldData().overworldData().getRespawnData();
            ServerLevel spawnLevel = loadedPosition.dimension().map(this.server::getLevel).orElseGet(() -> {
                ServerLevel spawnDataLevel = this.server.getLevel(respawnData.dimension());
                return spawnDataLevel != null ? spawnDataLevel : this.server.overworld();
            });
            CompletableFuture spawnPosition = loadedPosition.position().map(CompletableFuture::completedFuture).orElseGet(() -> PlayerSpawnFinder.findSpawn(spawnLevel, respawnData.pos()));
            Vec2 spawnAngle = loadedPosition.rotation().orElse(new Vec2(respawnData.yaw(), respawnData.pitch()));
            this.state = new Preparing(this, spawnLevel, spawnPosition, spawnAngle);
        }
    }

    @Override
    public boolean tick() {
        State state = this.state;
        int n = 0;
        return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{Preparing.class, Ready.class}, (State)state, n)) {
            default -> throw new MatchException(null, null);
            case 0 -> {
                Preparing preparing = (Preparing)state;
                Ready ready = preparing.tick();
                if (ready != null) {
                    this.state = ready;
                    yield true;
                }
                yield false;
            }
            case 1 -> {
                Ready ignored = (Ready)state;
                yield true;
            }
            case -1 -> false;
        };
    }

    public ServerPlayer spawnPlayer(Connection connection, CommonListenerCookie cookie) {
        State state = this.state;
        if (state instanceof Ready) {
            Ready ready = (Ready)state;
            return ready.spawn(connection, cookie);
        }
        throw new IllegalStateException("Player spawn was not ready");
    }

    public void keepAlive() {
        State state = this.state;
        if (state instanceof Ready) {
            Ready ready = (Ready)state;
            ready.keepAlive();
        }
    }

    public void close() {
        State state = this.state;
        if (state instanceof Preparing) {
            Preparing preparing = (Preparing)state;
            preparing.cancel();
        }
        this.state = null;
    }

    @Override
    public ConfigurationTask.Type type() {
        return TYPE;
    }

    private final class Preparing
    implements State {
        private final ServerLevel spawnLevel;
        private final CompletableFuture<Vec3> spawnPosition;
        private final Vec2 spawnAngle;
        private @Nullable CompletableFuture<?> chunkLoadFuture;
        private final ChunkLoadCounter chunkLoadCounter;
        final /* synthetic */ PrepareSpawnTask this$0;

        private Preparing(PrepareSpawnTask prepareSpawnTask, ServerLevel spawnLevel, CompletableFuture<Vec3> spawnPosition, Vec2 spawnAngle) {
            PrepareSpawnTask prepareSpawnTask2 = prepareSpawnTask;
            Objects.requireNonNull(prepareSpawnTask2);
            this.this$0 = prepareSpawnTask2;
            this.chunkLoadCounter = new ChunkLoadCounter();
            this.spawnLevel = spawnLevel;
            this.spawnPosition = spawnPosition;
            this.spawnAngle = spawnAngle;
        }

        public void cancel() {
            this.spawnPosition.cancel(false);
        }

        public @Nullable Ready tick() {
            if (!this.spawnPosition.isDone()) {
                return null;
            }
            Vec3 spawnPosition = this.spawnPosition.join();
            if (this.chunkLoadFuture == null) {
                ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing(spawnPosition));
                this.chunkLoadCounter.track(this.spawnLevel, () -> {
                    this.chunkLoadFuture = this.spawnLevel.getChunkSource().addTicketAndLoadWithRadius(TicketType.PLAYER_SPAWN, spawnChunk, 3);
                });
                this.this$0.loadListener.start(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.totalChunks());
                this.this$0.loadListener.updateFocus(this.spawnLevel.dimension(), spawnChunk);
            }
            this.this$0.loadListener.update(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS, this.chunkLoadCounter.readyChunks(), this.chunkLoadCounter.totalChunks());
            if (!this.chunkLoadFuture.isDone()) {
                return null;
            }
            this.this$0.loadListener.finish(LevelLoadListener.Stage.LOAD_PLAYER_CHUNKS);
            return new Ready(this.this$0, this.spawnLevel, spawnPosition, this.spawnAngle);
        }
    }

    private static sealed interface State
    permits Preparing, Ready {
    }

    private final class Ready
    implements State {
        private final ServerLevel spawnLevel;
        private final Vec3 spawnPosition;
        private final Vec2 spawnAngle;
        final /* synthetic */ PrepareSpawnTask this$0;

        private Ready(PrepareSpawnTask prepareSpawnTask, ServerLevel spawnLevel, Vec3 spawnPosition, Vec2 spawnAngle) {
            PrepareSpawnTask prepareSpawnTask2 = prepareSpawnTask;
            Objects.requireNonNull(prepareSpawnTask2);
            this.this$0 = prepareSpawnTask2;
            this.spawnLevel = spawnLevel;
            this.spawnPosition = spawnPosition;
            this.spawnAngle = spawnAngle;
        }

        public void keepAlive() {
            this.spawnLevel.getChunkSource().addTicketWithRadius(TicketType.PLAYER_SPAWN, ChunkPos.containing(BlockPos.containing(this.spawnPosition)), 3);
        }

        public ServerPlayer spawn(Connection connection, CommonListenerCookie cookie) {
            ChunkPos spawnChunk = ChunkPos.containing(BlockPos.containing(this.spawnPosition));
            this.spawnLevel.waitForEntities(spawnChunk, 3);
            ServerPlayer player = new ServerPlayer(this.this$0.server, this.spawnLevel, cookie.gameProfile(), cookie.clientInformation());
            try (ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(player.problemPath(), LOGGER);){
                Optional<ValueInput> input = this.this$0.server.getPlayerList().loadPlayerData(this.this$0.nameAndId).map(tag -> TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)this.this$0.server.registryAccess(), tag));
                input.ifPresent(player::load);
                player.snapTo(this.spawnPosition, this.spawnAngle.x, this.spawnAngle.y);
                this.this$0.server.getPlayerList().placeNewPlayer(connection, player, cookie);
                input.ifPresent(tag -> {
                    player.loadAndSpawnEnderPearls((ValueInput)tag);
                    player.loadAndSpawnParentVehicle((ValueInput)tag);
                });
                ServerPlayer serverPlayer = player;
                return serverPlayer;
            }
        }
    }
}


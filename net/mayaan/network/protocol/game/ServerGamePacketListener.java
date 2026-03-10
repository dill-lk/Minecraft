/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.protocol.common.ServerCommonPacketListener;
import net.mayaan.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.mayaan.network.protocol.game.ServerboundAttackPacket;
import net.mayaan.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
import net.mayaan.network.protocol.game.ServerboundChangeDifficultyPacket;
import net.mayaan.network.protocol.game.ServerboundChangeGameModePacket;
import net.mayaan.network.protocol.game.ServerboundChatAckPacket;
import net.mayaan.network.protocol.game.ServerboundChatCommandPacket;
import net.mayaan.network.protocol.game.ServerboundChatCommandSignedPacket;
import net.mayaan.network.protocol.game.ServerboundChatPacket;
import net.mayaan.network.protocol.game.ServerboundChatSessionUpdatePacket;
import net.mayaan.network.protocol.game.ServerboundChunkBatchReceivedPacket;
import net.mayaan.network.protocol.game.ServerboundClientCommandPacket;
import net.mayaan.network.protocol.game.ServerboundClientTickEndPacket;
import net.mayaan.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.mayaan.network.protocol.game.ServerboundConfigurationAcknowledgedPacket;
import net.mayaan.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.mayaan.network.protocol.game.ServerboundContainerClickPacket;
import net.mayaan.network.protocol.game.ServerboundContainerClosePacket;
import net.mayaan.network.protocol.game.ServerboundContainerSlotStateChangedPacket;
import net.mayaan.network.protocol.game.ServerboundDebugSubscriptionRequestPacket;
import net.mayaan.network.protocol.game.ServerboundEditBookPacket;
import net.mayaan.network.protocol.game.ServerboundEntityTagQueryPacket;
import net.mayaan.network.protocol.game.ServerboundInteractPacket;
import net.mayaan.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.mayaan.network.protocol.game.ServerboundLockDifficultyPacket;
import net.mayaan.network.protocol.game.ServerboundMovePlayerPacket;
import net.mayaan.network.protocol.game.ServerboundMoveVehiclePacket;
import net.mayaan.network.protocol.game.ServerboundPaddleBoatPacket;
import net.mayaan.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.mayaan.network.protocol.game.ServerboundPickItemFromEntityPacket;
import net.mayaan.network.protocol.game.ServerboundPlaceRecipePacket;
import net.mayaan.network.protocol.game.ServerboundPlayerAbilitiesPacket;
import net.mayaan.network.protocol.game.ServerboundPlayerActionPacket;
import net.mayaan.network.protocol.game.ServerboundPlayerCommandPacket;
import net.mayaan.network.protocol.game.ServerboundPlayerInputPacket;
import net.mayaan.network.protocol.game.ServerboundPlayerLoadedPacket;
import net.mayaan.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.mayaan.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
import net.mayaan.network.protocol.game.ServerboundRenameItemPacket;
import net.mayaan.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.mayaan.network.protocol.game.ServerboundSelectBundleItemPacket;
import net.mayaan.network.protocol.game.ServerboundSelectTradePacket;
import net.mayaan.network.protocol.game.ServerboundSetBeaconPacket;
import net.mayaan.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.mayaan.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.mayaan.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.mayaan.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.mayaan.network.protocol.game.ServerboundSetGameRulePacket;
import net.mayaan.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.mayaan.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.mayaan.network.protocol.game.ServerboundSetTestBlockPacket;
import net.mayaan.network.protocol.game.ServerboundSignUpdatePacket;
import net.mayaan.network.protocol.game.ServerboundSpectateEntityPacket;
import net.mayaan.network.protocol.game.ServerboundSwingPacket;
import net.mayaan.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.mayaan.network.protocol.game.ServerboundTestInstanceBlockActionPacket;
import net.mayaan.network.protocol.game.ServerboundUseItemOnPacket;
import net.mayaan.network.protocol.game.ServerboundUseItemPacket;
import net.mayaan.network.protocol.ping.ServerPingPacketListener;

public interface ServerGamePacketListener
extends ServerCommonPacketListener,
ServerPingPacketListener {
    @Override
    default public ConnectionProtocol protocol() {
        return ConnectionProtocol.PLAY;
    }

    public void handleAnimate(ServerboundSwingPacket var1);

    public void handleChat(ServerboundChatPacket var1);

    public void handleChatCommand(ServerboundChatCommandPacket var1);

    public void handleSignedChatCommand(ServerboundChatCommandSignedPacket var1);

    public void handleChatAck(ServerboundChatAckPacket var1);

    public void handleClientCommand(ServerboundClientCommandPacket var1);

    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket var1);

    public void handleContainerClick(ServerboundContainerClickPacket var1);

    public void handlePlaceRecipe(ServerboundPlaceRecipePacket var1);

    public void handleContainerClose(ServerboundContainerClosePacket var1);

    public void handleAttack(ServerboundAttackPacket var1);

    public void handleInteract(ServerboundInteractPacket var1);

    public void handleSpectateEntity(ServerboundSpectateEntityPacket var1);

    public void handleMovePlayer(ServerboundMovePlayerPacket var1);

    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket var1);

    public void handlePlayerAction(ServerboundPlayerActionPacket var1);

    public void handlePlayerCommand(ServerboundPlayerCommandPacket var1);

    public void handlePlayerInput(ServerboundPlayerInputPacket var1);

    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket var1);

    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket var1);

    public void handleSignUpdate(ServerboundSignUpdatePacket var1);

    public void handleUseItemOn(ServerboundUseItemOnPacket var1);

    public void handleUseItem(ServerboundUseItemPacket var1);

    public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket var1);

    public void handlePaddleBoat(ServerboundPaddleBoatPacket var1);

    public void handleMoveVehicle(ServerboundMoveVehiclePacket var1);

    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket var1);

    public void handleAcceptPlayerLoad(ServerboundPlayerLoadedPacket var1);

    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket var1);

    public void handleBundleItemSelectedPacket(ServerboundSelectBundleItemPacket var1);

    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket var1);

    public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket var1);

    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket var1);

    public void handleSetCommandBlock(ServerboundSetCommandBlockPacket var1);

    public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket var1);

    public void handlePickItemFromBlock(ServerboundPickItemFromBlockPacket var1);

    public void handlePickItemFromEntity(ServerboundPickItemFromEntityPacket var1);

    public void handleRenameItem(ServerboundRenameItemPacket var1);

    public void handleSetBeaconPacket(ServerboundSetBeaconPacket var1);

    public void handleSetGameRule(ServerboundSetGameRulePacket var1);

    public void handleSetStructureBlock(ServerboundSetStructureBlockPacket var1);

    public void handleSetTestBlock(ServerboundSetTestBlockPacket var1);

    public void handleTestInstanceBlockAction(ServerboundTestInstanceBlockActionPacket var1);

    public void handleSelectTrade(ServerboundSelectTradePacket var1);

    public void handleEditBook(ServerboundEditBookPacket var1);

    public void handleEntityTagQuery(ServerboundEntityTagQueryPacket var1);

    public void handleContainerSlotStateChanged(ServerboundContainerSlotStateChangedPacket var1);

    public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket var1);

    public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket var1);

    public void handleJigsawGenerate(ServerboundJigsawGeneratePacket var1);

    public void handleChangeDifficulty(ServerboundChangeDifficultyPacket var1);

    public void handleChangeGameMode(ServerboundChangeGameModePacket var1);

    public void handleLockDifficulty(ServerboundLockDifficultyPacket var1);

    public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket var1);

    public void handleConfigurationAcknowledged(ServerboundConfigurationAcknowledgedPacket var1);

    public void handleChunkBatchReceived(ServerboundChunkBatchReceivedPacket var1);

    public void handleDebugSubscriptionRequest(ServerboundDebugSubscriptionRequestPacket var1);

    public void handleClientTickEnd(ServerboundClientTickEndPacket var1);

    // ── Mayaan-specific server-side packet handlers ───────────────────────────

    /** Handles a player's glyph cast request from the {@link net.mayaan.client.gui.screens.GlyphCastScreen}. */
    public void handleMayaanCastGlyph(net.mayaan.network.protocol.game.ServerboundMayaanCastGlyphPacket var1);
}


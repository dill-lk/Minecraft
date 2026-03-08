/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.logging.LogUtils
 *  org.jspecify.annotations.Nullable
 *  org.slf4j.Logger
 */
package com.maayanlabs.realmsclient.client.worldupload;

import com.mojang.logging.LogUtils;
import com.maayanlabs.realmsclient.client.FileUpload;
import com.maayanlabs.realmsclient.client.RealmsClient;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadCanceledException;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadException;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadFailedException;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadWorldNotClosedException;
import com.maayanlabs.realmsclient.client.worldupload.RealmsUploadWorldPacker;
import com.maayanlabs.realmsclient.client.worldupload.RealmsWorldUploadStatusTracker;
import com.maayanlabs.realmsclient.dto.RealmsSlot;
import com.maayanlabs.realmsclient.dto.UploadInfo;
import com.maayanlabs.realmsclient.exception.RealmsServiceException;
import com.maayanlabs.realmsclient.exception.RetryCallException;
import com.maayanlabs.realmsclient.gui.screens.UploadResult;
import com.maayanlabs.realmsclient.util.UploadTokenCache;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import net.mayaan.SharedConstants;
import net.mayaan.client.User;
import net.mayaan.util.Util;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class RealmsWorldUpload {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final int UPLOAD_RETRIES = 20;
    private final RealmsClient client = RealmsClient.getOrCreate();
    private final Path worldFolder;
    private final RealmsSlot realmsSlot;
    private final User user;
    private final long realmId;
    private final RealmsWorldUploadStatusTracker statusCallback;
    private volatile boolean cancelled;
    private volatile @Nullable CompletableFuture<?> uploadTask;

    public RealmsWorldUpload(Path worldFolder, RealmsSlot realmsSlot, User user, long realmId, RealmsWorldUploadStatusTracker statusCallback) {
        this.worldFolder = worldFolder;
        this.realmsSlot = realmsSlot;
        this.user = user;
        this.realmId = realmId;
        this.statusCallback = statusCallback;
    }

    public CompletableFuture<?> packAndUpload() {
        return CompletableFuture.runAsync(() -> {
            File archive = null;
            try {
                UploadInfo uploadInfo = this.requestUploadInfoWithRetries();
                archive = RealmsUploadWorldPacker.pack(this.worldFolder, () -> this.cancelled);
                this.statusCallback.setUploading();
                try (FileUpload fileUpload = new FileUpload(archive, this.realmId, this.realmsSlot.slotId, uploadInfo, this.user, SharedConstants.getCurrentVersion().name(), this.realmsSlot.options.version, this.statusCallback.getUploadStatus());){
                    UploadResult join;
                    CompletableFuture<UploadResult> uploadTask = fileUpload.startUpload();
                    this.uploadTask = uploadTask;
                    if (this.cancelled) {
                        uploadTask.cancel(true);
                        return;
                    }
                    try {
                        join = uploadTask.join();
                    }
                    catch (CompletionException e) {
                        throw e.getCause();
                    }
                    String errorMessage = join.getSimplifiedErrorMessage();
                    if (errorMessage != null) {
                        throw new RealmsUploadFailedException(errorMessage);
                    }
                    UploadTokenCache.invalidate(this.realmId);
                    this.client.updateSlot(this.realmId, this.realmsSlot.slotId, this.realmsSlot.options, this.realmsSlot.settings);
                }
            }
            catch (RealmsServiceException e) {
                throw new RealmsUploadFailedException(e.realmsError.errorMessage());
            }
            catch (InterruptedException | CancellationException e) {
                throw new RealmsUploadCanceledException();
            }
            catch (RealmsUploadException e) {
                throw e;
            }
            catch (Throwable e) {
                if (e instanceof Error) {
                    Error error = (Error)e;
                    throw error;
                }
                throw new RealmsUploadFailedException(e.getMessage());
            }
            finally {
                if (archive != null) {
                    LOGGER.debug("Deleting file {}", (Object)archive.getAbsolutePath());
                    archive.delete();
                }
            }
        }, Util.backgroundExecutor());
    }

    public void cancel() {
        this.cancelled = true;
        CompletableFuture<?> uploadTask = this.uploadTask;
        if (uploadTask != null) {
            uploadTask.cancel(true);
        }
    }

    private UploadInfo requestUploadInfoWithRetries() throws RealmsServiceException, InterruptedException {
        for (int i = 0; i < 20; ++i) {
            try {
                UploadInfo uploadInfo = this.client.requestUploadInfo(this.realmId);
                if (this.cancelled) {
                    throw new RealmsUploadCanceledException();
                }
                if (uploadInfo == null) continue;
                if (!uploadInfo.worldClosed()) {
                    throw new RealmsUploadWorldNotClosedException();
                }
                return uploadInfo;
            }
            catch (RetryCallException e) {
                Thread.sleep((long)e.delaySeconds * 1000L);
            }
        }
        throw new RealmsUploadWorldNotClosedException();
    }
}


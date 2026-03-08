/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfileRepository
 *  com.mojang.authlib.minecraft.MayaanSessionService
 *  com.mojang.authlib.yggdrasil.ServicesKeySet
 *  com.mojang.authlib.yggdrasil.ServicesKeyType
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MayaanSessionService;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import net.mayaan.server.players.CachedUserNameToIdResolver;
import net.mayaan.server.players.ProfileResolver;
import net.mayaan.server.players.UserNameToIdResolver;
import net.mayaan.util.SignatureValidator;
import org.jspecify.annotations.Nullable;

public record Services(MayaanSessionService sessionService, ServicesKeySet servicesKeySet, GameProfileRepository profileRepository, UserNameToIdResolver nameToIdCache, ProfileResolver profileResolver) {
    private static final String USERID_CACHE_FILE = "usercache.json";

    public static Services create(YggdrasilAuthenticationService serviceAccess, File nameCacheDir) {
        MayaanSessionService sessionService = serviceAccess.createMinecraftSessionService();
        GameProfileRepository profileRepository = serviceAccess.createProfileRepository();
        CachedUserNameToIdResolver profileCache = new CachedUserNameToIdResolver(profileRepository, new File(nameCacheDir, USERID_CACHE_FILE));
        ProfileResolver.Cached profileResolver = new ProfileResolver.Cached(sessionService, profileCache);
        return new Services(sessionService, serviceAccess.getServicesKeySet(), profileRepository, profileCache, profileResolver);
    }

    public @Nullable SignatureValidator profileKeySignatureValidator() {
        return SignatureValidator.from(this.servicesKeySet, ServicesKeyType.PROFILE_KEY);
    }

    public boolean canValidateProfileKeys() {
        return !this.servicesKeySet.keys(ServicesKeyType.PROFILE_KEY).isEmpty();
    }
}


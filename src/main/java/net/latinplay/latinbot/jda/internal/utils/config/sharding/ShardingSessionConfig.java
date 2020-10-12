/*
 * Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.latinplay.latinbot.jda.internal.utils.config.sharding;

import com.neovisionaries.ws.client.WebSocketFactory;
import net.latinplay.latinbot.jda.api.audio.factory.IAudioSendFactory;
import net.latinplay.latinbot.jda.api.hooks.VoiceDispatchInterceptor;
import net.latinplay.latinbot.jda.api.utils.SessionController;
import net.latinplay.latinbot.jda.internal.utils.config.SessionConfig;
import net.latinplay.latinbot.jda.internal.utils.config.flags.ConfigFlag;
import net.latinplay.latinbot.jda.internal.utils.config.flags.ShardingConfigFlag;
import okhttp3.OkHttpClient;

import java.util.EnumSet;

public class ShardingSessionConfig extends SessionConfig
{
    private final OkHttpClient.Builder builder;
    private final IAudioSendFactory audioSendFactory;
    private final EnumSet<ShardingConfigFlag> shardingFlags;

    public ShardingSessionConfig(
             SessionController sessionController,  VoiceDispatchInterceptor interceptor,
             OkHttpClient httpClient,  OkHttpClient.Builder httpClientBuilder,
             WebSocketFactory webSocketFactory,  IAudioSendFactory audioSendFactory,
            EnumSet<ConfigFlag> flags, EnumSet<ShardingConfigFlag> shardingFlags,
            int maxReconnectDelay, int largeThreshold)
    {
        super(sessionController, httpClient, webSocketFactory, interceptor, flags, maxReconnectDelay, largeThreshold);
        if (httpClient == null)
            this.builder = httpClientBuilder == null ? new OkHttpClient.Builder() : httpClientBuilder;
        else
            this.builder = null;
        this.audioSendFactory = audioSendFactory;
        this.shardingFlags = shardingFlags;
    }

    public SessionConfig toSessionConfig(OkHttpClient client)
    {
        return new SessionConfig(getSessionController(), client, getWebSocketFactory(), getVoiceDispatchInterceptor(), getFlags(), getMaxReconnectDelay(), getLargeThreshold());
    }

    public EnumSet<ShardingConfigFlag> getShardingFlags()
    {
        return this.shardingFlags;
    }


    public OkHttpClient.Builder getHttpBuilder()
    {
        return builder;
    }


    public IAudioSendFactory getAudioSendFactory()
    {
        return audioSendFactory;
    }


    public static ShardingSessionConfig getDefault()
    {
        return new ShardingSessionConfig(null, null, new OkHttpClient(), null, null, null, ConfigFlag.getDefault(), ShardingConfigFlag.getDefault(), 900, 250);
    }
}

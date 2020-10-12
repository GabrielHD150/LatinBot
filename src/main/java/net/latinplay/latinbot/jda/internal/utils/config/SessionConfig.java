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

package net.latinplay.latinbot.jda.internal.utils.config;

import com.neovisionaries.ws.client.WebSocketFactory;
import net.latinplay.latinbot.jda.api.hooks.VoiceDispatchInterceptor;
import net.latinplay.latinbot.jda.api.utils.SessionController;
import net.latinplay.latinbot.jda.api.utils.SessionControllerAdapter;
import net.latinplay.latinbot.jda.internal.utils.config.flags.ConfigFlag;
import okhttp3.OkHttpClient;

import java.util.EnumSet;

public class SessionConfig
{
    private final SessionController sessionController;
    private final OkHttpClient httpClient;
    private final WebSocketFactory webSocketFactory;
    private final VoiceDispatchInterceptor interceptor;
    private final int largeThreshold;
    private EnumSet<ConfigFlag> flags;
    private int maxReconnectDelay;

    public SessionConfig(
         SessionController sessionController,  OkHttpClient httpClient,
         WebSocketFactory webSocketFactory,  VoiceDispatchInterceptor interceptor,
        EnumSet<ConfigFlag> flags, int maxReconnectDelay, int largeThreshold)
    {
        this.sessionController = sessionController == null ? new SessionControllerAdapter() : sessionController;
        this.httpClient = httpClient;
        this.webSocketFactory = webSocketFactory == null ? new WebSocketFactory() : webSocketFactory;
        this.interceptor = interceptor;
        this.flags = flags;
        this.maxReconnectDelay = maxReconnectDelay;
        this.largeThreshold = largeThreshold;
    }

    public void setAutoReconnect(boolean autoReconnect)
    {
        if (autoReconnect)
            flags.add(ConfigFlag.AUTO_RECONNECT);
        else
            flags.remove(ConfigFlag.AUTO_RECONNECT);
    }

    
    public SessionController getSessionController()
    {
        return sessionController;
    }

    
    public OkHttpClient getHttpClient()
    {
        return httpClient;
    }

    
    public WebSocketFactory getWebSocketFactory()
    {
        return webSocketFactory;
    }

    
    public VoiceDispatchInterceptor getVoiceDispatchInterceptor()
    {
        return interceptor;
    }

    public boolean isAutoReconnect()
    {
        return flags.contains(ConfigFlag.AUTO_RECONNECT);
    }

    public boolean isRetryOnTimeout()
    {
        return flags.contains(ConfigFlag.RETRY_TIMEOUT);
    }

    public boolean isBulkDeleteSplittingEnabled()
    {
        return flags.contains(ConfigFlag.BULK_DELETE_SPLIT);
    }

    public boolean isRawEvents()
    {
        return flags.contains(ConfigFlag.RAW_EVENTS);
    }

    public boolean isRelativeRateLimit()
    {
        return flags.contains(ConfigFlag.USE_RELATIVE_RATELIMIT);
    }

    public int getMaxReconnectDelay()
    {
        return maxReconnectDelay;
    }

    public int getLargeThreshold()
    {
        return largeThreshold;
    }

    public EnumSet<ConfigFlag> getFlags()
    {
        return flags;
    }

    
    public static SessionConfig getDefault()
    {
        return new SessionConfig(null, new OkHttpClient(), null, null, ConfigFlag.getDefault(), 900, 250);
    }
}

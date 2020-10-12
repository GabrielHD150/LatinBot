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

package net.latinplay.latinbot.jda.api.utils;

import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.sharding.DefaultShardManagerBuilder;

/**
 * Compression algorithms that can be used with JDA.
 *
 * @see JDABuilder#setCompression(Compression)
 * @see DefaultShardManagerBuilder#setCompression(Compression)
 */
public enum Compression
{
    /** Don't use any compression */
    NONE(""),
    /** Use ZLIB transport compression */
    ZLIB("zlib-stream");

    private final String key;

    Compression(String key)
    {
        this.key = key;
    }

    /**
     * The key used for the gateway query to enable this compression
     *
     * @return The query key
     */
    public String getKey()
    {
        return key;
    }
}

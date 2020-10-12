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

package net.latinplay.latinbot.jda.api.events;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.sharding.DefaultShardManagerBuilder;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;

/**
 * Wrapper for the raw dispatch event received from discord.
 * <br>This provides the raw structure of a gateway event through a {@link DataObject}
 * instance containing:
 * <ul>
 *     <li>d: The payload of the package (DataObject)</li>
 *     <li>t: The type of the package (String)</li>
 *     <li>op: The opcode of the package, always 0 for dispatch (int)</li>
 *     <li>s: The sequence number, equivalent to {@link #getResponseNumber()} (long)</li>
 * </ul>
 *
 * <p>Sent after derived events. This is disabled by default and can be enabled through either
 * the {@link JDABuilder#setRawEventsEnabled(boolean) JDABuilder}
 * or {@link DefaultShardManagerBuilder#setRawEventsEnabled(boolean) DefaultShardManagerBuilder}.
 *
 * @see JDABuilder#setRawEventsEnabled(boolean) JDABuilder.setRawEventsEnabled(boolean)
 * @see DefaultShardManagerBuilder#setRawEventsEnabled(boolean) DefaultShardManagerBuilder.setRawEventsEnabled(boolean)
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway" target="_blank">Gateway Documentation</a>
 */
public class RawGatewayEvent extends Event
{
    private final DataObject data;

    public RawGatewayEvent( JDA api, long responseNumber,  DataObject data)
    {
        super(api, responseNumber);
        this.data = data;
    }

    /**
     * The raw gateway package including sequence and type.
     *
     * <ul>
     *     <li>d: The payload of the package (DataObject)</li>
     *     <li>t: The type of the package (String)</li>
     *     <li>op: The opcode of the package, always 0 for dispatch (int)</li>
     *     <li>s: The sequence number, equivalent to {@link #getResponseNumber()} (long)</li>
     * </ul>
     *
     * @return The data object
     */

    public DataObject getPackage()
    {
        return data;
    }

    /**
     * The payload of the package.
     *
     * @return The payload as a {@link DataObject} instance
     */

    public DataObject getPayload()
    {
        return data.getObject("d");
    }

    /**
     * The type of event.
     *
     * @return The type of event.
     */

    public String getType()
    {
        return data.getString("t");
    }
}

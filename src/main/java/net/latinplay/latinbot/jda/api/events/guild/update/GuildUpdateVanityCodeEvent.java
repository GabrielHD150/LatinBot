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

package net.latinplay.latinbot.jda.api.events.guild.update;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Guild;

/**
 * Indicates that the {@link Guild#getVanityUrl() vanity url} of a {@link Guild Guild} changed.
 *
 * <p>Can be used to detect when the vanity url changes and retrieve the old one
 *
 * <p>Identifier: {@code vanity_code}
 */
public class GuildUpdateVanityCodeEvent extends GenericGuildUpdateEvent<String>
{
    public static final String IDENTIFIER = "vanity_code";

    public GuildUpdateVanityCodeEvent( JDA api, long responseNumber,  Guild guild,  String previous)
    {
        super(api, responseNumber, guild, previous, guild.getVanityCode(), IDENTIFIER);
    }

    /**
     * The old vanity code
     *
     * @return The old vanity code
     */

    public String getOldVanityCode()
    {
        return getOldValue();
    }

    /**
     * The old vanity url
     *
     * @return The old vanity url
     */

    public String getOldVanityUrl()
    {
        return getOldVanityCode() == null ? null : "https://discord.gg/" + getOldVanityCode();
    }

    /**
     * The new vanity code
     *
     * @return The new vanity code
     */

    public String getNewVanityCode()
    {
        return getNewValue();
    }

    /**
     * The new vanity url
     *
     * @return The new vanity url
     */

    public String getNewVanityUrl()
    {
        return getNewVanityCode() == null ? null : "https://discord.gg/" + getNewVanityCode();
    }
}

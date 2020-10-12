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
 * Indicates that the {@link Guild#getBannerId() banner} of a {@link Guild Guild} changed.
 *
 * <p>Can be used to detect when the banner changes and retrieve the old one
 *
 * <p>Identifier: {@code banner}
 */
public class GuildUpdateBannerEvent extends GenericGuildUpdateEvent<String>
{
    public static final String IDENTIFIER = "banner";

    public GuildUpdateBannerEvent( JDA api, long responseNumber,  Guild guild,  String previous)
    {
        super(api, responseNumber, guild, previous, guild.getBannerId(), IDENTIFIER);
    }

    /**
     * The new banner id
     *
     * @return The new banner id, or null if the banner was removed
     */

    public String getNewBannerId()
    {
        return getNewValue();
    }

    /**
     * The new banner url
     *
     * @return The new banner url, or null if the banner was removed
     */

    public String getNewBannerIdUrl()
    {
        return next == null ? null : String.format(Guild.BANNER_URL, guild.getId(), next);
    }

    /**
     * The old banner id
     *
     * @return The old banner id, or null if the banner didn't exist
     */

    public String getOldBannerId()
    {
        return getOldValue();
    }

    /**
     * The old banner url
     *
     * @return The old banner url, or null if the banner didn't exist
     */

    public String getOldBannerUrl()
    {
        return previous == null ? null : String.format(Guild.BANNER_URL, guild.getId(), previous);
    }
}

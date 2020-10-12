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
 * Indicates that the {@link Guild#getBoostCount() boost count} of a {@link Guild Guild} changed.
 *
 * <p>Can be used to detect when the boost count changes and retrieve the old one
 *
 * <p>Identifier: {@code boost_count}
 */
public class GuildUpdateBoostCountEvent extends GenericGuildUpdateEvent<Integer>
{
    public static final String IDENTIFIER = "boost_count";

    public GuildUpdateBoostCountEvent( JDA api, long responseNumber,  Guild guild, int previous)
    {
        super(api, responseNumber, guild, previous, guild.getBoostCount(), IDENTIFIER);
    }

    /**
     * The old boost count
     *
     * @return The old boost count
     */
    public int getOldBoostCount()
    {
        return getOldValue();
    }

    /**
     * The new boost count
     *
     * @return The new boost count
     */
    public int getNewBoostCount()
    {
        return getNewValue();
    }


    @Override
    public Integer getOldValue()
    {
        return super.getOldValue();
    }


    @Override
    public Integer getNewValue()
    {
        return super.getNewValue();
    }
}

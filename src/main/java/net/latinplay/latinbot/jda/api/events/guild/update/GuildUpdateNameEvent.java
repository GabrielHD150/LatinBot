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
 * Indicates that the name of a {@link Guild Guild} changed.
 *
 * <p>Can be used to detect when a guild name changes and retrieve the old one
 *
 * <p>Identifier: {@code name}
 */
public class GuildUpdateNameEvent extends GenericGuildUpdateEvent<String>
{
    public static final String IDENTIFIER = "name";

    public GuildUpdateNameEvent( JDA api, long responseNumber,  Guild guild,  String oldName)
    {
        super(api, responseNumber, guild, oldName, guild.getName(), IDENTIFIER);
    }

    /**
     * The old name
     *
     * @return The old name
     */

    public String getOldName()
    {
        return getOldValue();
    }

    /**
     * The new name
     *
     * @return The new name
     */

    public String getNewName()
    {
        return getNewValue();
    }


    @Override
    public String getOldValue()
    {
        return super.getOldValue();
    }


    @Override
    public String getNewValue()
    {
        return super.getNewValue();
    }
}

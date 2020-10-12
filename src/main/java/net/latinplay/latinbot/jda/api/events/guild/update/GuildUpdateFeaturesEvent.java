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

import java.util.Set;

/**
 * Indicates that the features of a {@link Guild Guild} changed.
 *
 * <p>Can be used to detect when the features change and retrieve the old ones
 *
 * <p>Identifier: {@code features}
 */
public class GuildUpdateFeaturesEvent extends GenericGuildUpdateEvent<Set<String>>
{
    public static final String IDENTIFIER = "features";

    public GuildUpdateFeaturesEvent( JDA api, long responseNumber,  Guild guild,  Set<String> oldFeatures)
    {
        super(api, responseNumber, guild, oldFeatures, guild.getFeatures(), IDENTIFIER);
    }

    /**
     * The old Set of features before the {@link Guild Guild} update.
     *
     * @return Never-null, unmodifiable Set of the old features
     */

    public Set<String> getOldFeatures()
    {
        return getOldValue();
    }

    /**
     * The new Set of features after the {@link Guild Guild} update.
     *
     * @return Never-null, unmodifiable Set of the new features
     */

    public Set<String> getNewFeatures()
    {
        return getNewValue();
    }


    @Override
    public Set<String> getOldValue()
    {
        return super.getOldValue();
    }


    @Override
    public Set<String> getNewValue()
    {
        return super.getNewValue();
    }
}

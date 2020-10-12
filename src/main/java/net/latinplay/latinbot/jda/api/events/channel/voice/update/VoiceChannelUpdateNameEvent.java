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
package net.latinplay.latinbot.jda.api.events.channel.voice.update;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.VoiceChannel;

/**
 * Indicates that a {@link VoiceChannel VoiceChannel}'s name changed.
 *
 * <p>Can be used to get affected VoiceChannel, affected Guild and previous name.
 *
 * <p>Identifier: {@code name}
 */
public class VoiceChannelUpdateNameEvent extends GenericVoiceChannelUpdateEvent<String>
{
    public static final String IDENTIFIER = "name";

    public VoiceChannelUpdateNameEvent( JDA api, long responseNumber,  VoiceChannel channel,  String oldName)
    {
        super(api, responseNumber, channel, oldName, channel.getName(), IDENTIFIER);
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
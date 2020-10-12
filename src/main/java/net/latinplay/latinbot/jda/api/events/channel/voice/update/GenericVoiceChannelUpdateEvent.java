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
import net.latinplay.latinbot.jda.api.events.UpdateEvent;
import net.latinplay.latinbot.jda.api.events.channel.voice.GenericVoiceChannelEvent;

/**
 * Indicates that a {@link VoiceChannel VoiceChannel} updated.
 * <br>Every VoiceChannelUpdateEvent is derived from this event and can be casted.
 *
 * <p>Can be used to detect any VoiceChannelUpdateEvent.
 */
public abstract class GenericVoiceChannelUpdateEvent<T> extends GenericVoiceChannelEvent implements UpdateEvent<VoiceChannel, T>
{
    private final String identifier;
    private final T prev;
    private final T next;

    public GenericVoiceChannelUpdateEvent(
             JDA api, long responseNumber,  VoiceChannel channel,
             T prev,  T next,  String identifier)
    {
        super(api, responseNumber, channel);
        this.prev = prev;
        this.next = next;
        this.identifier = identifier;
    }

    
    @Override
    public VoiceChannel getEntity()
    {
        return getChannel();
    }

    
    @Override
    public String getPropertyIdentifier()
    {
        return identifier;
    }

    
    @Override
    public T getOldValue()
    {
        return prev;
    }

    
    @Override
    public T getNewValue()
    {
        return next;
    }

    @Override
    public String toString()
    {
        return "VoiceChannelUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
    }
}

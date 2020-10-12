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

package net.latinplay.latinbot.jda.api.events.emote.update;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Emote;
import net.latinplay.latinbot.jda.api.events.UpdateEvent;
import net.latinplay.latinbot.jda.api.events.emote.GenericEmoteEvent;

/**
 * Indicates that an {@link Emote Emote} was updated.
 */
public abstract class GenericEmoteUpdateEvent<T> extends GenericEmoteEvent implements UpdateEvent<Emote, T>
{
    protected final T previous;
    protected final T next;
    protected final String identifier;

    public GenericEmoteUpdateEvent(
             JDA api, long responseNumber,  Emote emote,
             T previous,  T next,  String identifier)
    {
        super(api, responseNumber, emote);
        this.previous = previous;
        this.next = next;
        this.identifier = identifier;
    }


    @Override
    public Emote getEntity()
    {
        return getEmote();
    }


    @Override
    public String getPropertyIdentifier()
    {
        return identifier;
    }


    @Override
    public T getOldValue()
    {
        return previous;
    }


    @Override
    public T getNewValue()
    {
        return next;
    }

    @Override
    public String toString()
    {
        return "EmoteUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
    }
}

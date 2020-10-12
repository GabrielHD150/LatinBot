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

package net.latinplay.latinbot.jda.api.events.self;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.SelfUser;
import net.latinplay.latinbot.jda.api.events.Event;
import net.latinplay.latinbot.jda.api.events.UpdateEvent;

/**
 * Indicates that a {@link SelfUser SelfUser} changed or started an activity.
 * <br>Every SelfUserEvent is derived from this event and can be casted.
 *
 * <p>Can be used to detect any SelfUserEvent.
 */
public abstract class GenericSelfUpdateEvent<T> extends Event implements UpdateEvent<SelfUser, T>
{
    protected final T previous;
    protected final T next;
    protected final String identifier;

    public GenericSelfUpdateEvent(
             JDA api, long responseNumber,
             T previous,  T next,  String identifier)
    {
        super(api, responseNumber);
        this.previous = previous;
        this.next = next;
        this.identifier = identifier;
    }

    /**
     * The {@link SelfUser SelfUser}
     *
     * @return The {@link SelfUser SelfUser}
     */

    public SelfUser getSelfUser()
    {
        return api.getSelfUser();
    }


    @Override
    public SelfUser getEntity()
    {
        return getSelfUser();
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
        return "SelfUserUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
    }
}

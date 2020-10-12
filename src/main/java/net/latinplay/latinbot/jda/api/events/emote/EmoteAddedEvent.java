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

package net.latinplay.latinbot.jda.api.events.emote;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Emote;
import net.latinplay.latinbot.jda.api.entities.Guild;

/**
 * Indicates that a new {@link Emote Emote} was added to a {@link Guild Guild}.
 */
public class EmoteAddedEvent extends GenericEmoteEvent
{
    public EmoteAddedEvent( JDA api, long responseNumber,  Emote emote)
    {
        super(api, responseNumber, emote);
    }
}

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
package net.latinplay.latinbot.jda.api.events.message.priv;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.MessageEmbed;
import net.latinplay.latinbot.jda.api.entities.PrivateChannel;

import java.util.List;

/**
 * Indicates that a Message contains {@link MessageEmbed Embeds} in a {@link PrivateChannel PrivateChannel}.
 * 
 * <p>Can be used to retrieve affected PrivateChannel, the ID of the deleted Message and a list of MessageEmbeds.
 */
public class PrivateMessageEmbedEvent extends GenericPrivateMessageEvent
{
    private final List<MessageEmbed> embeds;

    public PrivateMessageEmbedEvent( JDA api, long responseNumber, long messageId,  PrivateChannel channel,  List<MessageEmbed> embeds)
    {
        super(api, responseNumber, messageId, channel);
        this.embeds = embeds;
    }

    /**
     * The {@link MessageEmbed MessageEmbeds}
     *
     * @return The MessageEmbeds
     */

    public List<MessageEmbed> getMessageEmbeds()
    {
        return embeds;
    }
}

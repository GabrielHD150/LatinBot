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
package net.latinplay.latinbot.jda.api.events.message.guild;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.events.guild.GenericGuildEvent;

/**
 * Indicates that a {@link Message Message} event is fired from a {@link TextChannel TextChannel}.
 * <br>Every GuildMessageEvent is derived from this event and can be casted.
 * 
 * <p>Can be used to detect any GuildMessageEvent.
 */
public abstract class GenericGuildMessageEvent extends GenericGuildEvent
{
    protected final long messageId;
    protected final TextChannel channel;

    public GenericGuildMessageEvent( JDA api, long responseNumber, long messageId,  TextChannel channel)
    {
        super(api, responseNumber, channel.getGuild());
        this.messageId = messageId;
        this.channel = channel;
    }

    /**
     * The message id
     *
     * @return The message id
     */
    
    public String getMessageId()
    {
        return Long.toUnsignedString(messageId);
    }

    /**
     * The message id
     *
     * @return The message id
     */
    public long getMessageIdLong()
    {
        return messageId;
    }

    /**
     * The {@link TextChannel TextChannel} for this message
     *
     * @return The TextChannel for this message
     */
    
    public TextChannel getChannel()
    {
        return channel;
    }
}

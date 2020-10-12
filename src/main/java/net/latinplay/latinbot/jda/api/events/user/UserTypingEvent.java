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
package net.latinplay.latinbot.jda.api.events.user;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.entities.*;

import java.time.OffsetDateTime;

/**
 * Indicates that a {@link User User} started typing. (Similar to the typing indicator in the Discord client)
 * <br>This event requires {@link JDABuilder#setGuildSubscriptionsEnabled(boolean) guild subscriptions}
 * to be enabled.
 *
 * <p>Can be used to retrieve the User who started typing and when and in which MessageChannel they started typing.
 */
public class UserTypingEvent extends GenericUserEvent
{
    private final MessageChannel channel;
    private final OffsetDateTime timestamp;

    public UserTypingEvent( JDA api, long responseNumber,  User user,  MessageChannel channel,  OffsetDateTime timestamp)
    {
        super(api, responseNumber, user);
        this.channel = channel;
        this.timestamp = timestamp;
    }

    /**
     * The time when the user started typing
     *
     * @return The time when the typing started
     */

    public OffsetDateTime getTimestamp()
    {
        return timestamp;
    }

    /**
     * The channel where the typing was started
     *
     * @return The channel
     */

    public MessageChannel getChannel()
    {
        return channel;
    }

    /**
     * Whether the user started typing in a channel of the specified type.
     *
     * @param  type
     *         {@link ChannelType ChannelType}
     *
     * @return True, if the user started typing in a channel of the specified type
     */
    public boolean isFromType( ChannelType type)
    {
        return channel.getType() == type;
    }

    /**
     * The {@link ChannelType ChannelType}
     *
     * @return The {@link ChannelType ChannelType}
     */

    public ChannelType getType()
    {
        return channel.getType();
    }

    /**
     * {@link PrivateChannel PrivateChannel} in which this users started typing,
     * or {@code null} if this was not in a PrivateChannel.
     *
     * @return Possibly-null {@link PrivateChannel PrivateChannel}
     */

    public PrivateChannel getPrivateChannel()
    {
        return isFromType(ChannelType.PRIVATE) ? (PrivateChannel) channel : null;
    }

    /**
     * {@link TextChannel TextChannel} in which this users started typing,
     * or {@code null} if this was not in a TextChannel.
     *
     * @return Possibly-null {@link TextChannel TextChannel}
     */

    public TextChannel getTextChannel()
    {
        return isFromType(ChannelType.TEXT) ? (TextChannel) channel : null;
    }

    /**
     * {@link Guild Guild} in which this users started typing,
     * or {@code null} if this was not in a Guild.
     *
     * @return Possibly-null {@link Guild Guild}
     */

    public Guild getGuild()
    {
        return isFromType(ChannelType.TEXT) ? getTextChannel().getGuild() : null;
    }

    /**
     * {@link Member Member} instance for the User, or null if this was not in a Guild.
     *
     * @return Possibly-null {@link Member Member}
     */

    public Member getMember()
    {
        return isFromType(ChannelType.TEXT) ? getGuild().getMember(getUser()) : null;
    }
}

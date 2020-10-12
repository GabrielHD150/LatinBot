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

package net.latinplay.latinbot.jda.api.events.message.priv.react;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.MessageReaction;
import net.latinplay.latinbot.jda.api.entities.PrivateChannel;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.events.message.priv.GenericPrivateMessageEvent;

/**
 * Indicates that a {@link MessageReaction MessageReaction} was added or removed.
 *
 * <p>Can be used to detect when a message reaction is added or removed from a message.
 */
public class GenericPrivateMessageReactionEvent extends GenericPrivateMessageEvent
{
    protected final long userId;
    protected final User issuer;
    protected final MessageReaction reaction;

    public GenericPrivateMessageReactionEvent( JDA api, long responseNumber,  User user,  MessageReaction reaction, long userId)
    {
        super(api, responseNumber, reaction.getMessageIdLong(), (PrivateChannel) reaction.getChannel());
        this.userId = userId;
        this.issuer = user;
        this.reaction = reaction;
    }

    /**
     * The id for the user who added/removed their reaction.
     *
     * @return The user id
     */

    public String getUserId()
    {
        return Long.toUnsignedString(userId);
    }

    /**
     * The id for the user who added/removed their reaction.
     *
     * @return The user id
     */
    public long getUserIdLong()
    {
        return userId;
    }

    /**
     * The reacting {@link User User}
     * <br>This might be missing if the user was not cached.
     *
     * @return The reacting user
     */

    public User getUser()
    {
        return issuer;
    }

    /**
     * The {@link MessageReaction MessageReaction}
     *
     * @return The message reaction
     */

    public MessageReaction getReaction()
    {
        return reaction;
    }

    /**
     * The {@link MessageReaction.ReactionEmote ReactionEmote}
     * <br>Shortcut for {@code getReaction().getReactionEmote()}
     *
     * @return The message reaction emote
     */

    public MessageReaction.ReactionEmote getReactionEmote()
    {
        return reaction.getReactionEmote();
    }
}

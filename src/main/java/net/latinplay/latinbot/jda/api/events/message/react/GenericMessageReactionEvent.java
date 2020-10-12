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

package net.latinplay.latinbot.jda.api.events.message.react;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.MessageReaction;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.events.message.GenericMessageEvent;

/**
 * Indicates that a MessageReaction was added/removed.
 * <br>Every MessageReactionEvent is derived from this event and can be casted.
 *
 * <p>Can be used to detect both remove and add events.
 */
public class GenericMessageReactionEvent extends GenericMessageEvent
{
    protected final long userId;
    protected User issuer;
    protected Member member;
    protected MessageReaction reaction;

    public GenericMessageReactionEvent( JDA api, long responseNumber,  User user,
                                        Member member,  MessageReaction reaction, long userId)
    {
        super(api, responseNumber, reaction.getMessageIdLong(), reaction.getChannel());
        this.userId = userId;
        this.issuer = user;
        this.member = member;
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
     * @return The reacting user or null if this information is missing
     */

    public User getUser()
    {
        return issuer;
    }

    /**
     * The {@link Member Member} instance for the reacting user
     * or {@code null} if the reaction was from a user not in this guild.
     *
     * @throws java.lang.IllegalStateException
     *         If this was not sent in a {@link TextChannel}.
     *
     * @return Member of the reacting user or null if they are no longer member of this guild
     *
     * @see    #isFromGuild()
     * @see    #getChannelType()
     */

    public Member getMember()
    {
        return member;
    }

    /**
     * The {@link MessageReaction MessageReaction}
     *
     * @return The MessageReaction
     */

    public MessageReaction getReaction()
    {
        return reaction;
    }

    /**
     * The {@link MessageReaction.ReactionEmote ReactionEmote}
     * of the reaction, shortcut for {@code getReaction().getReactionEmote()}
     *
     * @return The ReactionEmote instance
     */

    public MessageReaction.ReactionEmote getReactionEmote()
    {
        return reaction.getReactionEmote();
    }
}

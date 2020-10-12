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

package net.latinplay.latinbot.jda.api.events.message.guild.react;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.MessageReaction;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.events.message.guild.GenericGuildMessageEvent;

/**
 * Indicates that a {@link MessageReaction MessageReaction} was added or removed in a TextChannel.
 *
 * <p>Can be used to detect when a reaction is added or removed in a TextChannel.
 */
public abstract class GenericGuildMessageReactionEvent extends GenericGuildMessageEvent
{
    protected final long userId;
    protected final Member issuer;
    protected final MessageReaction reaction;

    public GenericGuildMessageReactionEvent( JDA api, long responseNumber,  Member user,  MessageReaction reaction, long userId)
    {
        super(api, responseNumber, reaction.getMessageIdLong(), (TextChannel) reaction.getChannel());
        this.issuer = user;
        this.reaction = reaction;
        this.userId = userId;
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
     * <br>This might be missing if the user was not previously cached or the member was removed.
     *
     * @return The reacting user or null if this information is missing
     *
     * @see    #getUserIdLong()
     */
    
    public User getUser()
    {
        return issuer == null ? getJDA().getUserById(userId) : issuer.getUser();
    }

    /**
     * The {@link Member Member} instance for the reacting user
     * <br>This might be missing if the user was not previously cached or the member was removed.
     *
     * @return The member instance for the reacting user or null if this information is missing
     */
    
    public Member getMember()
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
     * @return The reaction emote
     */
    
    public MessageReaction.ReactionEmote getReactionEmote()
    {
        return reaction.getReactionEmote();
    }
}

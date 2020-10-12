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
import net.latinplay.latinbot.jda.api.entities.User;

/**
 * Indicates that a {@link MessageReaction MessageReaction} was added to a Message in a Guild
 *
 * <p>Can be used to detect when a reaction is added in a guild
 */
public class GuildMessageReactionAddEvent extends GenericGuildMessageReactionEvent
{
    public GuildMessageReactionAddEvent( JDA api, long responseNumber,  Member member,  MessageReaction reaction)
    {
        super(api, responseNumber, member, reaction, member.getIdLong());
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    public User getUser()
    {
        return super.getUser();
    }


    @Override
    @SuppressWarnings("ConstantConditions")
    public Member getMember()
    {
        return super.getMember();
    }
}

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
package net.latinplay.latinbot.jda.api.events.guild;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.events.guild.member.GuildMemberLeaveEvent;

/**
 * Indicates that a {@link User User} was banned from a {@link Guild Guild}.
 *
 * <p>Can be used to retrieve the user who was banned (if available) and triggering guild.
 * <br><b>Note</b>: This does not directly indicate that a Member is removed from the Guild!
 *
 * @see GuildMemberLeaveEvent GuildMemberLeaveEvent
 */
public class GuildBanEvent extends GenericGuildEvent
{
    private final User user;

    public GuildBanEvent( JDA api, long responseNumber,  Guild guild,  User user)
    {
        super(api, responseNumber, guild);
        this.user = user;
    }

    /**
     * The banned {@link User User}
     * <br>Possibly fake user.
     *
     * @return The banned user
     */
    
    public User getUser()
    {
        return user;
    }
}

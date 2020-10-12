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

package net.latinplay.latinbot.jda.api.events.user.update;

import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.entities.Activity;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.internal.JDAImpl;

import java.util.List;

/**
 * Indicates that the {@link Activity Activity} order of a {@link User User} changes.
 * <br>As with any presence updates this happened for a {@link Member Member} in a Guild!
 * <br>This event requires {@link JDABuilder#setGuildSubscriptionsEnabled(boolean) guild subscriptions} to be enabled.
 *
 * <p>Can be used to retrieve the User who changed their Activities and their previous Activities.
 *
 * <p>Identifier: {@code activity_order}
 */
public class UserUpdateActivityOrderEvent extends GenericUserUpdateEvent<List<Activity>> implements GenericUserPresenceEvent
{
    public static final String IDENTIFIER = "activity_order";

    private final Member member;

    public UserUpdateActivityOrderEvent( JDAImpl api, long responseNumber,  List<Activity> previous,  Member member)
    {
        super(api, responseNumber, member.getUser(), previous, member.getActivities(), IDENTIFIER);
        this.member = member;
    }


    @Override
    public Guild getGuild()
    {
        return member.getGuild();
    }


    @Override
    public Member getMember()
    {
        return member;
    }


    @Override
    public List<Activity> getOldValue()
    {
        return super.getOldValue();
    }


    @Override
    public List<Activity> getNewValue()
    {
        return super.getNewValue();
    }
}

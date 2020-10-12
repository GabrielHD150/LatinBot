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

package net.latinplay.latinbot.jda.api.entities;

import net.latinplay.latinbot.jda.api.utils.MiscUtil;
import net.latinplay.latinbot.jda.internal.utils.Checks;

import java.util.List;

/**
 * Meta-data for the team of an application.
 *
 * @see ApplicationInfo#getTeam()
 */
public interface ApplicationTeam extends ISnowflake
{
    /** Template for {@link #getIconUrl()} */
    String ICON_URL = "https://cdn.discordapp.com/team-icons/%s/%s.png";

    /**
     * Searches for the {@link TeamMember TeamMember}
     * in {@link #getMembers()} that has the same user id as {@link #getOwnerIdLong()}.
     * <br>Its possible although unlikely that the owner of the team is not a member, in that case this will be null.
     *
     * @return Possibly-null {@link TeamMember TeamMember} who owns the team
     */
    
    default TeamMember getOwner()
    {
        return getMemberById(getOwnerIdLong());
    }

    /**
     * The id for the user who owns this team.
     *
     * @return The owner id
     */
    
    default String getOwnerId()
    {
        return Long.toUnsignedString(getOwnerIdLong());
    }

    /**
     * The id for the user who owns this team.
     *
     * @return The owner id
     */
    long getOwnerIdLong();

    /**
     * The id hash for the icon of this team.
     *
     * @return The icon id, or null if no icon is applied
     *
     * @see    #getIconUrl()
     */
    
    String getIconId();

    /**
     * The url for the icon of this team.
     *
     * @return The icon url, or null if no icon is applied
     */
    
    default String getIconUrl()
    {
        String iconId = getIconId();
        return iconId == null ? null : String.format(ICON_URL, getId(), iconId);
    }

    /**
     * The {@link TeamMember Team Members}.
     *
     * @return Immutable list of team members
     */
    
    List<TeamMember> getMembers();

    /**
     * Check whether {@link #getMember(User)} returns null for the provided user.
     *
     * @param  user
     *         The user to check
     *
     * @throws java.lang.IllegalArgumentException
     *         If provided with null
     *
     * @return True, if the provided user is a member of this team
     */
    default boolean isMember( User user)
    {
        return getMember(user) != null;
    }

    /**
     * Retrieves the {@link TeamMember TeamMember} instance
     * for the provided user. If the user is not a member of this team, null is returned.
     *
     * @param  user
     *         The user for the team member
     *
     * @throws java.lang.IllegalArgumentException
     *         If provided with null
     *
     * @return The {@link TeamMember TeamMember} for the user or null
     */
    
    default TeamMember getMember( User user)
    {
        Checks.notNull(user, "User");
        return getMemberById(user.getIdLong());
    }

    /**
     * Retrieves the {@link TeamMember TeamMember} instance
     * for the provided user id. If the user is not a member of this team, null is returned.
     *
     * @param  userId
     *         The user id for the team member
     *
     * @throws java.lang.IllegalArgumentException
     *         If provided with null
     *
     * @return The {@link TeamMember TeamMember} for the user or null
     */
    
    default TeamMember getMemberById( String userId)
    {
        return getMemberById(MiscUtil.parseSnowflake(userId));
    }

    /**
     * Retrieves the {@link TeamMember TeamMember} instance
     * for the provided user id. If the user is not a member of this team, null is returned.
     *
     * @param  userId
     *         The user id for the team member
     *
     * @return The {@link TeamMember TeamMember} for the user or null
     */
    
    default TeamMember getMemberById(long userId)
    {
        for (TeamMember member : getMembers())
        {
            if (member.getUser().getIdLong() == userId)
                return member;
        }
        return null;
    }
}

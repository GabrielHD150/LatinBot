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

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.OnlineStatus;
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.latinplay.latinbot.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.latinplay.latinbot.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.latinplay.latinbot.jda.api.exceptions.HierarchyException;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.requests.ErrorResponse;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.AuditableRestAction;
import net.latinplay.latinbot.jda.api.utils.cache.CacheFlag;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

/**
 * Represents a Guild-specific User.
 *
 * <p>Contains all guild-specific information about a User. (Roles, Nickname, VoiceStatus etc.)
 *
 * @since 3.0
 *
 * @see   Guild#getMember(User)
 * @see   Guild#getMemberCache()
 * @see   Guild#getMemberById(long)
 * @see   Guild#getMemberByTag(String)
 * @see   Guild#getMemberByTag(String, String)
 * @see   Guild#getMembersByEffectiveName(String, boolean)
 * @see   Guild#getMembersByName(String, boolean)
 * @see   Guild#getMembersByNickname(String, boolean)
 * @see   Guild#getMembersWithRoles(Role...)
 * @see   Guild#getMembers()
 */
public interface Member extends IMentionable, IPermissionHolder, IFakeable
{
    /**
     * The user wrapped by this Entity.
     *
     * @return {@link User User}
     */
    
    User getUser();

    /**
     * The Guild in which this Member is represented.
     *
     * @return {@link Guild Guild}
     */
    
    Guild getGuild();

    /**
     * The JDA instance.
     *
     * @return The current JDA instance.
     */
    
    JDA getJDA();

    /**
     * The {@link java.time.OffsetDateTime Time} this Member joined the Guild.
     * <br>If the member was loaded through a presence update (lazy loading) this will be identical
     * to the creation time of the guild.
     *
     * @return The Join Date.
     */
    
    OffsetDateTime getTimeJoined();

    /**
     * The time when this member boosted the guild.
     * <br>Null indicates this member is not currently boosting the guild.
     *
     * @return The boosting time, or null if the member is not boosting
     *
     * @since  4.0.0
     */
    
    OffsetDateTime getTimeBoosted();

    /**
     * The {@link GuildVoiceState VoiceState} of this Member.
     * <br><b>This will be null when the {@link CacheFlag#VOICE_STATE} is disabled manually</b>
     *
     * <p>This can be used to get the Member's VoiceChannel using {@link GuildVoiceState#getChannel()}.
     *
     * @return {@link GuildVoiceState GuildVoiceState}
     */
    
    GuildVoiceState getVoiceState();

    /**
     * The activities of the user.
     * <br>If the user does not currently have any activity, this returns an empty list.
     *
     * @return Immutable list of {@link Activity Activities} for the user
     */
    
    List<Activity> getActivities();

    /**
     * Returns the {@link OnlineStatus OnlineStatus} of the User.
     * <br>If the {@link OnlineStatus OnlineStatus} is unrecognized, will return {@link OnlineStatus#UNKNOWN UNKNOWN}.
     *
     * @return The current {@link OnlineStatus OnlineStatus} of the {@link User User}.
     */
    
    OnlineStatus getOnlineStatus();

    /**
     * The platform dependent {@link OnlineStatus} of this member.
     * <br>Since a user can be connected from multiple different devices such as web and mobile,
     * discord specifies a status for each {@link ClientType}.
     *
     * <p>If a user is not online on the specified type,
     * {@link OnlineStatus#OFFLINE OFFLINE} is returned.
     *
     * @param  type
     *         The type of client
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided type is null
     *
     * @return The status for that specific client or OFFLINE
     *
     * @since  4.0.0
     */
    
    OnlineStatus getOnlineStatus( ClientType type);

    /**
     * A Set of all active {@link ClientType ClientTypes} of this Member.
     * Every {@link OnlineStatus OnlineStatus} other than {@code OFFLINE} and {@code UNKNOWN}
     * is interpreted as active.
     * Since {@code INVISIBLE} is only possible for the SelfUser, other Members will never have ClientTypes show as
     * active when actually being {@code INVISIBLE}, since they will show as {@code OFFLINE}.
     * <br>If the Member is currently not active with any Client, this returns an empty Set.
     * <br>When {@link CacheFlag#CLIENT_STATUS CacheFlag.CLIENT_STATUS} is disabled,
     * active clients will not be tracked and this will always return an empty Set.
     * <br>Since a user can be connected from multiple different devices such as web and mobile,
     * discord specifies a status for each {@link ClientType}.
     *
     * @return EnumSet of all active {@link ClientType ClientTypes}
     *
     * @since  4.0.0
     */
    
    EnumSet<ClientType> getActiveClients();

    /**
     * Returns the current nickname of this Member for the parent Guild.
     *
     * <p>This can be changed using
     * {@link Guild#modifyNickname(Member, String) modifyNickname(Member, String)}.
     *
     * @return The nickname or null, if no nickname is set.
     */
    
    String getNickname();

    /**
     * Retrieves the Name displayed in the official Discord Client.
     *
     * @return The Nickname of this Member or the Username if no Nickname is present.
     */
    
    String getEffectiveName();

    /**
     * The roles applied to this Member.
     * <br>The roles are ordered based on their position. The highest role being at index 0
     * and the lowest at the last index.
     *
     * <p>A Member's roles can be changed using the <b>addRolesToMember</b>, <b>removeRolesFromMember</b>, and <b>modifyMemberRoles</b>
     * methods in {@link Guild Guild}.
     *
     * <p><b>The Public Role ({@code @everyone}) is not included in the returned immutable list of roles
     * <br>It is implicit that every member holds the Public Role in a Guild thus it is not listed here!</b>
     *
     * @return An immutable List of {@link Role Roles} for this Member.
     */
    
    List<Role> getRoles();

    /**
     * The {@link java.awt.Color Color} of this Member's name in a Guild.
     *
     * <p>This is determined by the color of the highest role assigned to them that does not have the default color.
     * <br>If all roles have default color, this returns null.
     *
     * @return The display Color for this Member.
     *
     * @see    #getColorRaw()
     */
    
    Color getColor();

    /**
     * The raw RGB value for the color of this member.
     * <br>Defaulting to {@link Role#DEFAULT_COLOR_RAW Role.DEFAULT_COLOR_RAW}
     * if this member uses the default color (special property, it changes depending on theme used in the client)
     *
     * @return The raw RGB value or the role default
     */
    int getColorRaw();

    /**
     * Whether this Member can interact with the provided Member
     * (kick/ban/etc.)
     *
     * @param  member
     *         The target Member to check
     *
     * @throws NullPointerException
     *         if the specified Member is null
     * @throws IllegalArgumentException
     *         if the specified Member is not from the same guild
     *
     * @return True, if this Member is able to interact with the specified Member
     */
    boolean canInteract( Member member);

    /**
     * Whether this Member can interact with the provided {@link Role Role}
     * (kick/ban/move/modify/delete/etc.)
     *
     * <p>If this returns true this member can assign the role to other members.
     *
     * @param  role
     *         The target Role to check
     *
     * @throws NullPointerException
     *         if the specified Role is null
     * @throws IllegalArgumentException
     *         if the specified Role is not from the same guild
     *
     * @return True, if this member is able to interact with the specified Role
     */
    boolean canInteract( Role role);

    /**
     * Whether this Member can interact with the provided {@link Emote Emote}
     * (use in a message)
     *
     * @param  emote
     *         The target Emote to check
     *
     * @throws NullPointerException
     *         if the specified Emote is null
     * @throws IllegalArgumentException
     *         if the specified Emote is not from the same guild
     *
     * @return True, if this Member is able to interact with the specified Emote
     */
    boolean canInteract( Emote emote);

    /**
     * Checks whether this member is the owner of its related {@link Guild Guild}.
     *
     * @return True, if this member is the owner of the attached Guild.
     */
    boolean isOwner();

    /**
     * The default {@link TextChannel TextChannel} for a {@link Member Member}.
     * <br>This is the channel that the Discord client will default to opening when a Guild is opened for the first time
     * after joining the guild.
     * <br>The default channel is the channel with the highest position in which the member has
     * {@link Permission#MESSAGE_READ Permission.MESSAGE_READ} permissions. If this requirement doesn't apply for
     * any channel in the guild, this method returns {@code null}.
     *
     * @return The {@link TextChannel TextChannel} representing the default channel for this member
     *         or null if no such channel exists.
     */
    
    TextChannel getDefaultChannel();

    /**
     * Bans this Member and deletes messages sent by the user based on the amount of delDays.
     * <br>If you wish to ban a member without deleting any messages, provide delDays with a value of 0.
     *
     * <p>You can unban a user with {@link Guild#unban(User) Guild.unban(User)}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the
     * {@link Member Member} until Discord sends the
     * {@link GuildMemberLeaveEvent GuildMemberLeaveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be banned due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param  delDays
     *         The history of messages, in days, that will be deleted.
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     * @throws HierarchyException
     *         If the logged in account cannot ban the other user due to permission hierarchy position.
     *         <br>See {@link Member#canInteract(Member)}
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the provided amount of days (delDays) is less than 0.</li>
     *             <li>If the provided amount of days (delDays) is bigger than 7.</li>
     *             <li>If the provided member is {@code null}</li>
     *         </ul>
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> ban(int delDays)
    {
        return getGuild().ban(this, delDays);
    }

    /**
     * Bans this Member and deletes messages sent by the user based on the amount of delDays.
     * <br>If you wish to ban a member without deleting any messages, provide delDays with a value of 0.
     *
     * <p>You can unban a user with {@link Guild#unban(User) Guild.unban(User)}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the
     * {@link Member Member} until Discord sends the
     * {@link GuildMemberLeaveEvent GuildMemberLeaveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be banned due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param  delDays
     *         The history of messages, in days, that will be deleted.
     * @param  reason
     *         The reason for this action or {@code null} if there is no specified reason
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#BAN_MEMBERS} permission.
     * @throws HierarchyException
     *         If the logged in account cannot ban the other user due to permission hierarchy position.
     *         <br>See {@link Member#canInteract(Member)}
     * @throws java.lang.IllegalArgumentException
     *         <ul>
     *             <li>If the provided amount of days (delDays) is less than 0.</li>
     *             <li>If the provided amount of days (delDays) is bigger than 7.</li>
     *             <li>If the provided member is {@code null}</li>
     *         </ul>
     *
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> ban(int delDays,  String reason)
    {
        return getGuild().ban(this, delDays, reason);
    }

    /**
     * Kicks this Member from the {@link Guild Guild}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the {@link User User}
     * until Discord sends the {@link GuildMemberLeaveEvent GuildMemberLeaveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be kicked due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided member is not a Member of this Guild or is {@code null}
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#KICK_MEMBERS} permission.
     * @throws HierarchyException
     *         If the logged in account cannot kick the other member due to permission hierarchy position.
     *         <br>See {@link Member#canInteract(Member)}
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *         Kicks the provided Member from the current Guild
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> kick()
    {
        return getGuild().kick(this);
    }

    /**
     * Kicks this from the {@link Guild Guild}.
     *
     * <p><b>Note:</b> {@link Guild#getMembers()} will still contain the {@link User User}
     * until Discord sends the {@link GuildMemberLeaveEvent GuildMemberLeaveEvent}.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be kicked due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param  reason
     *         The reason for this action or {@code null} if there is no specified reason
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided member is not a Member of this Guild or is {@code null}
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#KICK_MEMBERS} permission.
     * @throws HierarchyException
     *         If the logged in account cannot kick the other member due to permission hierarchy position.
     *         <br>See {@link Member#canInteract(Member)}
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *         Kicks the provided Member from the current Guild
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> kick( String reason)
    {
        return getGuild().kick(this, reason);
    }

    /**
     * Sets the Guild Muted state state of this Member based on the provided
     * boolean.
     *
     * <p><b>Note:</b> The Member's {@link GuildVoiceState#isGuildMuted() GuildVoiceState.isGuildMuted()} value won't change
     * until JDA receives the {@link GuildVoiceGuildMuteEvent GuildVoiceGuildMuteEvent} event related to this change.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be muted due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#USER_NOT_CONNECTED USER_NOT_CONNECTED}
     *     <br>The specified Member is not connected to a voice channel</li>
     * </ul>
     *
     * @param  mute
     *         Whether this {@link Member Member} should be muted or unmuted.
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#VOICE_DEAF_OTHERS} permission.
     * @throws java.lang.IllegalArgumentException
     *         If the provided member is not from this Guild or null.
     * @throws java.lang.IllegalStateException
     *         If the provided member is not currently connected to a voice channel.
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> mute(boolean mute)
    {
        return getGuild().mute(this, mute);
    }

    /**
     * Sets the Guild Deafened state state of this Member based on the provided boolean.
     *
     * <p><b>Note:</b> The Member's {@link GuildVoiceState#isGuildDeafened() GuildVoiceState.isGuildDeafened()} value won't change
     * until JDA receives the {@link GuildVoiceGuildDeafenEvent GuildVoiceGuildDeafenEvent} event related to this change.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The target Member cannot be deafened due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     *
     *     <li>{@link ErrorResponse#USER_NOT_CONNECTED USER_NOT_CONNECTED}
     *     <br>The specified Member is not connected to a voice channel</li>
     * </ul>
     *
     * @param  deafen
     *         Whether this {@link Member Member} should be deafened or undeafened.
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#VOICE_DEAF_OTHERS} permission.
     * @throws IllegalArgumentException
     *         If the provided member is not from this Guild or null.
     * @throws java.lang.IllegalStateException
     *         If the provided member is not currently connected to a voice channel.
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> deafen(boolean deafen)
    {
        return getGuild().deafen(this, deafen);
    }

    /**
     * Changes this Member's nickname in this guild.
     * The nickname is visible to all members of this guild.
     *
     * <p>To change the nickname for the currently logged in account
     * only the Permission {@link Permission#NICKNAME_CHANGE NICKNAME_CHANGE} is required.
     * <br>To change the nickname of <b>any</b> {@link Member Member} for this {@link Guild Guild}
     * the Permission {@link Permission#NICKNAME_MANAGE NICKNAME_MANAGE} is required.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The nickname of the target Member is not modifiable due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#UNKNOWN_MEMBER UNKNOWN_MEMBER}
     *     <br>The specified Member was removed from the Guild before finishing the task</li>
     * </ul>
     *
     * @param  nickname
     *         The new nickname of the {@link Member Member}, provide {@code null} or an
     *         empty String to reset the nickname
     *
     * @throws IllegalArgumentException
     *         If the specified {@link Member Member}
     *         is not from the same {@link Guild Guild}.
     *         Or if the provided member is {@code null}
     * @throws InsufficientPermissionException
     *         <ul>
     *             <li>If attempting to set nickname for self and the logged in account has neither {@link Permission#NICKNAME_CHANGE}
     *                 or {@link Permission#NICKNAME_MANAGE}</li>
     *             <li>If attempting to set nickname for another member and the logged in account does not have {@link Permission#NICKNAME_MANAGE}</li>
     *         </ul>
     * @throws HierarchyException
     *         If attempting to set nickname for another member and the logged in account cannot manipulate the other user due to permission hierarchy position.
     *         <br>See {@link #canInteract(Member)}.
     *
     * @return {@link AuditableRestAction AuditableRestAction}
     *
     * @since  4.0.0
     */
    
    
    default AuditableRestAction<Void> modifyNickname( String nickname)
    {
        return getGuild().modifyNickname(this, nickname);
    }
}

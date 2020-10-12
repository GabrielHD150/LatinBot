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

package net.latinplay.latinbot.jda.internal.entities;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.OnlineStatus;
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.*;
import net.latinplay.latinbot.jda.api.utils.cache.CacheFlag;
import net.latinplay.latinbot.jda.internal.JDAImpl;
import net.latinplay.latinbot.jda.internal.utils.Checks;
import net.latinplay.latinbot.jda.internal.utils.PermissionUtil;
import net.latinplay.latinbot.jda.internal.utils.cache.SnowflakeReference;

import java.awt.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MemberImpl implements Member
{
    private static final ZoneOffset OFFSET = ZoneOffset.of("+00:00");
    private final SnowflakeReference<Guild> guild;
    private final User user;
    private final JDAImpl api;
    private final Set<Role> roles = ConcurrentHashMap.newKeySet();
    private final GuildVoiceState voiceState;
    private final Map<ClientType, OnlineStatus> clientStatus;

    private String nickname;
    private long joinDate, boostDate;
    private List<Activity> activities = null;
    private OnlineStatus onlineStatus = OnlineStatus.OFFLINE;

    public MemberImpl(GuildImpl guild, User user)
    {
        this.api = (JDAImpl) user.getJDA();
        this.guild = new SnowflakeReference<>(guild, api::getGuildById);
        this.user = user;
        boolean cacheState = api.isCacheFlagSet(CacheFlag.VOICE_STATE) || user.equals(api.getSelfUser());
        boolean cacheOnline = api.isCacheFlagSet(CacheFlag.CLIENT_STATUS);
        this.voiceState = cacheState ? new GuildVoiceStateImpl(this) : null;
        this.clientStatus = cacheOnline ? Collections.synchronizedMap(new EnumMap<>(ClientType.class)) : null;
    }

    
    @Override
    public User getUser()
    {
        return user;
    }

    
    @Override
    public GuildImpl getGuild()
    {
        return (GuildImpl) guild.resolve();
    }

    
    @Override
    public JDA getJDA()
    {
        return api;
    }

    
    @Override
    public OffsetDateTime getTimeJoined()
    {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(joinDate), OFFSET);
    }

    
    @Override
    public OffsetDateTime getTimeBoosted()
    {
        return boostDate != 0 ? OffsetDateTime.ofInstant(Instant.ofEpochMilli(boostDate), OFFSET) : null;
    }

    @Override
    public GuildVoiceState getVoiceState()
    {
        return voiceState;
    }

    
    @Override
    public List<Activity> getActivities()
    {
        return activities == null || activities.isEmpty() ? Collections.emptyList() : activities;
    }

    
    @Override
    public OnlineStatus getOnlineStatus()
    {
        return onlineStatus;
    }

    
    @Override
    public OnlineStatus getOnlineStatus( ClientType type)
    {
        Checks.notNull(type, "Type");
        if (this.clientStatus == null || this.clientStatus.isEmpty())
            return OnlineStatus.OFFLINE;
        OnlineStatus status = this.clientStatus.get(type);
        return status == null ? OnlineStatus.OFFLINE : status;
    }

    
    @Override
    public EnumSet<ClientType> getActiveClients()
    {
        if (clientStatus == null || clientStatus.isEmpty())
            return EnumSet.noneOf(ClientType.class);
        return EnumSet.copyOf(clientStatus.keySet());
    }

    @Override
    public String getNickname()
    {
        return nickname;
    }

    
    @Override
    public String getEffectiveName()
    {
        return nickname != null ? nickname : getUser().getName();
    }

    
    @Override
    public List<Role> getRoles()
    {
        List<Role> roleList = new ArrayList<>(roles);
        roleList.sort(Comparator.reverseOrder());

        return Collections.unmodifiableList(roleList);
    }

    @Override
    public Color getColor()
    {
        final int raw = getColorRaw();
        return raw != Role.DEFAULT_COLOR_RAW ? new Color(raw) : null;
    }

    @Override
    public int getColorRaw()
    {
        for (Role r : getRoles())
        {
            final int colorRaw = r.getColorRaw();
            if (colorRaw != Role.DEFAULT_COLOR_RAW)
                return colorRaw;
        }
        return Role.DEFAULT_COLOR_RAW;
    }

    
    @Override
    public EnumSet<Permission> getPermissions()
    {
        return Permission.getPermissions(PermissionUtil.getEffectivePermission(this));
    }

    
    @Override
    public EnumSet<Permission> getPermissions( GuildChannel channel)
    {
        Checks.notNull(channel, "Channel");
        if (!getGuild().equals(channel.getGuild()))
            throw new IllegalArgumentException("Provided channel is not in the same guild as this member!");

        return Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, this));
    }

    
    @Override
    public EnumSet<Permission> getPermissionsExplicit()
    {
        return Permission.getPermissions(PermissionUtil.getExplicitPermission(this));
    }

    
    @Override
    public EnumSet<Permission> getPermissionsExplicit( GuildChannel channel)
    {
        return Permission.getPermissions(PermissionUtil.getExplicitPermission(channel, this));
    }

    @Override
    public boolean hasPermission( Permission... permissions)
    {
        return PermissionUtil.checkPermission(this, permissions);
    }

    @Override
    public boolean hasPermission( Collection<Permission> permissions)
    {
        Checks.notNull(permissions, "Permission Collection");

        return hasPermission(permissions.toArray(Permission.EMPTY_PERMISSIONS));
    }

    @Override
    public boolean hasPermission( GuildChannel channel,  Permission... permissions)
    {
        return PermissionUtil.checkPermission(channel, this, permissions);
    }

    @Override
    public boolean hasPermission( GuildChannel channel,  Collection<Permission> permissions)
    {
        Checks.notNull(permissions, "Permission Collection");

        return hasPermission(channel, permissions.toArray(Permission.EMPTY_PERMISSIONS));
    }

    @Override
    public boolean canInteract( Member member)
    {
        return PermissionUtil.canInteract(this, member);
    }

    @Override
    public boolean canInteract( Role role)
    {
        return PermissionUtil.canInteract(this, role);
    }

    @Override
    public boolean canInteract( Emote emote)
    {
        return PermissionUtil.canInteract(this, emote);
    }

    @Override
    public boolean isOwner()
    {
        return this.user.getIdLong() == getGuild().getOwnerIdLong();
    }

    @Override
    public boolean isFake()
    {
        return getGuild().getMemberById(getIdLong()) == null;
    }

    @Override
    public long getIdLong()
    {
        return user.getIdLong();
    }

    public MemberImpl setNickname(String nickname)
    {
        this.nickname = nickname;
        return this;
    }

    public MemberImpl setJoinDate(long joinDate)
    {
        this.joinDate = joinDate;
        return this;
    }

    public MemberImpl setBoostDate(long boostDate)
    {
        this.boostDate = boostDate;
        return this;
    }

    public MemberImpl setActivities(List<Activity> activities)
    {
        this.activities = Collections.unmodifiableList(activities);
        return this;
    }

    public MemberImpl setOnlineStatus(ClientType type, OnlineStatus status)
    {
        if (this.clientStatus == null || type == ClientType.UNKNOWN || type == null)
            return this;
        if (status == null || status == OnlineStatus.UNKNOWN || status == OnlineStatus.OFFLINE)
            this.clientStatus.remove(type);
        else
            this.clientStatus.put(type, status);
        return this;
    }

    public MemberImpl setOnlineStatus(OnlineStatus onlineStatus)
    {
        this.onlineStatus = onlineStatus;
        return this;
    }

    public Set<Role> getRoleSet()
    {
        return roles;
    }

    public long getBoostDateRaw()
    {
        return boostDate;
    }

    public boolean isIncomplete()
    {
        // the joined_at is only present on complete members, this implies the member is completely loaded
        return !isOwner() && Objects.equals(getGuild().getTimeCreated(), getTimeJoined());
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof MemberImpl))
            return false;

        MemberImpl oMember = (MemberImpl) o;
        return oMember.user.getIdLong() == user.getIdLong()
            && oMember.guild.getIdLong() == guild.getIdLong();
    }

    @Override
    public int hashCode()
    {
        return (guild.getIdLong() + user.getId()).hashCode();
    }

    @Override
    public String toString()
    {
        return "MB:" + getEffectiveName() + '(' + getUser().toString() + " / " + getGuild().toString() +')';
    }

    
    @Override
    public String getAsMention()
    {
        return (nickname == null ? "<@" : "<@!") + user.getId() + '>';
    }

    
    @Override
    public TextChannel getDefaultChannel()
    {
        return getGuild().getTextChannelsView().stream()
                 .sorted(Comparator.reverseOrder())
                 .filter(c -> hasPermission(c, Permission.MESSAGE_READ))
                 .findFirst().orElse(null);
    }
}

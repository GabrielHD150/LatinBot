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
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.GuildChannel;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.exceptions.HierarchyException;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.managers.RoleManager;
import net.latinplay.latinbot.jda.api.requests.restaction.AuditableRestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.RoleAction;
import net.latinplay.latinbot.jda.api.utils.MiscUtil;
import net.latinplay.latinbot.jda.internal.JDAImpl;
import net.latinplay.latinbot.jda.internal.managers.RoleManagerImpl;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.requests.restaction.AuditableRestActionImpl;
import net.latinplay.latinbot.jda.internal.utils.Checks;
import net.latinplay.latinbot.jda.internal.utils.PermissionUtil;
import net.latinplay.latinbot.jda.internal.utils.cache.SnowflakeReference;
import net.latinplay.latinbot.jda.internal.utils.cache.SortedSnowflakeCacheViewImpl;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.concurrent.locks.ReentrantLock;

public class RoleImpl implements Role
{
    private final long id;
    private final SnowflakeReference<Guild> guild;
    private final JDAImpl api;

    private final ReentrantLock mngLock = new ReentrantLock();
    private volatile RoleManager manager;

    private String name;
    private boolean managed;
    private boolean hoisted;
    private boolean mentionable;
    private long rawPermissions;
    private int color;
    private int rawPosition;

    public RoleImpl(long id, Guild guild)
    {
        this.id = id;
        this.api =(JDAImpl) guild.getJDA();
        this.guild = new SnowflakeReference<>(guild, api::getGuildById);
    }

    @Override
    public int getPosition()
    {
        Guild guild = getGuild();
        if (equals(guild.getPublicRole()))
            return -1;

        //Subtract 1 to get into 0-index, and 1 to disregard the everyone role.
        int i = guild.getRoles().size() - 2;
        for (Role r : guild.getRoles())
        {
            if (equals(r))
                return i;
            i--;
        }
        throw new AssertionError("Somehow when determining position we never found the role in the Guild's roles? wtf?");
    }

    @Override
    public int getPositionRaw()
    {
        return rawPosition;
    }


    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isManaged()
    {
        return managed;
    }

    @Override
    public boolean isHoisted()
    {
        return hoisted;
    }

    @Override
    public boolean isMentionable()
    {
        return mentionable;
    }

    @Override
    public long getPermissionsRaw()
    {
        return rawPermissions;
    }


    @Override
    public EnumSet<Permission> getPermissions()
    {
        return Permission.getPermissions(rawPermissions);
    }


    @Override
    public EnumSet<Permission> getPermissions( GuildChannel channel)
    {
        return Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, this));
    }


    @Override
    public EnumSet<Permission> getPermissionsExplicit()
    {
        return getPermissions();
    }


    @Override
    public EnumSet<Permission> getPermissionsExplicit( GuildChannel channel)
    {
        return Permission.getPermissions(PermissionUtil.getExplicitPermission(channel, this));
    }

    @Override
    public Color getColor()
    {
        return color != DEFAULT_COLOR_RAW ? new Color(color) : null;
    }

    @Override
    public int getColorRaw()
    {
        return color;
    }

    @Override
    public boolean isPublicRole()
    {
        return this.equals(this.getGuild().getPublicRole());
    }

    @Override
    public boolean hasPermission( Permission... permissions)
    {
        long effectivePerms = rawPermissions | getGuild().getPublicRole().getPermissionsRaw();
        for (Permission perm : permissions)
        {
            final long rawValue = perm.getRawValue();
            if ((effectivePerms & rawValue) != rawValue)
                return false;
        }
        return true;
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
        long effectivePerms = PermissionUtil.getEffectivePermission(channel, this);
        for (Permission perm : permissions)
        {
            final long rawValue = perm.getRawValue();
            if ((effectivePerms & rawValue) != rawValue)
                return false;
        }
        return true;
    }

    @Override
    public boolean hasPermission( GuildChannel channel,  Collection<Permission> permissions)
    {
        Checks.notNull(permissions, "Permission Collection");

        return hasPermission(channel, permissions.toArray(Permission.EMPTY_PERMISSIONS));
    }

    @Override
    public boolean canInteract( Role role)
    {
        return PermissionUtil.canInteract(this, role);
    }


    @Override
    public Guild getGuild()
    {
        return guild.resolve();
    }


    @Override
    public RoleAction createCopy( Guild guild)
    {
        Checks.notNull(guild, "Guild");
        return guild.createRole()
                    .setColor(color)
                    .setHoisted(hoisted)
                    .setMentionable(mentionable)
                    .setName(name)
                    .setPermissions(rawPermissions);
    }


    @Override
    public RoleManager getManager()
    {
        RoleManager mng = manager;
        if (mng == null)
        {
            mng = MiscUtil.locked(mngLock, () ->
            {
                if (manager == null)
                    manager = new RoleManagerImpl(this);
                return manager;
            });
        }
        return mng;
    }


    @Override
    public AuditableRestAction<Void> delete()
    {
        Guild guild = getGuild();
        if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES))
            throw new InsufficientPermissionException(guild, Permission.MANAGE_ROLES);
        if(!PermissionUtil.canInteract(guild.getSelfMember(), this))
            throw new HierarchyException("Can't delete role >= highest self-role");
        if (managed)
            throw new UnsupportedOperationException("Cannot delete a Role that is managed. ");

        Route.CompiledRoute route = Route.Roles.DELETE_ROLE.compile(guild.getId(), getId());
        return new AuditableRestActionImpl<>(getJDA(), route);
    }


    @Override
    public JDA getJDA()
    {
        return api;
    }


    @Override
    public String getAsMention()
    {
        return isPublicRole() ? "@everyone" : "<@&" + getId() + '>';
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof Role))
            return false;
        Role oRole = (Role) o;
        return this.getIdLong() == oRole.getIdLong();
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return "R:" + getName() + '(' + id + ')';
    }

    @Override
    public int compareTo( Role r)
    {
        if (this == r)
            return 0;
        if (!(r instanceof RoleImpl))
            throw new IllegalArgumentException("Cannot compare different role implementations");
        RoleImpl impl = (RoleImpl) r;

        if (this.guild.getIdLong() != impl.guild.getIdLong())
            throw new IllegalArgumentException("Cannot compare roles that aren't from the same guild!");

        if (this.getPositionRaw() != r.getPositionRaw())
            return this.getPositionRaw() - r.getPositionRaw();

        OffsetDateTime thisTime = this.getTimeCreated();
        OffsetDateTime rTime = r.getTimeCreated();

        //We compare the provided role's time to this's time instead of the reverse as one would expect due to how
        // discord deals with hierarchy. The more recent a role was created, the lower its hierarchy ranking when
        // it shares the same position as another role.
        return rTime.compareTo(thisTime);
    }

    // -- Setters --

    public RoleImpl setName(String name)
    {
        this.name = name;
        return this;
    }

    public RoleImpl setColor(int color)
    {
        this.color = color;
        return this;
    }

    public RoleImpl setManaged(boolean managed)
    {
        this.managed = managed;
        return this;
    }

    public RoleImpl setHoisted(boolean hoisted)
    {
        this.hoisted = hoisted;
        return this;
    }

    public RoleImpl setMentionable(boolean mentionable)
    {
        this.mentionable = mentionable;
        return this;
    }

    public RoleImpl setRawPermissions(long rawPermissions)
    {
        this.rawPermissions = rawPermissions;
        return this;
    }

    public RoleImpl setRawPosition(int rawPosition)
    {
        SortedSnowflakeCacheViewImpl<Role> roleCache = (SortedSnowflakeCacheViewImpl<Role>) getGuild().getRoleCache();
        roleCache.clearCachedLists();
        this.rawPosition = rawPosition;
        return this;
    }
}

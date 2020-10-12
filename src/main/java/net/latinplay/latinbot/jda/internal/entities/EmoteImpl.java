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

import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.ListedEmote;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.managers.EmoteManager;
import net.latinplay.latinbot.jda.api.requests.restaction.AuditableRestAction;
import net.latinplay.latinbot.jda.api.utils.MiscUtil;
import net.latinplay.latinbot.jda.internal.JDAImpl;
import net.latinplay.latinbot.jda.internal.managers.EmoteManagerImpl;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.requests.restaction.AuditableRestActionImpl;
import net.latinplay.latinbot.jda.internal.utils.cache.SnowflakeReference;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a Custom Emote. (Emoji in official Discord API terminology)
 *
 * @since  2.2
 */
public class EmoteImpl implements ListedEmote
{
    private final long id;
    private final SnowflakeReference<Guild> guild;
    private final JDAImpl api;
    private final Set<Role> roles;
    private final boolean fake;

    private final ReentrantLock mngLock = new ReentrantLock();
    private volatile EmoteManager manager = null;

    private boolean managed = false;
    private boolean animated = false;
    private String name;
    private User user;

    public EmoteImpl(long id, GuildImpl guild)
    {
        this(id, guild, false);
    }

    public EmoteImpl(long id, GuildImpl guild, boolean fake)
    {
        this.id = id;
        this.api = guild.getJDA();
        this.guild = new SnowflakeReference<>(guild, api::getGuildById);
        this.roles = ConcurrentHashMap.newKeySet();
        this.fake = fake;
    }

    public EmoteImpl(long id, JDAImpl api)
    {
        this.id = id;
        this.api = api;
        this.guild = null;
        this.roles = null;
        this.fake = true;
    }

    @Override
    public GuildImpl getGuild()
    {
        return guild == null ? null : (GuildImpl) guild.resolve();
    }

    
    @Override
    public List<Role> getRoles()
    {
        if (!canProvideRoles())
            throw new IllegalStateException("Unable to return roles because this emote is fake. (We do not know the origin Guild of this emote)");
        return Collections.unmodifiableList(new LinkedList<>(roles));
    }

    @Override
    public boolean canProvideRoles()
    {
        return roles != null;
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
    public boolean isFake()
    {
        return fake;
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    
    @Override
    public JDAImpl getJDA()
    {
        return api;
    }

    
    @Override
    public User getUser()
    {
        if (!hasUser())
            throw new IllegalStateException("This emote does not have a user");
        return user;
    }

    @Override
    public boolean hasUser()
    {
        return user != null;
    }

    
    @Override
    public EmoteManager getManager()
    {
        EmoteManager m = manager;
        if (m == null)
        {
            m = MiscUtil.locked(mngLock, () ->
            {
                if (manager == null)
                    manager = new EmoteManagerImpl(this);
                return manager;
            });
        }
        return m;
    }

    @Override
    public boolean isAnimated()
    {
        return animated;
    }

    
    @Override
    public AuditableRestAction<Void> delete()
    {
        if (getGuild() == null)
            throw new IllegalStateException("The emote you are trying to delete is not an actual emote we have access to (it is fake)!");
        if (managed)
            throw new UnsupportedOperationException("You cannot delete a managed emote!");
        if (!getGuild().getSelfMember().hasPermission(Permission.MANAGE_EMOTES))
            throw new InsufficientPermissionException(getGuild(), Permission.MANAGE_EMOTES);

        Route.CompiledRoute route = Route.Emotes.DELETE_EMOTE.compile(getGuild().getId(), getId());
        return new AuditableRestActionImpl<>(getJDA(), route);
    }

    // -- Setters --

    public EmoteImpl setName(String name)
    {
        this.name = name;
        return this;
    }

    public EmoteImpl setAnimated(boolean animated)
    {
        this.animated = animated;
        return this;
    }

    public EmoteImpl setManaged(boolean val)
    {
        this.managed = val;
        return this;
    }

    public EmoteImpl setUser(User user)
    {
        this.user = user;
        return this;
    }

    // -- Set Getter --

    public Set<Role> getRoleSet()
    {
        return this.roles;
    }

    // -- Object overrides --

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (!(obj instanceof EmoteImpl))
            return false;

        EmoteImpl oEmote = (EmoteImpl) obj;
        return this.id == oEmote.id && getName().equals(oEmote.getName());
    }


    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return "E:" + getName() + '(' + getIdLong() + ')';
    }

    @Override
    public EmoteImpl clone()
    {
        if (isFake()) return null;
        EmoteImpl copy = new EmoteImpl(id, getGuild()).setUser(user).setManaged(managed).setAnimated(animated).setName(name);
        copy.roles.addAll(roles);
        return copy;
    }
}

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

package net.latinplay.latinbot.jda.internal.requests.restaction.pagination;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.audit.ActionType;
import net.latinplay.latinbot.jda.api.audit.AuditLogEntry;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.exceptions.ParsingException;
import net.latinplay.latinbot.jda.api.requests.Request;
import net.latinplay.latinbot.jda.api.requests.Response;
import net.latinplay.latinbot.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.latinplay.latinbot.jda.api.utils.data.DataArray;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;
import net.latinplay.latinbot.jda.internal.entities.EntityBuilder;
import net.latinplay.latinbot.jda.internal.entities.GuildImpl;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.utils.Checks;

import java.util.ArrayList;
import java.util.List;

public class AuditLogPaginationActionImpl
    extends PaginationActionImpl<AuditLogEntry, AuditLogPaginationAction>
    implements AuditLogPaginationAction
{
    protected final Guild guild;
    // filters
    protected ActionType type = null;
    protected String userId = null;

    public AuditLogPaginationActionImpl(Guild guild)
    {
        super(guild.getJDA(), Route.Guilds.GET_AUDIT_LOGS.compile(guild.getId()), 1, 100, 100);
        if (!guild.getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS))
            throw new InsufficientPermissionException(guild, Permission.VIEW_AUDIT_LOGS);
        this.guild = guild;
    }


    @Override
    public AuditLogPaginationActionImpl type(ActionType type)
    {
        this.type = type;
        return this;
    }


    @Override
    public AuditLogPaginationActionImpl user(User user)
    {
        return user(user == null ? null : user.getId());
    }


    @Override
    public AuditLogPaginationActionImpl user(String userId)
    {
        if (userId != null)
            Checks.isSnowflake(userId, "User ID");
        this.userId = userId;
        return this;
    }


    @Override
    public AuditLogPaginationActionImpl user(long userId)
    {
        return user(Long.toUnsignedString(userId));
    }


    @Override
    public Guild getGuild()
    {
        return guild;
    }

    @Override
    protected Route.CompiledRoute finalizeRoute()
    {
        Route.CompiledRoute route = super.finalizeRoute();

        final String limit = String.valueOf(this.limit.get());
        final long last = this.lastKey;

        route = route.withQueryParams("limit", limit);

        if (type != null)
            route = route.withQueryParams("action_type", String.valueOf(type.getKey()));

        if (userId != null)
            route = route.withQueryParams("user_id", userId);

        if (last != 0)
            route = route.withQueryParams("before", Long.toUnsignedString(last));

        return route;
    }

    @Override
    protected void handleSuccess(Response response, Request<List<AuditLogEntry>> request)
    {
        DataObject obj = response.getObject();
        DataArray users = obj.getArray("users");
        DataArray webhooks = obj.getArray("webhooks");
        DataArray entries = obj.getArray("audit_log_entries");

        List<AuditLogEntry> list = new ArrayList<>(entries.length());
        EntityBuilder builder = api.getEntityBuilder();

        TLongObjectMap<DataObject> userMap = new TLongObjectHashMap<>();
        for (int i = 0; i < users.length(); i++)
        {
            DataObject user = users.getObject(i);
            userMap.put(user.getLong("id"), user);
        }

        TLongObjectMap<DataObject> webhookMap = new TLongObjectHashMap<>();
        for (int i = 0; i < webhooks.length(); i++)
        {
            DataObject webhook = webhooks.getObject(i);
            webhookMap.put(webhook.getLong("id"), webhook);
        }

        for (int i = 0; i < entries.length(); i++)
        {
            try
            {
                DataObject entry = entries.getObject(i);
                DataObject user = userMap.get(entry.getLong("user_id", 0));
                DataObject webhook = webhookMap.get(entry.getLong("target_id", 0));
                AuditLogEntry result = builder.createAuditLogEntry((GuildImpl) guild, entry, user, webhook);
                list.add(result);
                if (this.useCache)
                    this.cached.add(result);
                this.last = result;
                this.lastKey = last.getIdLong();
            }
            catch (ParsingException | NullPointerException e)
            {
                LOG.warn("Encountered exception in AuditLogPagination", e);
            }
        }

        request.onSuccess(list);
    }

    @Override
    protected long getKey(AuditLogEntry it)
    {
        return it.getIdLong();
    }
}

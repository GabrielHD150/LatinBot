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

import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.ChannelType;
import net.latinplay.latinbot.jda.api.entities.Message;
import net.latinplay.latinbot.jda.api.entities.MessageChannel;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.exceptions.ParsingException;
import net.latinplay.latinbot.jda.api.requests.Request;
import net.latinplay.latinbot.jda.api.requests.Response;
import net.latinplay.latinbot.jda.api.requests.restaction.pagination.MessagePaginationAction;
import net.latinplay.latinbot.jda.api.utils.data.DataArray;
import net.latinplay.latinbot.jda.internal.entities.EntityBuilder;
import net.latinplay.latinbot.jda.internal.requests.Route;

import java.util.ArrayList;
import java.util.List;

public class MessagePaginationActionImpl
    extends PaginationActionImpl<Message, MessagePaginationAction>
    implements MessagePaginationAction
{
    private final MessageChannel channel;

    public MessagePaginationActionImpl(MessageChannel channel)
    {
        super(channel.getJDA(), Route.Messages.GET_MESSAGE_HISTORY.compile(channel.getId()), 1, 100, 100);

        if (channel.getType() == ChannelType.TEXT)
        {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(textChannel, Permission.MESSAGE_HISTORY))
                throw new InsufficientPermissionException(textChannel, Permission.MESSAGE_HISTORY);
        }

        this.channel = channel;
    }


    @Override
    public MessageChannel getChannel()
    {
        return channel;
    }

    @Override
    protected Route.CompiledRoute finalizeRoute()
    {
        Route.CompiledRoute route = super.finalizeRoute();

        final String limit = String.valueOf(this.getLimit());
        final long last = this.lastKey;

        route = route.withQueryParams("limit", limit);

        if (last != 0)
            route = route.withQueryParams("before", Long.toUnsignedString(last));

        return route;
    }

    @Override
    protected void handleSuccess(Response response, Request<List<Message>> request)
    {
        DataArray array = response.getArray();
        List<Message> messages = new ArrayList<>(array.length());
        EntityBuilder builder = api.getEntityBuilder();
        for (int i = 0; i < array.length(); i++)
        {
            try
            {
                Message msg = builder.createMessage(array.getObject(i), channel, false);
                messages.add(msg);
                if (useCache)
                    cached.add(msg);
                last = msg;
                lastKey = last.getIdLong();
            }
            catch (ParsingException | NullPointerException e)
            {
                LOG.warn("Encountered an exception in MessagePagination", e);
            }
            catch (IllegalArgumentException e)
            {
                if (EntityBuilder.UNKNOWN_MESSAGE_TYPE.equals(e.getMessage()))
                    LOG.warn("Skipping unknown message type during pagination", e);
                else
                    LOG.warn("Unexpected issue trying to parse message during pagination", e);
            }
        }

        request.onSuccess(messages);
    }

    @Override
    protected long getKey(Message it)
    {
        return it.getIdLong();
    }
}

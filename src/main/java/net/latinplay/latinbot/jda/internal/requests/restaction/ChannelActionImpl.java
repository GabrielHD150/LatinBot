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

package net.latinplay.latinbot.jda.internal.requests.restaction;

import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.*;
import net.latinplay.latinbot.jda.api.requests.Request;
import net.latinplay.latinbot.jda.api.requests.Response;
import net.latinplay.latinbot.jda.api.requests.restaction.ChannelAction;
import net.latinplay.latinbot.jda.api.utils.data.DataArray;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;
import net.latinplay.latinbot.jda.internal.entities.EntityBuilder;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.utils.Checks;
import okhttp3.RequestBody;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class ChannelActionImpl<T extends GuildChannel> extends AuditableRestActionImpl<T> implements ChannelAction<T>
{
    protected final Set<PermOverrideData> overrides = new HashSet<>();
    protected final Guild guild;
    protected final ChannelType type;
    protected final Class<T> clazz;
    protected String name;
    protected Category parent;
    protected Integer position;

    // --text only--
    protected String topic = null;
    protected Boolean nsfw = null;
    protected Integer slowmode = null;

    // --voice only--
    protected Integer bitrate = null;
    protected Integer userlimit = null;

    public ChannelActionImpl(Class<T> clazz, String name, Guild guild, ChannelType type)
    {
        super(guild.getJDA(), Route.Guilds.CREATE_CHANNEL.compile(guild.getId()));
        this.clazz = clazz;
        this.guild = guild;
        this.type = type;
        this.name = name;
    }


    @Override
    public ChannelActionImpl<T> setCheck(BooleanSupplier checks)
    {
        return (ChannelActionImpl<T>) super.setCheck(checks);
    }


    @Override
    public Guild getGuild()
    {
        return guild;
    }


    @Override
    public ChannelType getType()
    {
        return type;
    }


    @Override

    public ChannelActionImpl<T> setName( String name)
    {
        Checks.notNull(name, "Channel name");
        if (name.length() < 1 || name.length() > 100)
            throw new IllegalArgumentException("Provided channel name must be 1 to 100 characters in length");

        this.name = name;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setParent(Category category)
    {
        Checks.check(category == null || category.getGuild().equals(guild), "Category is not from same guild!");
        this.parent = category;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setPosition(Integer position)
    {
        Checks.check(position == null || position >= 0, "Position must be >= 0!");
        this.position = position;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setTopic(String topic)
    {
        if (type != ChannelType.TEXT)
            throw new UnsupportedOperationException("Can only set the topic for a TextChannel!");
        if (topic != null && topic.length() > 1024)
            throw new IllegalArgumentException("Channel Topic must not be greater than 1024 in length!");
        this.topic = topic;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setNSFW(boolean nsfw)
    {
        if (type != ChannelType.TEXT)
            throw new UnsupportedOperationException("Can only set nsfw for a TextChannel!");
        this.nsfw = nsfw;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setSlowmode(int slowmode)
    {
        if (type != ChannelType.TEXT)
            throw new UnsupportedOperationException("Can only set slowmode on text channels");
        Checks.check(slowmode <= TextChannel.MAX_SLOWMODE && slowmode >= 0, "Slowmode must be between 0 and %d (seconds)!", TextChannel.MAX_SLOWMODE);
        this.slowmode = slowmode;
        return this;
    }


    @Override

    public ChannelActionImpl<T> addPermissionOverride( IPermissionHolder target, long allow, long deny)
    {
        Checks.notNull(target, "Override Role");
        Checks.notNegative(allow, "Granted permissions value");
        Checks.notNegative(deny, "Denied permissions value");
        Checks.check(allow <= Permission.ALL_PERMISSIONS, "Specified allow value may not be greater than a full permission set");
        Checks.check(deny <= Permission.ALL_PERMISSIONS,  "Specified deny value may not be greater than a full permission set");
        Checks.check(target.getGuild().equals(guild), "Specified Role is not in the same Guild!");

        if (target instanceof Role)
        {
            Role r = (Role) target;
            long id = r.getIdLong();
            overrides.add(new PermOverrideData(PermOverrideData.ROLE_TYPE, id, allow, deny));
        }
        else
        {
            Member m = (Member) target;
            long id = m.getUser().getIdLong();
            overrides.add(new PermOverrideData(PermOverrideData.MEMBER_TYPE, id, allow, deny));
        }
        return this;
    }

    // --voice only--

    @Override

    public ChannelActionImpl<T> setBitrate(Integer bitrate)
    {
        if (type != ChannelType.VOICE)
            throw new UnsupportedOperationException("Can only set the bitrate for a VoiceChannel!");
        if (bitrate != null)
        {
            int maxBitrate = getGuild().getMaxBitrate();
            if (bitrate < 8000)
                throw new IllegalArgumentException("Bitrate must be greater than 8000.");
            else if (bitrate > maxBitrate)
                throw new IllegalArgumentException("Bitrate must be less than " + maxBitrate);
        }

        this.bitrate = bitrate;
        return this;
    }


    @Override

    public ChannelActionImpl<T> setUserlimit(Integer userlimit)
    {
        if (type != ChannelType.VOICE)
            throw new UnsupportedOperationException("Can only set the userlimit for a VoiceChannel!");
        if (userlimit != null && (userlimit < 0 || userlimit > 99))
            throw new IllegalArgumentException("Userlimit must be between 0-99!");
        this.userlimit = userlimit;
        return this;
    }

    @Override
    protected RequestBody finalizeData()
    {
        DataObject object = DataObject.empty();
        object.put("name", name);
        object.put("type", type.getId());
        object.put("permission_overwrites", DataArray.fromCollection(overrides));
        if (position != null)
            object.put("position", position);
        switch (type)
        {
            case VOICE:
                if (bitrate != null)
                    object.put("bitrate", bitrate);
                if (userlimit != null)
                    object.put("user_limit", userlimit);
                break;
            case TEXT:
                if (topic != null && !topic.isEmpty())
                    object.put("topic", topic);
                if (nsfw != null)
                    object.put("nsfw", nsfw);
                if (slowmode != null)
                    object.put("rate_limit_per_user", slowmode);
        }
        if (type != ChannelType.CATEGORY && parent != null)
            object.put("parent_id", parent.getId());

        return getRequestBody(object);
    }

    @Override
    protected void handleSuccess(Response response, Request<T> request)
    {
        EntityBuilder builder = api.getEntityBuilder();
        GuildChannel channel;
        switch (type)
        {
            case VOICE:
                channel = builder.createVoiceChannel(response.getObject(), guild.getIdLong());
                break;
            case TEXT:
                channel = builder.createTextChannel(response.getObject(), guild.getIdLong());
                break;
            case CATEGORY:
                channel = builder.createCategory(response.getObject(), guild.getIdLong());
                break;
            default:
                request.onFailure(new IllegalStateException("Created channel of unknown type!"));
                return;
        }
        request.onSuccess(clazz.cast(channel));
    }

    protected void checkPermissions(Collection<Permission> permissions)
    {
        if (permissions == null)
            return;
        for (Permission p : permissions)
            Checks.notNull(p, "Permissions");
    }
}

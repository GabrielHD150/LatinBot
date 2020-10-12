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

import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.PrivateChannel;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.utils.MiscUtil;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;
import net.latinplay.latinbot.jda.internal.JDAImpl;
import net.latinplay.latinbot.jda.internal.requests.DeferredRestAction;
import net.latinplay.latinbot.jda.internal.requests.RestActionImpl;
import net.latinplay.latinbot.jda.internal.requests.Route;

import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.List;

public class UserImpl implements User
{
    protected final long id;
    protected final JDAImpl api;

    protected short discriminator;
    protected String name;
    protected String avatarId;
    protected PrivateChannel privateChannel;
    protected boolean bot;
    protected boolean fake = false;

    public UserImpl(long id, JDAImpl api)
    {
        this.id = id;
        this.api = api;
    }

    
    @Override
    public String getName()
    {
        return name;
    }

    
    @Override
    public String getDiscriminator()
    {
        return String.format("%04d", discriminator);
    }

    @Override
    public String getAvatarId()
    {
        return avatarId;
    }

    
    @Override
    public String getDefaultAvatarId()
    {
        return String.valueOf(discriminator % 5);
    }

    
    @Override
    public String getAsTag()
    {
        return getName() + '#' + getDiscriminator();
    }

    @Override
    public boolean hasPrivateChannel()
    {
        return privateChannel != null;
    }

    
    @Override
    public RestAction<PrivateChannel> openPrivateChannel()
    {
        return new DeferredRestAction<>(getJDA(), PrivateChannel.class, () -> privateChannel, () -> {
            Route.CompiledRoute route = Route.Self.CREATE_PRIVATE_CHANNEL.compile();
            DataObject body = DataObject.empty().put("recipient_id", getId());
            return new RestActionImpl<>(getJDA(), route, body, (response, request) ->
            {
                PrivateChannel priv = api.getEntityBuilder().createPrivateChannel(response.getObject(), this);
                UserImpl.this.privateChannel = priv;
                return priv;
            });
        });
    }

    
    @Override
    public List<Guild> getMutualGuilds()
    {
        return getJDA().getMutualGuilds(this);
    }

    public PrivateChannel getPrivateChannel()
    {
        if (!hasPrivateChannel())
            throw new IllegalStateException("There is no PrivateChannel for this user yet! Use User#openPrivateChannel() first!");

        return privateChannel;
    }

    @Override
    public boolean isBot()
    {
        return bot;
    }

    
    @Override
    public JDAImpl getJDA()
    {
        return api;
    }

    
    @Override
    public String getAsMention()
    {
        return "<@" + getId() + '>';
    }

    @Override
    public long getIdLong()
    {
        return id;
    }

    @Override
    public boolean isFake()
    {
        return fake;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof UserImpl))
            return false;
        UserImpl oUser = (UserImpl) o;
        return this.id == oUser.id;
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(id);
    }

    @Override
    public String toString()
    {
        return "U:" + getName() + '(' + id + ')';
    }

    // -- Setters --

    public UserImpl setName(String name)
    {
        this.name = name;
        return this;
    }

    public UserImpl setDiscriminator(String discriminator)
    {
        this.discriminator = Short.parseShort(discriminator);
        return this;
    }

    public UserImpl setAvatarId(String avatarId)
    {
        this.avatarId = avatarId;
        return this;
    }

    public UserImpl setPrivateChannel(PrivateChannel privateChannel)
    {
        this.privateChannel = privateChannel;
        return this;
    }

    public UserImpl setBot(boolean bot)
    {
        this.bot = bot;
        return this;
    }

    public UserImpl setFake(boolean fake)
    {
        this.fake = fake;
        return this;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision)
    {
        boolean alt = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        boolean upper = (flags & FormattableFlags.UPPERCASE) == FormattableFlags.UPPERCASE;
        boolean leftJustified = (flags & FormattableFlags.LEFT_JUSTIFY) == FormattableFlags.LEFT_JUSTIFY;

        String out;
        if (!alt)
            out = getAsMention();
        else if (upper)
            out = getAsTag().toUpperCase();
        else
            out = getAsTag();

        MiscUtil.appendTo(formatter, width, precision, leftJustified, out);
    }
}

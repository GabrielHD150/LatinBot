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

package net.latinplay.latinbot.jda.internal.managers;

import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.Icon;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.entities.Webhook;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.managers.WebhookManager;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.utils.Checks;
import okhttp3.RequestBody;

public class WebhookManagerImpl extends ManagerBase<WebhookManager> implements WebhookManager
{
    protected final Webhook webhook;
    protected String name;
    protected String channel;
    protected Icon avatar;

    /**
     * Creates a new WebhookManager instance
     *
     * @param webhook
     *        The target {@link Webhook Webhook} to modify
     */
    public WebhookManagerImpl(Webhook webhook)
    {
        super(webhook.getJDA(), Route.Webhooks.MODIFY_WEBHOOK.compile(webhook.getId()));
        this.webhook = webhook;
        if (isPermissionChecksEnabled())
            checkPermissions();
    }


    @Override
    public Webhook getWebhook()
    {
        return webhook;
    }


    @Override

    public WebhookManagerImpl reset(long fields)
    {
        super.reset(fields);
        if ((fields & NAME) == NAME)
            this.name = null;
        if ((fields & CHANNEL) == CHANNEL)
            this.channel = null;
        if ((fields & AVATAR) == AVATAR)
            this.avatar = null;
        return this;
    }


    @Override

    public WebhookManagerImpl reset(long... fields)
    {
        super.reset(fields);
        return this;
    }


    @Override

    public WebhookManagerImpl reset()
    {
        super.reset();
        this.name = null;
        this.channel = null;
        this.avatar = null;
        return this;
    }


    @Override

    public WebhookManagerImpl setName( String name)
    {
        Checks.notBlank(name, "Name");
        this.name = name;
        set |= NAME;
        return this;
    }


    @Override

    public WebhookManagerImpl setAvatar(Icon icon)
    {
        this.avatar = icon;
        set |= AVATAR;
        return this;
    }


    @Override

    public WebhookManagerImpl setChannel( TextChannel channel)
    {
        Checks.notNull(channel, "Channel");
        Checks.check(channel.getGuild().equals(getGuild()), "Channel is not from the same guild");
        this.channel = channel.getId();
        set |= CHANNEL;
        return this;
    }

    @Override
    protected RequestBody finalizeData()
    {
        DataObject data = DataObject.empty();
        if (shouldUpdate(NAME))
            data.put("name", name);
        if (shouldUpdate(CHANNEL))
            data.put("channel_id", channel);
        if (shouldUpdate(AVATAR))
            data.put("avatar", avatar == null ? null : avatar.getEncoding());

        return getRequestBody(data);
    }

    @Override
    protected boolean checkPermissions()
    {
        if (!getGuild().getSelfMember().hasPermission(getChannel(), Permission.MANAGE_WEBHOOKS))
            throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_WEBHOOKS);
        return super.checkPermissions();
    }
}

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

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Icon;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.entities.Webhook;
import net.latinplay.latinbot.jda.api.requests.Request;
import net.latinplay.latinbot.jda.api.requests.Response;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.WebhookAction;
import net.latinplay.latinbot.jda.api.utils.data.DataObject;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.utils.Checks;
import okhttp3.RequestBody;

import java.util.function.BooleanSupplier;

/**
 * {@link Webhook Webhook} Builder system created as an extension of {@link RestAction}
 * <br>Provides an easy way to gather and deliver information to Discord to create {@link Webhook Webhooks}.
 */
public class WebhookActionImpl extends AuditableRestActionImpl<Webhook> implements WebhookAction
{
    protected final TextChannel channel;
    protected String name;
    protected Icon avatar = null;

    public WebhookActionImpl(JDA api, TextChannel channel, String name)
    {
        super(api, Route.Channels.CREATE_WEBHOOK.compile(channel.getId()));
        this.channel = channel;
        this.name = name;
    }


    @Override
    public WebhookActionImpl setCheck(BooleanSupplier checks)
    {
        return (WebhookActionImpl) super.setCheck(checks);
    }


    @Override
    public TextChannel getChannel()
    {
        return channel;
    }


    @Override

    public WebhookActionImpl setName( String name)
    {
        Checks.notNull(name, "Webhook name");
        Checks.check(name.length() >= 2 && name.length() <= 100, "The webhook name must be in the range of 2-100!");

        this.name = name;
        return this;
    }


    @Override

    public WebhookActionImpl setAvatar(Icon icon)
    {
        this.avatar = icon;
        return this;
    }

    @Override
    public RequestBody finalizeData()
    {
        DataObject object = DataObject.empty();
        object.put("name",   name);
        object.put("avatar", avatar != null ? avatar.getEncoding() : null);

        return getRequestBody(object);
    }

    @Override
    protected void handleSuccess(Response response, Request<Webhook> request)
    {
        DataObject json = response.getObject();
        Webhook webhook = api.getEntityBuilder().createWebhook(json);

        request.onSuccess(webhook);
    }
}

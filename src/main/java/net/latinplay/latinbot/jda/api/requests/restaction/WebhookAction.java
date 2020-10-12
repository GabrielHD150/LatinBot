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

package net.latinplay.latinbot.jda.api.requests.restaction;

import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.Icon;
import net.latinplay.latinbot.jda.api.entities.TextChannel;
import net.latinplay.latinbot.jda.api.entities.Webhook;
import net.latinplay.latinbot.jda.api.requests.RestAction;

import java.util.function.BooleanSupplier;

/**
 * {@link Webhook Webhook} Builder system created as an extension of {@link RestAction}
 * <br>Provides an easy way to gather and deliver information to Discord to create {@link Webhook Webhooks}.
 *
 * @see TextChannel#createWebhook(String)
 */
public interface WebhookAction extends AuditableRestAction<Webhook>
{
    
    @Override
    WebhookAction setCheck(BooleanSupplier checks);

    /**
     * The {@link TextChannel TextChannel} to create this webhook in
     *
     * @return The channel
     */
    
    TextChannel getChannel();

    /**
     * The {@link Guild Guild} to create this webhook in
     *
     * @return The guild
     */
    
    default Guild getGuild()
    {
        return getChannel().getGuild();
    }

    /**
     * Sets the <b>Name</b> for the custom Webhook User
     *
     * @param  name
     *         A not-null String name for the new Webhook user.
     *
     * @throws IllegalArgumentException
     *         If the specified name is not in the range of 2-100.
     *
     * @return The current WebhookAction for chaining convenience.
     */
    
    
    WebhookAction setName( String name);

    /**
     * Sets the <b>Avatar</b> for the custom Webhook User
     *
     * @param  icon
     *         An {@link Icon Icon} for the new avatar.
     *         Or null to use default avatar.
     *
     * @return The current WebhookAction for chaining convenience.
     */
    
    
    WebhookAction setAvatar(Icon icon);
}

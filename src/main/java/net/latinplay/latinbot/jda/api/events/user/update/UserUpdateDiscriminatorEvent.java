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

package net.latinplay.latinbot.jda.api.events.user.update;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.entities.User;

/**
 * Indicates that the discriminator of a {@link User User} changed.
 * <br>This event requires {@link JDABuilder#setGuildSubscriptionsEnabled(boolean) guild subscriptions}
 * to be enabled.
 *
 * <p>Can be used to retrieve the User who changed their discriminator and their previous discriminator.
 *
 * <p>Identifier: {@code discriminator}
 */
public class UserUpdateDiscriminatorEvent extends GenericUserUpdateEvent<String>
{
    public static final String IDENTIFIER = "discriminator";

    public UserUpdateDiscriminatorEvent( JDA api, long responseNumber,  User user,  String oldDiscriminator)
    {
        super(api, responseNumber, user, oldDiscriminator, user.getDiscriminator(), IDENTIFIER);
    }

    /**
     * The old discriminator
     *
     * @return The old discriminator
     */

    public String getOldDiscriminator()
    {
        return getOldValue();
    }

    /**
     * The new discriminator
     *
     * @return The new discriminator
     */

    public String getNewDiscriminator()
    {
        return getNewValue();
    }


    @Override
    public String getOldValue()
    {
        return super.getOldValue();
    }


    @Override
    public String getNewValue()
    {
        return super.getNewValue();
    }
}

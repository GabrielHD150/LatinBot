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

package net.latinplay.latinbot.jda.api.events.self;

import net.latinplay.latinbot.jda.api.JDA;

/**
 * Indicates that the email of the current user changed. (client-only)
 *
 * <p>Can be used to retrieve the old email.
 *
 * <p>Identifier: {@code email}
 */
public class SelfUpdateEmailEvent extends GenericSelfUpdateEvent<String>
{
    public static final String IDENTIFIER = "email";

    public SelfUpdateEmailEvent( JDA api, long responseNumber,  String oldEmail)
    {
        super(api, responseNumber, oldEmail, api.getSelfUser().getEmail(), IDENTIFIER);
    }

    /**
     * The old email
     *
     * @return The old email
     */

    public String getOldEmail()
    {
        return getOldValue();
    }

    /**
     * The new email
     *
     * @return The new email
     */

    public String getNewEmail()
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

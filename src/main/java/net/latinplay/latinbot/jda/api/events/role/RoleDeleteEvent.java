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

package net.latinplay.latinbot.jda.api.events.role;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Role;

/**
 * Indicates that a {@link Role Role} was deleted.
 *
 * <p>Can be used to retrieve the deleted Role and its Guild.
 */
public class RoleDeleteEvent extends GenericRoleEvent
{
    public RoleDeleteEvent( JDA api, long responseNumber,  Role deletedRole)
    {
        super(api, responseNumber, deletedRole);
    }
}

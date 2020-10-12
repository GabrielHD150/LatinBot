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

package net.latinplay.latinbot.jda.api.requests.restaction.order;

import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.requests.RestAction;

/**
 * Implementation of {@link OrderAction OrderAction}
 * designed to modify the order of {@link Role Roles} of the
 * specified {@link Guild Guild}.
 * <br>To apply the changes you must finish the {@link RestAction RestAction}
 *
 * <p>Before you can use any of the {@code move} methods
 * you must use either {@link #selectPosition(Object) selectPosition(Role)} or {@link #selectPosition(int)}!
 *
 * <p><b>This uses descending order!</b>
 *
 * @since 3.0
 *
 * @see   Guild#modifyRolePositions()
 * @see   Guild#modifyRolePositions(boolean)
 */
public interface RoleOrderAction extends OrderAction<Role, RoleOrderAction>
{
    /**
     * The {@link Guild Guild} which holds
     * the roles from {@link #getCurrentOrder()}
     *
     * @return The corresponding {@link Guild Guild}
     */
    Guild getGuild();
}

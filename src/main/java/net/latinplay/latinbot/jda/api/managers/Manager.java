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

package net.latinplay.latinbot.jda.api.managers;

import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.requests.ErrorResponse;
import net.latinplay.latinbot.jda.api.requests.restaction.AuditableRestAction;
import net.latinplay.latinbot.jda.internal.managers.ManagerBase;

import java.util.function.BooleanSupplier;

public interface Manager<M extends Manager<M>> extends AuditableRestAction<Void>
{
    /**
     * Enables internal checks for missing permissions
     * <br>When this is disabled the chances of hitting a
     * {@link ErrorResponse#MISSING_PERMISSIONS ErrorResponse.MISSING_PERMISSIONS} is increased significantly,
     * otherwise JDA will check permissions and cancel the execution using
     * {@link InsufficientPermissionException InsufficientPermissionException}.
     * <br><b>Default: true</b>
     *
     * @param enable
     *        True, if JDA should perform permissions checks internally
     *
     * @see   #isPermissionChecksEnabled()
     */
    static void setPermissionChecksEnabled(boolean enable)
    {
        ManagerBase.setPermissionChecksEnabled(enable);
    }

    /**
     * Whether internal checks for missing permissions are enabled
     * <br>When this is disabled the chances of hitting a
     * {@link ErrorResponse#MISSING_PERMISSIONS ErrorResponse.MISSING_PERMISSIONS} is increased significantly,
     * otherwise JDA will check permissions and cancel the execution using
     * {@link InsufficientPermissionException InsufficientPermissionException}.
     *
     * @return True, if internal permission checks are enabled
     *
     * @see    #setPermissionChecksEnabled(boolean)
     */
    static boolean isPermissionChecksEnabled()
    {
        return ManagerBase.isPermissionChecksEnabled();
    }

    
    @Override
    M setCheck(BooleanSupplier checks);

    
    
    M reset(long fields);

    
    
    M reset(long... fields);

    /**
     * Resets all fields for this Manager
     *
     * @return The current Manager with all settings reset to default
     */
    
    
    M reset();
}

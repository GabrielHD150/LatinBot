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

import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.Role;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.internal.utils.Checks;

import java.awt.*;
import java.util.Collection;
import java.util.function.BooleanSupplier;

/**
 * Extension of {@link RestAction RestAction} specifically
 * designed to create a {@link Role Role}.
 * This extension allows setting properties before executing the action.
 *
 * @since  3.0
 *
 * @see    Guild
 * @see    Guild#createRole()
 * @see    Role#createCopy()
 * @see    Role#createCopy(Guild)
 */
public interface RoleAction extends AuditableRestAction<Role>
{
    
    @Override
    RoleAction setCheck( BooleanSupplier checks);

    /**
     * The guild to create the role in
     *
     * @return The guild
     */
    
    Guild getGuild();

    /**
     * Sets the name for new role (optional)
     *
     * @param  name
     *         The name for the new role, null to use default name
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided name is longer than 100 characters
     *
     * @return The current RoleAction, for chaining convenience
     */
    
    
    RoleAction setName( String name);

    /**
     * Sets whether or not the new role should be hoisted
     *
     * @param  hoisted
     *         Whether the new role should be hoisted (grouped). Default is {@code false}
     *
     * @return The current RoleAction, for chaining convenience
     */
    
    
    RoleAction setHoisted( Boolean hoisted);

    /**
     * Sets whether the new role should be mentionable by members of
     * the parent {@link Guild Guild}.
     *
     * @param  mentionable
     *         Whether the new role should be mentionable. Default is {@code false}
     *
     * @return The current RoleAction, for chaining convenience
     */
    
    
    RoleAction setMentionable( Boolean mentionable);

    /**
     * Sets the color which the new role should be displayed with.
     *
     * @param  color
     *         An {@link java.awt.Color Color} for the new role, null to use default white/black
     *
     * @return The current RoleAction, for chaining convenience
     */
    
    
    default RoleAction setColor( Color color)
    {
        return this.setColor(color != null ? color.getRGB() : null);
    }

    /**
     * Sets the Color for the new role.
     * This accepts colors from the range {@code 0x000} to {@code 0xFFFFFF}.
     * The provided value will be ranged using {@code rbg & 0xFFFFFF}
     *
     * @param  rgb
     *         The color for the new role in integer form, {@code null} to use default white/black
     *
     * @return The current RoleAction, for chaining convenience
     */
    
    
    RoleAction setColor( Integer rgb);

    /**
     * Sets the Permissions the new Role should have.
     * This will only allow permissions that the current account already holds unless
     * the account is owner or {@link Permission#ADMINISTRATOR admin} of the parent {@link Guild Guild}.
     *
     * @param  permissions
     *         The varargs {@link Permission Permissions} for the new role
     *
     * @throws InsufficientPermissionException
     *         If the currently logged in account does not hold one of the specified permissions
     * @throws IllegalArgumentException
     *         If any of the provided permissions is {@code null}
     *
     * @return The current RoleAction, for chaining convenience
     *
     * @see    Permission#getRaw(Permission...) Permission.getRaw(Permission...)
     */
    
    
    default RoleAction setPermissions( Permission... permissions)
    {
        if (permissions != null)
            Checks.noneNull(permissions, "Permissions");

        return setPermissions(permissions == null ? null : Permission.getRaw(permissions));
    }

    /**
     * Sets the Permissions the new Role should have.
     * This will only allow permissions that the current account already holds unless
     * the account is owner or {@link Permission#ADMINISTRATOR admin} of the parent {@link Guild Guild}.
     *
     * @param  permissions
     *         A {@link java.util.Collection Collection} of {@link Permission Permissions} for the new role
     *
     * @throws InsufficientPermissionException
     *         If the currently logged in account does not hold one of the specified permissions
     * @throws IllegalArgumentException
     *         If any of the provided permissions is {@code null}
     *
     * @return The current RoleAction, for chaining convenience
     *
     * @see    Permission#getRaw(java.util.Collection) Permission.getRaw(Collection)
     * @see    java.util.EnumSet EnumSet
     */
    
    
    default RoleAction setPermissions( Collection<Permission> permissions)
    {
        if (permissions != null)
            Checks.noneNull(permissions, "Permissions");

        return setPermissions(permissions == null ? null : Permission.getRaw(permissions));
    }

    /**
     * Sets the Permissions the new Role should have.
     * This will only allow permissions that the current account already holds unless
     * the account is owner or {@link Permission#ADMINISTRATOR admin} of the parent {@link Guild Guild}.
     *
     * @param  permissions
     *         The raw {@link Permission Permissions} value for the new role.
     *         To retrieve this use {@link Permission#getRawValue()}
     *
     * @throws java.lang.IllegalArgumentException
     *         If the provided permission value is invalid
     * @throws InsufficientPermissionException
     *         If the currently logged in account does not hold one of the specified permissions
     *
     * @return The current RoleAction, for chaining convenience
     *
     * @see    Permission#getRawValue()
     * @see    Permission#getRaw(java.util.Collection)
     * @see    Permission#getRaw(Permission...)
     */
    
    
    RoleAction setPermissions( Long permissions);
}

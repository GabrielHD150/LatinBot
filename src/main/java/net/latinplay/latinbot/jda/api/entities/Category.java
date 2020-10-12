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

package net.latinplay.latinbot.jda.api.entities;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.Permission;
import net.latinplay.latinbot.jda.api.exceptions.InsufficientPermissionException;
import net.latinplay.latinbot.jda.api.requests.ErrorResponse;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.ChannelAction;
import net.latinplay.latinbot.jda.api.requests.restaction.order.CategoryOrderAction;
import net.latinplay.latinbot.jda.api.requests.restaction.order.ChannelOrderAction;
import net.latinplay.latinbot.jda.api.requests.restaction.order.OrderAction;

import java.util.List;

/**
 * Represents a channel category in the official Discord API.
 * <br>Categories are used to keep order in a Guild by dividing the channels into groups.
 *
 * @since 3.4.0
 *
 * @see   Guild#getCategoryCache()
 * @see   Guild#getCategories()
 * @see   Guild#getCategoriesByName(String, boolean)
 * @see   Guild#getCategoryById(long)
 *
 * @see   JDA#getCategoryCache()
 * @see   JDA#getCategories()
 * @see   JDA#getCategoriesByName(String, boolean)
 * @see   JDA#getCategoryById(long)
 */
public interface Category extends GuildChannel
{
    /**
     * All {@link GuildChannel Channels} listed
     * for this Category
     * <br>This may contain {@link VoiceChannel VoiceChannels},
     * {@link StoreChannel StoreChannels},
     * and {@link TextChannel TextChannels}!
     *
     * @return Immutable list of all child channels
     */
    
    List<GuildChannel> getChannels();

    /**
     * All {@link StoreChannel StoreChannels}
     * listed for this Category
     *
     * @return Immutable list of all child StoreChannels
     *
     * @since  4.0.0
     */
    
    List<StoreChannel> getStoreChannels();

    /**
     * All {@link TextChannel TextChannels}
     * listed for this Category
     *
     * @return Immutable list of all child TextChannels
     */
    
    List<TextChannel> getTextChannels();

    /**
     * All {@link VoiceChannel VoiceChannels}
     * listed for this Category
     *
     * @return Immutable list of all child VoiceChannels
     */
    
    List<VoiceChannel> getVoiceChannels();

    /**
     * Creates a new {@link TextChannel TextChannel} with this Category as parent.
     * For this to be successful, the logged in account has to have the
     * {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission in the {@link Guild Guild}.
     *
     * <p>This will copy all {@link PermissionOverride PermissionOverrides} of this Category!
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The {@link Permission#VIEW_CHANNEL VIEW_CHANNEL} permission was removed</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param  name
     *         The name of the TextChannel to create
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException
     *         If the provided name is {@code null} or empty or greater than 100 characters in length
     *
     * @return A specific {@link ChannelAction ChannelAction}
     *         <br>This action allows to set fields for the new TextChannel before creating it
     */
    
    
    ChannelAction<TextChannel> createTextChannel( String name);

    /**
     * Creates a new {@link VoiceChannel VoiceChannel} with this Category as parent.
     * For this to be successful, the logged in account has to have the
     * {@link Permission#MANAGE_CHANNEL MANAGE_CHANNEL} Permission in the {@link Guild Guild}.
     *
     * <p>This will copy all {@link PermissionOverride PermissionOverrides} of this Category!
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} caused by
     * the returned {@link RestAction RestAction} include the following:
     * <ul>
     *     <li>{@link ErrorResponse#MISSING_PERMISSIONS MISSING_PERMISSIONS}
     *     <br>The channel could not be created due to a permission discrepancy</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The {@link Permission#VIEW_CHANNEL VIEW_CHANNEL} permission was removed</li>
     *
     *     <li>{@link ErrorResponse#MAX_CHANNELS MAX_CHANNELS}
     *     <br>The maximum number of channels were exceeded</li>
     * </ul>
     *
     * @param  name
     *         The name of the VoiceChannel to create
     *
     * @throws InsufficientPermissionException
     *         If the logged in account does not have the {@link Permission#MANAGE_CHANNEL} permission
     * @throws IllegalArgumentException
     *         If the provided name is {@code null} or empty or greater than 100 characters in length
     *
     * @return A specific {@link ChannelAction ChannelAction}
     *         <br>This action allows to set fields for the new VoiceChannel before creating it
     */
    
    
    ChannelAction<VoiceChannel> createVoiceChannel( String name);

    /**
     * Modifies the positional order of this Category's nested {@link #getTextChannels() TextChannels} and {@link #getStoreChannels() StoreChannels}.
     * <br>This uses an extension of {@link ChannelOrderAction ChannelOrderAction}
     * specialized for ordering the nested {@link TextChannel TextChannels}
     * and {@link StoreChannel StoreChannels} of this {@link Category Category}.
     * <br>Like {@link ChannelOrderAction}, the returned {@link CategoryOrderAction CategoryOrderAction}
     * can be used to move TextChannels/StoreChannels {@link OrderAction#moveUp(int) up},
     * {@link OrderAction#moveDown(int) down}, or
     * {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task.</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild.</li>
     * </ul>
     *
     * @return A {@link CategoryOrderAction CategoryOrderAction} for
     *         ordering the Category's {@link TextChannel TextChannels}
     *         and {@link StoreChannel StoreChannels}.
     */
    
    
    CategoryOrderAction modifyTextChannelPositions();

    /**
     * Modifies the positional order of this Category's nested {@link #getVoiceChannels() VoiceChannels}.
     * <br>This uses an extension of {@link ChannelOrderAction ChannelOrderAction}
     * specialized for ordering the nested {@link VoiceChannel VoiceChannels} of this
     * {@link Category Category}.
     * <br>Like {@code ChannelOrderAction}, the returned {@link CategoryOrderAction CategoryOrderAction}
     * can be used to move VoiceChannels {@link OrderAction#moveUp(int) up},
     * {@link OrderAction#moveDown(int) down}, or
     * {@link OrderAction#moveTo(int) to} a specific position.
     * <br>This uses <b>ascending</b> order with a 0 based index.
     *
     * <p>Possible {@link ErrorResponse ErrorResponses} include:
     * <ul>
     *     <li>{@link ErrorResponse#UNKNOWN_CHANNEL UNNKOWN_CHANNEL}
     *     <br>One of the channels has been deleted before the completion of the task.</li>
     *
     *     <li>{@link ErrorResponse#MISSING_ACCESS MISSING_ACCESS}
     *     <br>The currently logged in account was removed from the Guild.</li>
     * </ul>
     *
     * @return A {@link CategoryOrderAction CategoryOrderAction} for
     *         ordering the Category's {@link VoiceChannel VoiceChannels}.
     */
    
    
    CategoryOrderAction modifyVoiceChannelPositions();

    
    @Override
    ChannelAction<Category> createCopy( Guild guild);

    
    @Override
    ChannelAction<Category> createCopy();
}

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
package net.latinplay.latinbot.jda.api.hooks;

import net.latinplay.latinbot.jda.api.events.*;
import net.latinplay.latinbot.jda.api.events.channel.category.CategoryCreateEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.CategoryDeleteEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.GenericCategoryEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.latinplay.latinbot.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.latinplay.latinbot.jda.api.events.channel.priv.PrivateChannelCreateEvent;
import net.latinplay.latinbot.jda.api.events.channel.priv.PrivateChannelDeleteEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.GenericStoreChannelEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.StoreChannelCreateEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.StoreChannelDeleteEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.update.GenericStoreChannelUpdateEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.update.StoreChannelUpdateNameEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.update.StoreChannelUpdatePermissionsEvent;
import net.latinplay.latinbot.jda.api.events.channel.store.update.StoreChannelUpdatePositionEvent;
import net.latinplay.latinbot.jda.api.events.channel.text.GenericTextChannelEvent;
import net.latinplay.latinbot.jda.api.events.channel.text.TextChannelCreateEvent;
import net.latinplay.latinbot.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.latinplay.latinbot.jda.api.events.channel.text.update.*;
import net.latinplay.latinbot.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.latinplay.latinbot.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.latinplay.latinbot.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.latinplay.latinbot.jda.api.events.channel.voice.update.*;
import net.latinplay.latinbot.jda.api.events.emote.EmoteAddedEvent;
import net.latinplay.latinbot.jda.api.events.emote.EmoteRemovedEvent;
import net.latinplay.latinbot.jda.api.events.emote.GenericEmoteEvent;
import net.latinplay.latinbot.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.latinplay.latinbot.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.latinplay.latinbot.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.latinplay.latinbot.jda.api.events.guild.*;
import net.latinplay.latinbot.jda.api.events.guild.member.*;
import net.latinplay.latinbot.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.latinplay.latinbot.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.latinplay.latinbot.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.latinplay.latinbot.jda.api.events.guild.update.*;
import net.latinplay.latinbot.jda.api.events.guild.voice.*;
import net.latinplay.latinbot.jda.api.events.http.HttpRequestEvent;
import net.latinplay.latinbot.jda.api.events.message.*;
import net.latinplay.latinbot.jda.api.events.message.guild.*;
import net.latinplay.latinbot.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.latinplay.latinbot.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.latinplay.latinbot.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.latinplay.latinbot.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.latinplay.latinbot.jda.api.events.message.priv.*;
import net.latinplay.latinbot.jda.api.events.message.priv.react.GenericPrivateMessageReactionEvent;
import net.latinplay.latinbot.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.latinplay.latinbot.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.latinplay.latinbot.jda.api.events.message.react.GenericMessageReactionEvent;
import net.latinplay.latinbot.jda.api.events.message.react.MessageReactionAddEvent;
import net.latinplay.latinbot.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.latinplay.latinbot.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.latinplay.latinbot.jda.api.events.role.GenericRoleEvent;
import net.latinplay.latinbot.jda.api.events.role.RoleCreateEvent;
import net.latinplay.latinbot.jda.api.events.role.RoleDeleteEvent;
import net.latinplay.latinbot.jda.api.events.role.update.*;
import net.latinplay.latinbot.jda.api.events.self.*;
import net.latinplay.latinbot.jda.api.events.user.GenericUserEvent;
import net.latinplay.latinbot.jda.api.events.user.UserActivityEndEvent;
import net.latinplay.latinbot.jda.api.events.user.UserActivityStartEvent;
import net.latinplay.latinbot.jda.api.events.user.UserTypingEvent;
import net.latinplay.latinbot.jda.api.events.user.update.*;

/**
 * An abstract implementation of {@link EventListener EventListener} which divides {@link Event Events}
 * for you. You should <b><u>override</u></b> the methods provided by this class for your event listener implementation.
 *
 * <h2>Example:</h2>
 * <pre><code>
 * public class MyReadyListener extends ListenerAdapter
 * {
 *    {@literal @Override}
 *     public void onReady(ReadyEvent event)
 *     {
 *         System.out.println("I am ready to go!");
 *     }
 *
 *    {@literal @Override}
 *     public void onMessageReceived(MessageReceivedEvent event)
 *     {
 *         System.out.printf("[%s]: %s\n", event.getAuthor().getName(), event.getMessage().getContentDisplay());
 *     }
 * }
 * </code></pre>
 *
 * @see EventListener EventListener
 * @see InterfacedEventManager InterfacedEventManager
 */
public abstract class ListenerAdapter implements EventListener
{
    public void onGenericEvent( GenericEvent event) {}
    public void onGenericUpdate( UpdateEvent<?, ?> event) {}
    public void onRawGateway( RawGatewayEvent event) {}
    public void onGatewayPing( GatewayPingEvent event) {}

    //JDA Events
    public void onReady( ReadyEvent event) {}
    public void onResume( ResumedEvent event) {}
    public void onReconnect( ReconnectedEvent event) {}
    public void onDisconnect( DisconnectEvent event) {}
    public void onShutdown( ShutdownEvent event) {}
    public void onStatusChange( StatusChangeEvent event) {}
    public void onException( ExceptionEvent event) {}

    //User Events
    public void onUserUpdateName( UserUpdateNameEvent event) {}
    public void onUserUpdateDiscriminator( UserUpdateDiscriminatorEvent event) {}
    public void onUserUpdateAvatar( UserUpdateAvatarEvent event) {}
    public void onUserUpdateOnlineStatus( UserUpdateOnlineStatusEvent event) {}
    public void onUserUpdateActivityOrder( UserUpdateActivityOrderEvent event) {}
    public void onUserTyping( UserTypingEvent event) {}
    public void onUserActivityStart( UserActivityStartEvent event) {}
    public void onUserActivityEnd( UserActivityEndEvent event) {}

    //Self Events. Fires only in relation to the currently logged in account.
    public void onSelfUpdateAvatar( SelfUpdateAvatarEvent event) {}
    public void onSelfUpdateEmail( SelfUpdateEmailEvent event) {}
    public void onSelfUpdateMFA( SelfUpdateMFAEvent event) {}
    public void onSelfUpdateName( SelfUpdateNameEvent event) {}
    public void onSelfUpdateVerified( SelfUpdateVerifiedEvent event) {}

    //Message Events
    //Guild (TextChannel) Message Events
    public void onGuildMessageReceived( GuildMessageReceivedEvent event) {}
    public void onGuildMessageUpdate( GuildMessageUpdateEvent event) {}
    public void onGuildMessageDelete( GuildMessageDeleteEvent event) {}
    public void onGuildMessageEmbed( GuildMessageEmbedEvent event) {}
    public void onGuildMessageReactionAdd( GuildMessageReactionAddEvent event) {}
    public void onGuildMessageReactionRemove( GuildMessageReactionRemoveEvent event) {}
    public void onGuildMessageReactionRemoveAll( GuildMessageReactionRemoveAllEvent event) {}

    //Private Message Events
    public void onPrivateMessageReceived( PrivateMessageReceivedEvent event) {}
    public void onPrivateMessageUpdate( PrivateMessageUpdateEvent event) {}
    public void onPrivateMessageDelete( PrivateMessageDeleteEvent event) {}
    public void onPrivateMessageEmbed( PrivateMessageEmbedEvent event) {}
    public void onPrivateMessageReactionAdd( PrivateMessageReactionAddEvent event) {}
    public void onPrivateMessageReactionRemove( PrivateMessageReactionRemoveEvent event) {}

    //Combined Message Events (Combines Guild and Private message into 1 event)
    public void onMessageReceived( MessageReceivedEvent event) {}
    public void onMessageUpdate( MessageUpdateEvent event) {}
    public void onMessageDelete( MessageDeleteEvent event) {}
    public void onMessageBulkDelete( MessageBulkDeleteEvent event) {}
    public void onMessageEmbed( MessageEmbedEvent event) {}
    public void onMessageReactionAdd( MessageReactionAddEvent event) {}
    public void onMessageReactionRemove( MessageReactionRemoveEvent event) {}
    public void onMessageReactionRemoveAll( MessageReactionRemoveAllEvent event) {}

    //StoreChannel Events
    public void onStoreChannelDelete( StoreChannelDeleteEvent event) {}
    public void onStoreChannelUpdateName( StoreChannelUpdateNameEvent event) {}
    public void onStoreChannelUpdatePosition( StoreChannelUpdatePositionEvent event) {}
    public void onStoreChannelUpdatePermissions( StoreChannelUpdatePermissionsEvent event) {}
    public void onStoreChannelCreate( StoreChannelCreateEvent event) {}

    //TextChannel Events
    public void onTextChannelDelete( TextChannelDeleteEvent event) {}
    public void onTextChannelUpdateName( TextChannelUpdateNameEvent event) {}
    public void onTextChannelUpdateTopic( TextChannelUpdateTopicEvent event) {}
    public void onTextChannelUpdatePosition( TextChannelUpdatePositionEvent event) {}
    public void onTextChannelUpdatePermissions( TextChannelUpdatePermissionsEvent event) {}
    public void onTextChannelUpdateNSFW( TextChannelUpdateNSFWEvent event) {}
    public void onTextChannelUpdateParent( TextChannelUpdateParentEvent event) {}
    public void onTextChannelUpdateSlowmode( TextChannelUpdateSlowmodeEvent event) {}
    public void onTextChannelCreate( TextChannelCreateEvent event) {}

    //VoiceChannel Events
    public void onVoiceChannelDelete( VoiceChannelDeleteEvent event) {}
    public void onVoiceChannelUpdateName( VoiceChannelUpdateNameEvent event) {}
    public void onVoiceChannelUpdatePosition( VoiceChannelUpdatePositionEvent event) {}
    public void onVoiceChannelUpdateUserLimit( VoiceChannelUpdateUserLimitEvent event) {}
    public void onVoiceChannelUpdateBitrate( VoiceChannelUpdateBitrateEvent event) {}
    public void onVoiceChannelUpdatePermissions( VoiceChannelUpdatePermissionsEvent event) {}
    public void onVoiceChannelUpdateParent( VoiceChannelUpdateParentEvent event) {}
    public void onVoiceChannelCreate( VoiceChannelCreateEvent event) {}

    //Category Events
    public void onCategoryDelete( CategoryDeleteEvent event) {}
    public void onCategoryUpdateName( CategoryUpdateNameEvent event) {}
    public void onCategoryUpdatePosition( CategoryUpdatePositionEvent event) {}
    public void onCategoryUpdatePermissions( CategoryUpdatePermissionsEvent event) {}
    public void onCategoryCreate( CategoryCreateEvent event) {}

    //PrivateChannel Events
    public void onPrivateChannelCreate( PrivateChannelCreateEvent event) {}
    public void onPrivateChannelDelete( PrivateChannelDeleteEvent event) {}

    //Guild Events
    public void onGuildReady( GuildReadyEvent event) {}
    public void onGuildJoin( GuildJoinEvent event) {}
    public void onGuildLeave( GuildLeaveEvent event) {}
    public void onGuildAvailable( GuildAvailableEvent event) {}
    public void onGuildUnavailable( GuildUnavailableEvent event) {}
    public void onUnavailableGuildJoined( UnavailableGuildJoinedEvent event) {}
    public void onUnavailableGuildLeave( UnavailableGuildLeaveEvent event) {}
    public void onGuildBan( GuildBanEvent event) {}
    public void onGuildUnban( GuildUnbanEvent event) {}

    //Guild Update Events
    public void onGuildUpdateAfkChannel( GuildUpdateAfkChannelEvent event) {}
    public void onGuildUpdateSystemChannel( GuildUpdateSystemChannelEvent event) {}
    public void onGuildUpdateAfkTimeout( GuildUpdateAfkTimeoutEvent event) {}
    public void onGuildUpdateExplicitContentLevel( GuildUpdateExplicitContentLevelEvent event) {}
    public void onGuildUpdateIcon( GuildUpdateIconEvent event) {}
    public void onGuildUpdateMFALevel( GuildUpdateMFALevelEvent event) {}
    public void onGuildUpdateName( GuildUpdateNameEvent event){}
    public void onGuildUpdateNotificationLevel( GuildUpdateNotificationLevelEvent event) {}
    public void onGuildUpdateOwner( GuildUpdateOwnerEvent event) {}
    public void onGuildUpdateRegion( GuildUpdateRegionEvent event) {}
    public void onGuildUpdateSplash( GuildUpdateSplashEvent event) {}
    public void onGuildUpdateVerificationLevel( GuildUpdateVerificationLevelEvent event) {}
    public void onGuildUpdateFeatures( GuildUpdateFeaturesEvent event) {}
    public void onGuildUpdateVanityCode( GuildUpdateVanityCodeEvent event) {}
    public void onGuildUpdateBanner( GuildUpdateBannerEvent event) {}
    public void onGuildUpdateDescription( GuildUpdateDescriptionEvent event) {}
    public void onGuildUpdateBoostTier( GuildUpdateBoostTierEvent event) {}
    public void onGuildUpdateBoostCount( GuildUpdateBoostCountEvent event) {}
    public void onGuildUpdateMaxMembers( GuildUpdateMaxMembersEvent event) {}
    public void onGuildUpdateMaxPresences( GuildUpdateMaxPresencesEvent event) {}

    //Guild Member Events
    public void onGuildMemberJoin( GuildMemberJoinEvent event) {}
    public void onGuildMemberLeave( GuildMemberLeaveEvent event) {}
    public void onGuildMemberRoleAdd( GuildMemberRoleAddEvent event) {}
    public void onGuildMemberRoleRemove( GuildMemberRoleRemoveEvent event) {}

    //Guild Member Update Events
    public void onGuildMemberUpdateNickname( GuildMemberUpdateNicknameEvent event) {}
    public void onGuildMemberUpdateBoostTime( GuildMemberUpdateBoostTimeEvent event) {}

    //Guild Voice Events
    public void onGuildVoiceUpdate( GuildVoiceUpdateEvent event) {}
    public void onGuildVoiceJoin( GuildVoiceJoinEvent event) {}
    public void onGuildVoiceMove( GuildVoiceMoveEvent event) {}
    public void onGuildVoiceLeave( GuildVoiceLeaveEvent event) {}
    public void onGuildVoiceMute( GuildVoiceMuteEvent event) {}
    public void onGuildVoiceDeafen( GuildVoiceDeafenEvent event) {}
    public void onGuildVoiceGuildMute( GuildVoiceGuildMuteEvent event) {}
    public void onGuildVoiceGuildDeafen( GuildVoiceGuildDeafenEvent event) {}
    public void onGuildVoiceSelfMute( GuildVoiceSelfMuteEvent event) {}
    public void onGuildVoiceSelfDeafen( GuildVoiceSelfDeafenEvent event) {}
    public void onGuildVoiceSuppress( GuildVoiceSuppressEvent event) {}

    //Role events
    public void onRoleCreate( RoleCreateEvent event) {}
    public void onRoleDelete( RoleDeleteEvent event) {}

    //Role Update Events
    public void onRoleUpdateColor( RoleUpdateColorEvent event) {}
    public void onRoleUpdateHoisted( RoleUpdateHoistedEvent event) {}
    public void onRoleUpdateMentionable( RoleUpdateMentionableEvent event) {}
    public void onRoleUpdateName( RoleUpdateNameEvent event) {}
    public void onRoleUpdatePermissions( RoleUpdatePermissionsEvent event) {}
    public void onRoleUpdatePosition( RoleUpdatePositionEvent event) {}

    //Emote Events
    public void onEmoteAdded( EmoteAddedEvent event) {}
    public void onEmoteRemoved( EmoteRemovedEvent event) {}

    //Emote Update Events
    public void onEmoteUpdateName( EmoteUpdateNameEvent event) {}
    public void onEmoteUpdateRoles( EmoteUpdateRolesEvent event) {}

    // Debug Events
    public void onHttpRequest( HttpRequestEvent event) {}

    //Generic Events
    public void onGenericMessage( GenericMessageEvent event) {}
    public void onGenericMessageReaction( GenericMessageReactionEvent event) {}
    public void onGenericGuildMessage( GenericGuildMessageEvent event) {}
    public void onGenericGuildMessageReaction( GenericGuildMessageReactionEvent event) {}
    public void onGenericPrivateMessage( GenericPrivateMessageEvent event) {}
    public void onGenericPrivateMessageReaction( GenericPrivateMessageReactionEvent event) {}
    public void onGenericUser( GenericUserEvent event) {}
    public void onGenericUserPresence( GenericUserPresenceEvent event) {}
    public void onGenericSelfUpdate( GenericSelfUpdateEvent event) {}
    public void onGenericStoreChannel( GenericStoreChannelEvent event) {}
    public void onGenericStoreChannelUpdate( GenericStoreChannelUpdateEvent event) {}
    public void onGenericTextChannel( GenericTextChannelEvent event) {}
    public void onGenericTextChannelUpdate( GenericTextChannelUpdateEvent event) {}
    public void onGenericVoiceChannel( GenericVoiceChannelEvent event) {}
    public void onGenericVoiceChannelUpdate( GenericVoiceChannelUpdateEvent event) {}
    public void onGenericCategory( GenericCategoryEvent event) {}
    public void onGenericCategoryUpdate( GenericCategoryUpdateEvent event) {}
    public void onGenericGuild( GenericGuildEvent event) {}
    public void onGenericGuildUpdate( GenericGuildUpdateEvent event) {}
    public void onGenericGuildMember( GenericGuildMemberEvent event) {}
    public void onGenericGuildMemberUpdate( GenericGuildMemberUpdateEvent event) {}
    public void onGenericGuildVoice( GenericGuildVoiceEvent event) {}
    public void onGenericRole( GenericRoleEvent event) {}
    public void onGenericRoleUpdate( GenericRoleUpdateEvent event) {}
    public void onGenericEmote( GenericEmoteEvent event) {}
    public void onGenericEmoteUpdate( GenericEmoteUpdateEvent event) {}

    @Override
    public final void onEvent( GenericEvent event)
    {
        onGenericEvent(event);
        if (event instanceof UpdateEvent)
            onGenericUpdate((UpdateEvent<?, ?>) event);
        else if (event instanceof RawGatewayEvent)
            onRawGateway((RawGatewayEvent) event);

        //JDA Events
        if (event instanceof ReadyEvent)
            onReady((ReadyEvent) event);
        else if (event instanceof ResumedEvent)
            onResume((ResumedEvent) event);
        else if (event instanceof ReconnectedEvent)
            onReconnect((ReconnectedEvent) event);
        else if (event instanceof DisconnectEvent)
            onDisconnect((DisconnectEvent) event);
        else if (event instanceof ShutdownEvent)
            onShutdown((ShutdownEvent) event);
        else if (event instanceof StatusChangeEvent)
            onStatusChange((StatusChangeEvent) event);
        else if (event instanceof ExceptionEvent)
            onException((ExceptionEvent) event);
        else if (event instanceof GatewayPingEvent)
            onGatewayPing((GatewayPingEvent) event);

        //Message Events
        //Guild (TextChannel) Message Events
        else if (event instanceof GuildMessageReceivedEvent)
            onGuildMessageReceived((GuildMessageReceivedEvent) event);
        else if (event instanceof GuildMessageUpdateEvent)
            onGuildMessageUpdate((GuildMessageUpdateEvent) event);
        else if (event instanceof GuildMessageDeleteEvent)
            onGuildMessageDelete((GuildMessageDeleteEvent) event);
        else if (event instanceof GuildMessageEmbedEvent)
            onGuildMessageEmbed((GuildMessageEmbedEvent) event);
        else if (event instanceof GuildMessageReactionAddEvent)
            onGuildMessageReactionAdd((GuildMessageReactionAddEvent) event);
        else if (event instanceof GuildMessageReactionRemoveEvent)
            onGuildMessageReactionRemove((GuildMessageReactionRemoveEvent) event);
        else if (event instanceof GuildMessageReactionRemoveAllEvent)
            onGuildMessageReactionRemoveAll((GuildMessageReactionRemoveAllEvent) event);

        //Private Message Events
        else if (event instanceof PrivateMessageReceivedEvent)
            onPrivateMessageReceived((PrivateMessageReceivedEvent) event);
        else if (event instanceof PrivateMessageUpdateEvent)
            onPrivateMessageUpdate((PrivateMessageUpdateEvent) event);
        else if (event instanceof PrivateMessageDeleteEvent)
            onPrivateMessageDelete((PrivateMessageDeleteEvent) event);
        else if (event instanceof PrivateMessageEmbedEvent)
            onPrivateMessageEmbed((PrivateMessageEmbedEvent) event);
        else if (event instanceof PrivateMessageReactionAddEvent)
            onPrivateMessageReactionAdd((PrivateMessageReactionAddEvent) event);
        else if (event instanceof PrivateMessageReactionRemoveEvent)
            onPrivateMessageReactionRemove((PrivateMessageReactionRemoveEvent) event);

        //Combined Message Events (Combines Guild and Private message into 1 event)
        else if (event instanceof MessageReceivedEvent)
            onMessageReceived((MessageReceivedEvent) event);
        else if (event instanceof MessageUpdateEvent)
            onMessageUpdate((MessageUpdateEvent) event);
        else if (event instanceof MessageDeleteEvent)
            onMessageDelete((MessageDeleteEvent) event);
        else if (event instanceof MessageBulkDeleteEvent)
            onMessageBulkDelete((MessageBulkDeleteEvent) event);
        else if (event instanceof MessageEmbedEvent)
            onMessageEmbed((MessageEmbedEvent) event);
        else if (event instanceof MessageReactionAddEvent)
            onMessageReactionAdd((MessageReactionAddEvent) event);
        else if (event instanceof MessageReactionRemoveEvent)
            onMessageReactionRemove((MessageReactionRemoveEvent) event);
        else if (event instanceof MessageReactionRemoveAllEvent)
            onMessageReactionRemoveAll((MessageReactionRemoveAllEvent) event);

        //User Events
        else if (event instanceof UserUpdateNameEvent)
            onUserUpdateName((UserUpdateNameEvent) event);
        else if (event instanceof UserUpdateDiscriminatorEvent)
            onUserUpdateDiscriminator((UserUpdateDiscriminatorEvent) event);
        else if (event instanceof UserUpdateAvatarEvent)
            onUserUpdateAvatar((UserUpdateAvatarEvent) event);
        else if (event instanceof UserUpdateActivityOrderEvent)
            onUserUpdateActivityOrder((UserUpdateActivityOrderEvent) event);
        else if (event instanceof UserUpdateOnlineStatusEvent)
            onUserUpdateOnlineStatus((UserUpdateOnlineStatusEvent) event);
        else if (event instanceof UserTypingEvent)
            onUserTyping((UserTypingEvent) event);
        else if (event instanceof UserActivityStartEvent)
            onUserActivityStart((UserActivityStartEvent) event);
        else if (event instanceof UserActivityEndEvent)
            onUserActivityEnd((UserActivityEndEvent) event);

        //Self Events
        else if (event instanceof SelfUpdateAvatarEvent)
            onSelfUpdateAvatar((SelfUpdateAvatarEvent) event);
        else if (event instanceof SelfUpdateEmailEvent)
            onSelfUpdateEmail((SelfUpdateEmailEvent) event);
        else if (event instanceof SelfUpdateMFAEvent)
            onSelfUpdateMFA((SelfUpdateMFAEvent) event);
        else if (event instanceof SelfUpdateNameEvent)
            onSelfUpdateName((SelfUpdateNameEvent) event);
        else if (event instanceof SelfUpdateVerifiedEvent)
            onSelfUpdateVerified((SelfUpdateVerifiedEvent) event);

        //StoreChannel Events
        else if (event instanceof StoreChannelCreateEvent)
            onStoreChannelCreate((StoreChannelCreateEvent) event);
        else if (event instanceof StoreChannelDeleteEvent)
            onStoreChannelDelete((StoreChannelDeleteEvent) event);
        else if (event instanceof StoreChannelUpdateNameEvent)
            onStoreChannelUpdateName((StoreChannelUpdateNameEvent) event);
        else if (event instanceof StoreChannelUpdatePositionEvent)
            onStoreChannelUpdatePosition((StoreChannelUpdatePositionEvent) event);
        else if (event instanceof StoreChannelUpdatePermissionsEvent)
            onStoreChannelUpdatePermissions((StoreChannelUpdatePermissionsEvent) event);

        //TextChannel Events
        else if (event instanceof TextChannelCreateEvent)
            onTextChannelCreate((TextChannelCreateEvent) event);
        else if (event instanceof TextChannelUpdateNameEvent)
            onTextChannelUpdateName((TextChannelUpdateNameEvent) event);
        else if (event instanceof TextChannelUpdateTopicEvent)
            onTextChannelUpdateTopic((TextChannelUpdateTopicEvent) event);
        else if (event instanceof TextChannelUpdatePositionEvent)
            onTextChannelUpdatePosition((TextChannelUpdatePositionEvent) event);
        else if (event instanceof TextChannelUpdatePermissionsEvent)
            onTextChannelUpdatePermissions((TextChannelUpdatePermissionsEvent) event);
        else if (event instanceof TextChannelUpdateNSFWEvent)
            onTextChannelUpdateNSFW((TextChannelUpdateNSFWEvent) event);
        else if (event instanceof TextChannelUpdateParentEvent)
            onTextChannelUpdateParent((TextChannelUpdateParentEvent) event);
        else if (event instanceof TextChannelUpdateSlowmodeEvent)
            onTextChannelUpdateSlowmode((TextChannelUpdateSlowmodeEvent) event);
        else if (event instanceof TextChannelDeleteEvent)
        onTextChannelDelete((TextChannelDeleteEvent) event);

        //VoiceChannel Events
        else if (event instanceof VoiceChannelCreateEvent)
            onVoiceChannelCreate((VoiceChannelCreateEvent) event);
        else if (event instanceof VoiceChannelUpdateNameEvent)
            onVoiceChannelUpdateName((VoiceChannelUpdateNameEvent) event);
        else if (event instanceof VoiceChannelUpdatePositionEvent)
            onVoiceChannelUpdatePosition((VoiceChannelUpdatePositionEvent) event);
        else if (event instanceof VoiceChannelUpdateUserLimitEvent)
            onVoiceChannelUpdateUserLimit((VoiceChannelUpdateUserLimitEvent) event);
        else if (event instanceof VoiceChannelUpdateBitrateEvent)
            onVoiceChannelUpdateBitrate((VoiceChannelUpdateBitrateEvent) event);
        else if (event instanceof VoiceChannelUpdatePermissionsEvent)
            onVoiceChannelUpdatePermissions((VoiceChannelUpdatePermissionsEvent) event);
        else if (event instanceof VoiceChannelUpdateParentEvent)
            onVoiceChannelUpdateParent((VoiceChannelUpdateParentEvent) event);
        else if (event instanceof VoiceChannelDeleteEvent)
            onVoiceChannelDelete((VoiceChannelDeleteEvent) event);

        //Category Events
        else if (event instanceof CategoryCreateEvent)
            onCategoryCreate((CategoryCreateEvent) event);
        else if (event instanceof CategoryUpdateNameEvent)
            onCategoryUpdateName((CategoryUpdateNameEvent) event);
        else if (event instanceof CategoryUpdatePositionEvent)
            onCategoryUpdatePosition((CategoryUpdatePositionEvent) event);
        else if (event instanceof CategoryUpdatePermissionsEvent)
            onCategoryUpdatePermissions((CategoryUpdatePermissionsEvent) event);
        else if (event instanceof CategoryDeleteEvent)
            onCategoryDelete((CategoryDeleteEvent) event);

        //PrivateChannel Events
        else if (event instanceof PrivateChannelCreateEvent)
            onPrivateChannelCreate((PrivateChannelCreateEvent) event);
        else if (event instanceof PrivateChannelDeleteEvent)
            onPrivateChannelDelete((PrivateChannelDeleteEvent) event);

        //Guild Events
        else if (event instanceof GuildReadyEvent)
            onGuildReady((GuildReadyEvent) event);
        else if (event instanceof GuildJoinEvent)
            onGuildJoin((GuildJoinEvent) event);
        else if (event instanceof GuildLeaveEvent)
            onGuildLeave((GuildLeaveEvent) event);
        else if (event instanceof GuildAvailableEvent)
            onGuildAvailable((GuildAvailableEvent) event);
        else if (event instanceof GuildUnavailableEvent)
            onGuildUnavailable((GuildUnavailableEvent) event);
        else if (event instanceof UnavailableGuildJoinedEvent)
            onUnavailableGuildJoined((UnavailableGuildJoinedEvent) event);
        else if (event instanceof UnavailableGuildLeaveEvent)
            onUnavailableGuildLeave((UnavailableGuildLeaveEvent) event);
        else if (event instanceof GuildBanEvent)
            onGuildBan((GuildBanEvent) event);
        else if (event instanceof GuildUnbanEvent)
            onGuildUnban((GuildUnbanEvent) event);

        //Guild Update Events
        else if (event instanceof GuildUpdateAfkChannelEvent)
            onGuildUpdateAfkChannel((GuildUpdateAfkChannelEvent) event);
        else if (event instanceof GuildUpdateSystemChannelEvent)
            onGuildUpdateSystemChannel((GuildUpdateSystemChannelEvent) event);
        else if (event instanceof GuildUpdateAfkTimeoutEvent)
            onGuildUpdateAfkTimeout((GuildUpdateAfkTimeoutEvent) event);
        else if (event instanceof GuildUpdateExplicitContentLevelEvent)
            onGuildUpdateExplicitContentLevel((GuildUpdateExplicitContentLevelEvent) event);
        else if (event instanceof GuildUpdateIconEvent)
            onGuildUpdateIcon((GuildUpdateIconEvent) event);
        else if (event instanceof GuildUpdateMFALevelEvent)
            onGuildUpdateMFALevel((GuildUpdateMFALevelEvent) event);
        else if (event instanceof GuildUpdateNameEvent)
            onGuildUpdateName((GuildUpdateNameEvent) event);
        else if (event instanceof GuildUpdateNotificationLevelEvent)
            onGuildUpdateNotificationLevel((GuildUpdateNotificationLevelEvent) event);
        else if (event instanceof GuildUpdateOwnerEvent)
            onGuildUpdateOwner((GuildUpdateOwnerEvent) event);
        else if (event instanceof GuildUpdateRegionEvent)
            onGuildUpdateRegion((GuildUpdateRegionEvent) event);
        else if (event instanceof GuildUpdateSplashEvent)
            onGuildUpdateSplash((GuildUpdateSplashEvent) event);
        else if (event instanceof GuildUpdateVerificationLevelEvent)
            onGuildUpdateVerificationLevel((GuildUpdateVerificationLevelEvent) event);
        else if (event instanceof GuildUpdateFeaturesEvent)
            onGuildUpdateFeatures((GuildUpdateFeaturesEvent) event);
        else if (event instanceof GuildUpdateVanityCodeEvent)
            onGuildUpdateVanityCode((GuildUpdateVanityCodeEvent) event);
        else if (event instanceof GuildUpdateBannerEvent)
            onGuildUpdateBanner((GuildUpdateBannerEvent) event);
        else if (event instanceof GuildUpdateDescriptionEvent)
            onGuildUpdateDescription((GuildUpdateDescriptionEvent) event);
        else if (event instanceof GuildUpdateBoostTierEvent)
            onGuildUpdateBoostTier((GuildUpdateBoostTierEvent) event);
        else if (event instanceof GuildUpdateBoostCountEvent)
            onGuildUpdateBoostCount((GuildUpdateBoostCountEvent) event);
        else if (event instanceof GuildUpdateMaxMembersEvent)
            onGuildUpdateMaxMembers((GuildUpdateMaxMembersEvent) event);
        else if (event instanceof GuildUpdateMaxPresencesEvent)
            onGuildUpdateMaxPresences((GuildUpdateMaxPresencesEvent) event);

        //Guild Member Events
        else if (event instanceof GuildMemberJoinEvent)
            onGuildMemberJoin((GuildMemberJoinEvent) event);
        else if (event instanceof GuildMemberLeaveEvent)
            onGuildMemberLeave((GuildMemberLeaveEvent) event);
        else if (event instanceof GuildMemberRoleAddEvent)
            onGuildMemberRoleAdd((GuildMemberRoleAddEvent) event);
        else if (event instanceof GuildMemberRoleRemoveEvent)
            onGuildMemberRoleRemove((GuildMemberRoleRemoveEvent) event);

        //Guild Member Update Events
        else if (event instanceof GuildMemberUpdateNicknameEvent)
            onGuildMemberUpdateNickname((GuildMemberUpdateNicknameEvent) event);
        else if (event instanceof GuildMemberUpdateBoostTimeEvent)
            onGuildMemberUpdateBoostTime((GuildMemberUpdateBoostTimeEvent) event);

        //Guild Voice Events
        else if (event instanceof GuildVoiceJoinEvent)
            onGuildVoiceJoin((GuildVoiceJoinEvent) event);
        else if (event instanceof GuildVoiceMoveEvent)
            onGuildVoiceMove((GuildVoiceMoveEvent) event);
        else if (event instanceof GuildVoiceLeaveEvent)
            onGuildVoiceLeave((GuildVoiceLeaveEvent) event);
        else if (event instanceof GuildVoiceMuteEvent)
            onGuildVoiceMute((GuildVoiceMuteEvent) event);
        else if (event instanceof GuildVoiceDeafenEvent)
            onGuildVoiceDeafen((GuildVoiceDeafenEvent) event);
        else if (event instanceof GuildVoiceGuildMuteEvent)
            onGuildVoiceGuildMute((GuildVoiceGuildMuteEvent) event);
        else if (event instanceof GuildVoiceGuildDeafenEvent)
            onGuildVoiceGuildDeafen((GuildVoiceGuildDeafenEvent) event);
        else if (event instanceof GuildVoiceSelfMuteEvent)
            onGuildVoiceSelfMute((GuildVoiceSelfMuteEvent) event);
        else if (event instanceof GuildVoiceSelfDeafenEvent)
            onGuildVoiceSelfDeafen((GuildVoiceSelfDeafenEvent) event);
        else if (event instanceof GuildVoiceSuppressEvent)
            onGuildVoiceSuppress((GuildVoiceSuppressEvent) event);

        //Role Events
        else if (event instanceof RoleCreateEvent)
            onRoleCreate((RoleCreateEvent) event);
        else if (event instanceof RoleDeleteEvent)
            onRoleDelete((RoleDeleteEvent) event);

        //Role Update Events
        else if (event instanceof RoleUpdateColorEvent)
            onRoleUpdateColor(((RoleUpdateColorEvent) event));
        else if (event instanceof RoleUpdateHoistedEvent)
            onRoleUpdateHoisted(((RoleUpdateHoistedEvent) event));
        else if (event instanceof RoleUpdateMentionableEvent)
            onRoleUpdateMentionable((RoleUpdateMentionableEvent) event);
        else if (event instanceof RoleUpdateNameEvent)
            onRoleUpdateName(((RoleUpdateNameEvent) event));
        else if (event instanceof RoleUpdatePermissionsEvent)
            onRoleUpdatePermissions(((RoleUpdatePermissionsEvent) event));
        else if (event instanceof RoleUpdatePositionEvent)
            onRoleUpdatePosition(((RoleUpdatePositionEvent) event));

        //Emote Events
        else if (event instanceof EmoteAddedEvent)
            onEmoteAdded((EmoteAddedEvent) event);
        else if (event instanceof EmoteRemovedEvent)
            onEmoteRemoved((EmoteRemovedEvent) event);

        //Emote Update Events
        else if (event instanceof EmoteUpdateNameEvent)
            onEmoteUpdateName((EmoteUpdateNameEvent) event);
        else if (event instanceof EmoteUpdateRolesEvent)
            onEmoteUpdateRoles((EmoteUpdateRolesEvent) event);

        // Debug Events
        else if (event instanceof HttpRequestEvent)
            onHttpRequest((HttpRequestEvent) event);

        //Generic subclasses - combining multiple events
        if (event instanceof GuildVoiceUpdateEvent)
            onGuildVoiceUpdate((GuildVoiceUpdateEvent) event);

        //Generic Events
        //Start a new if statement so that these are no overridden by the above events.
        if (event instanceof GenericMessageReactionEvent)
            onGenericMessageReaction((GenericMessageReactionEvent) event);
        else if (event instanceof GenericPrivateMessageReactionEvent)
            onGenericPrivateMessageReaction((GenericPrivateMessageReactionEvent) event);
        else if (event instanceof GenericStoreChannelUpdateEvent)
            onGenericStoreChannelUpdate((GenericStoreChannelUpdateEvent) event);
        else if (event instanceof GenericTextChannelUpdateEvent)
            onGenericTextChannelUpdate((GenericTextChannelUpdateEvent) event);
        else if (event instanceof GenericCategoryUpdateEvent)
            onGenericCategoryUpdate((GenericCategoryUpdateEvent) event);
        else if (event instanceof GenericGuildMessageReactionEvent)
            onGenericGuildMessageReaction((GenericGuildMessageReactionEvent) event);
        else if (event instanceof GenericVoiceChannelUpdateEvent)
            onGenericVoiceChannelUpdate((GenericVoiceChannelUpdateEvent) event);
        else if (event instanceof GenericGuildUpdateEvent)
            onGenericGuildUpdate((GenericGuildUpdateEvent) event);
        else if (event instanceof GenericGuildMemberUpdateEvent)
            onGenericGuildMemberUpdate((GenericGuildMemberUpdateEvent) event);
        else if (event instanceof GenericGuildVoiceEvent)
            onGenericGuildVoice((GenericGuildVoiceEvent) event);
        else if (event instanceof GenericRoleUpdateEvent)
            onGenericRoleUpdate(((GenericRoleUpdateEvent) event));
        else if (event instanceof GenericEmoteUpdateEvent)
            onGenericEmoteUpdate((GenericEmoteUpdateEvent) event);
        else if (event instanceof GenericUserPresenceEvent)
            onGenericUserPresence((GenericUserPresenceEvent) event);

        //Generic events that have generic subclasses (the subclasses as above).
        if (event instanceof GenericMessageEvent)
            onGenericMessage((GenericMessageEvent) event);
        else if (event instanceof GenericPrivateMessageEvent)
            onGenericPrivateMessage((GenericPrivateMessageEvent) event);
        else if (event instanceof GenericGuildMessageEvent)
            onGenericGuildMessage((GenericGuildMessageEvent) event);
        else if (event instanceof GenericGuildMemberEvent)
            onGenericGuildMember((GenericGuildMemberEvent) event);
        else if (event instanceof GenericUserEvent)
            onGenericUser((GenericUserEvent) event);
        else if (event instanceof GenericSelfUpdateEvent)
            onGenericSelfUpdate((GenericSelfUpdateEvent) event);
        else if (event instanceof GenericStoreChannelEvent)
            onGenericStoreChannel((GenericStoreChannelEvent) event);
        else if (event instanceof GenericTextChannelEvent)
            onGenericTextChannel((GenericTextChannelEvent) event);
        else if (event instanceof GenericVoiceChannelEvent)
            onGenericVoiceChannel((GenericVoiceChannelEvent) event);
        else if (event instanceof GenericCategoryEvent)
            onGenericCategory((GenericCategoryEvent) event);
        else if (event instanceof GenericRoleEvent)
            onGenericRole((GenericRoleEvent) event);
        else if (event instanceof GenericEmoteEvent)
            onGenericEmote((GenericEmoteEvent) event);

        //Generic events that have 2 levels of generic subclasses
        if (event instanceof GenericGuildEvent)
            onGenericGuild((GenericGuildEvent) event);
    }
}

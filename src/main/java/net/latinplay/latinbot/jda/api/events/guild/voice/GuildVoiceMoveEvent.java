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

package net.latinplay.latinbot.jda.api.events.guild.voice;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Member;
import net.latinplay.latinbot.jda.api.entities.VoiceChannel;

/**
 * Indicates that a {@link Member Member} moves between {@link VoiceChannel VoiceChannels}.
 *
 * <p><b>When the {@link Member Member} is leaving a {@link GuildVoiceLeaveEvent GuildVoiceLeaveEvent} is fired instead</b>
 *
 * <p>Can be used to detect when a member moves from one voice channel to another in the same guild.
 *
 * @see GuildVoiceUpdateEvent GuildVoiceUpdateEvent
 */
public class GuildVoiceMoveEvent extends GenericGuildVoiceUpdateEvent
{
    public GuildVoiceMoveEvent( JDA api, long responseNumber,  Member member,  VoiceChannel channelLeft)
    {
        super(api, responseNumber, member, channelLeft, member.getVoiceState().getChannel());
    }

    
    @Override
    public VoiceChannel getChannelLeft()
    {
        return super.getChannelLeft();
    }

    
    @Override
    public VoiceChannel getChannelJoined()
    {
        return super.getChannelJoined();
    }

    
    @Override
    public VoiceChannel getOldValue()
    {
        return super.getOldValue();
    }

    
    @Override
    public VoiceChannel getNewValue()
    {
        return super.getNewValue();
    }
}

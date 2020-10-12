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

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.JDABuilder;
import net.latinplay.latinbot.jda.api.events.GenericEvent;

/**
 * JDA pushes {@link GenericEvent GenericEvents} to the registered EventListeners.
 *
 * <p>Register an EventListener with either a {@link JDA JDA} object
 * <br>or the {@link JDABuilder JDABuilder}.
 *
 * <p><b>Examples: </b>
 * <br>
 * <code>
 *     JDA jda = new {@link JDABuilder JDABuilder}("token").{@link JDABuilder#addEventListeners(Object...) addEventListeners(listeners)}.{@link JDABuilder#build() build()};<br>
 *     {@link JDA#addEventListener(Object...) jda.addEventListener(listeners)};
 * </code>
 *
 * @see ListenerAdapter
 * @see InterfacedEventManager
 */
@FunctionalInterface
public interface EventListener
{
    /**
     * Handles any {@link GenericEvent GenericEvent}.
     *
     * <p>To get specific events with Methods like {@code onMessageReceived(MessageReceivedEvent event)}
     * take a look at: {@link ListenerAdapter ListenerAdapter}
     *
     * @param  event
     *         The Event to handle.
     */
    void onEvent( GenericEvent event);
}

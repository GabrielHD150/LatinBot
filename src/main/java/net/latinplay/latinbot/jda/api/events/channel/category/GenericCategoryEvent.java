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

package net.latinplay.latinbot.jda.api.events.channel.category;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.entities.Category;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.events.Event;

/**
 * Indicates that a {@link Category Category} was created/deleted/updated.
 * <br>Every category event is a subclass of this event and can be casted
 *
 * <p>Can be used to detect that any category event was fired
 */
public abstract class GenericCategoryEvent extends Event
{
    protected final Category category;

    public GenericCategoryEvent( JDA api, long responseNumber,  Category category)
    {
        super(api, responseNumber);
        this.category = category;
    }

    /**
     * The responsible {@link Category Category}
     *
     * @return The Category
     */
    
    public Category getCategory()
    {
        return category;
    }

    /**
     * The snowflake ID for the responsible {@link Category Category}
     *
     * @return The ID for the category
     */
    
    public String getId()
    {
        return Long.toUnsignedString(getIdLong());
    }

    /**
     * The snowflake ID for the responsible {@link Category Category}
     *
     * @return The ID for the category
     */
    public long getIdLong()
    {
        return category.getIdLong();
    }

    /**
     * The {@link Guild Guild}
     * the responsible {@link Category Category} is part of.
     *
     * @return The {@link Guild Guild}
     */
    
    public Guild getGuild()
    {
        return category.getGuild();
    }
}

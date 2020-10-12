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

package net.latinplay.latinbot.jda.internal.managers;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.exceptions.RateLimitedException;
import net.latinplay.latinbot.jda.api.managers.Manager;
import net.latinplay.latinbot.jda.internal.requests.Route;
import net.latinplay.latinbot.jda.internal.requests.restaction.AuditableRestActionImpl;
import net.latinplay.latinbot.jda.internal.utils.Checks;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class ManagerBase<M extends Manager<M>> extends AuditableRestActionImpl<Void> implements Manager<M>
{
    private static boolean enablePermissionChecks = true;
    protected long set = 0;

    public static void setPermissionChecksEnabled(boolean enable)
    {
        enablePermissionChecks = enable;
    }

    public static boolean isPermissionChecksEnabled()
    {
        return enablePermissionChecks;
    }

    protected ManagerBase(JDA api, Route.CompiledRoute route)
    {
        super(api, route);
    }


    @Override
    @SuppressWarnings("unchecked")
    public M setCheck(BooleanSupplier checks)
    {
        return (M) super.setCheck(checks);
    }


    @Override
    @SuppressWarnings("unchecked")
    public M reset(long fields)
    {
        //logic explanation:
        //0101 = fields
        //1010 = ~fields
        //1100 = set
        //1000 = set & ~fields
        set &= ~fields;
        return (M) this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public M reset(long... fields)
    {
        Checks.notNull(fields, "Fields");
        //trivial case
        if (fields.length == 0)
            return (M) this;
        else if (fields.length == 1)
            return reset(fields[0]);

        //complex case
        long sum = fields[0];
        for (int i = 1; i < fields.length; i++)
            sum |= fields[i];
        return reset(sum);
    }


    @Override
    @SuppressWarnings("unchecked")
    public M reset()
    {
        set = 0;
        return (M) this;
    }

    @Override
    public void queue(Consumer<? super Void> success, Consumer<? super Throwable> failure)
    {
        if (shouldUpdate())
            super.queue(success, failure);
        else if (success != null)
            success.accept(null);
        else
            getDefaultSuccess().accept(null);
    }

    @Override
    public Void complete(boolean shouldQueue) throws RateLimitedException
    {
        if (shouldUpdate())
            return super.complete(shouldQueue);
        return null;
    }

    @Override
    protected BooleanSupplier finalizeChecks()
    {
        return enablePermissionChecks ? this::checkPermissions : super.finalizeChecks();
    }

    protected boolean shouldUpdate()
    {
        return set != 0;
    }

    protected boolean shouldUpdate(long bit)
    {
        return (set & bit) == bit;
    }

    protected <E> void withLock(E object, Consumer<? super E> consumer)
    {
        synchronized (object)
        {
            consumer.accept(object);
        }
    }

    protected boolean checkPermissions()
    {
        return true;
    }
}

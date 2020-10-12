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

package net.latinplay.latinbot.jda.internal.requests;

import net.latinplay.latinbot.jda.api.JDA;
import net.latinplay.latinbot.jda.api.exceptions.RateLimitedException;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.AuditableRestAction;

import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredRestAction<T, R extends RestAction<T>> implements AuditableRestAction<T>
{
    private final JDA api;
    private final Class<T> type;
    private final Supplier<T> valueSupplier;
    private final Supplier<R> actionSupplier;

    private BooleanSupplier isAction;
    private BooleanSupplier transitiveChecks;

    public DeferredRestAction(JDA api, Supplier<R> actionSupplier)
    {
        this(api, null, null, actionSupplier);
    }

    public DeferredRestAction(JDA api, Class<T> type,
                              Supplier<T> valueSupplier,
                              Supplier<R> actionSupplier)
    {
        this.api = api;
        this.type = type;
        this.valueSupplier = valueSupplier;
        this.actionSupplier = actionSupplier;
    }


    @Override
    public JDA getJDA()
    {
        return api;
    }


    @Override
    public AuditableRestAction<T> reason(String reason)
    {
        return this;
    }


    @Override
    public AuditableRestAction<T> setCheck(BooleanSupplier checks)
    {
        this.transitiveChecks = checks;
        return this;
    }

    public AuditableRestAction<T> setCacheCheck(BooleanSupplier checks)
    {
        this.isAction = checks;
        return this;
    }

    @Override
    public void queue(Consumer<? super T> success, Consumer<? super Throwable> failure)
    {
        Consumer<? super T> finalSuccess;
        if (success != null)
            finalSuccess = success;
        else
            finalSuccess = RestAction.getDefaultSuccess();

        if (type == null)
        {
            BooleanSupplier checks = this.isAction;
            if (checks != null && checks.getAsBoolean())
                actionSupplier.get().queue(success, failure);
            else
                finalSuccess.accept(null);
            return;
        }

        T value = valueSupplier.get();
        if (value == null)
        {
            getAction().queue(success, failure);
        }
        else
        {
            finalSuccess.accept(value);
        }
    }


    @Override
    public CompletableFuture<T> submit(boolean shouldQueue)
    {
        if (type == null)
        {
            BooleanSupplier checks = this.isAction;
            if (checks != null && checks.getAsBoolean())
                return actionSupplier.get().submit(shouldQueue);
            return CompletableFuture.completedFuture(null);
        }
        T value = valueSupplier.get();
        if (value != null)
            return CompletableFuture.completedFuture(value);
        return getAction().submit(shouldQueue);
    }

    @Override
    public T complete(boolean shouldQueue) throws RateLimitedException
    {
        if (type == null)
        {
            BooleanSupplier checks = this.isAction;
            if (checks != null && checks.getAsBoolean())
                return actionSupplier.get().complete(shouldQueue);
            return null;
        }
        T value = valueSupplier.get();
        if (value != null)
            return value;
        return getAction().complete(shouldQueue);
    }

    @SuppressWarnings("unchecked")
    private R getAction()
    {
        return (R) actionSupplier.get().setCheck(transitiveChecks);
    }
}

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

package net.latinplay.latinbot.jda.internal.requests.restaction.operator;

import net.latinplay.latinbot.jda.api.exceptions.RateLimitedException;
import net.latinplay.latinbot.jda.api.requests.RestAction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DelayRestAction<T> extends RestActionOperator<T, T>
{
    private final TimeUnit unit;
    private final long delay;
    private final ScheduledExecutorService scheduler;

    public DelayRestAction(RestAction<T> action, TimeUnit unit, long delay, ScheduledExecutorService scheduler)
    {
        super(action);
        this.unit = unit;
        this.delay = delay;
        this.scheduler = scheduler == null ? action.getJDA().getRateLimitPool() : scheduler;
    }

    @Override
    public void queue( Consumer<? super T> success,  Consumer<? super Throwable> failure)
    {
        action.queue((result) ->
            scheduler.schedule(() ->
                doSuccess(success, result),
            delay, unit),
        contextWrap(failure));
    }

    @Override
    public T complete(boolean shouldQueue) throws RateLimitedException
    {
        T result = action.complete(shouldQueue);
        try
        {
            unit.sleep(delay);
            return result;
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public CompletableFuture<T> submit(boolean shouldQueue)
    {
        CompletableFuture<T> future = new CompletableFuture<>();
        queue(future::complete, future::completeExceptionally);
        return future;
    }
}

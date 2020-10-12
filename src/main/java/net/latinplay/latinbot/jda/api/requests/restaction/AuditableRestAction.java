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

import net.latinplay.latinbot.jda.api.AccountType;
import net.latinplay.latinbot.jda.api.audit.AuditLogEntry;
import net.latinplay.latinbot.jda.api.audit.ThreadLocalReason;
import net.latinplay.latinbot.jda.api.entities.Guild;
import net.latinplay.latinbot.jda.api.entities.User;
import net.latinplay.latinbot.jda.api.requests.RestAction;
import net.latinplay.latinbot.jda.api.requests.restaction.pagination.AuditLogPaginationAction;

import java.util.function.BooleanSupplier;

/**
 * Extension of RestAction to allow setting a reason, only available to accounts of {@link AccountType#BOT AccountType.BOT}
 *
 * <p>This will automatically use the {@link ThreadLocalReason ThreadLocalReason} if no
 * reason was specified via {@link #reason(String)}.
 *
 * @param  <T>
 *         The return type
 *
 * @since  3.3.0
 */
public interface AuditableRestAction<T> extends RestAction<T>
{
    /**
     * Applies the specified reason as audit-log reason field.
     * <br>When the provided reason is empty or {@code null} it will be treated as not set.
     *
     * <p>Reasons for any AuditableRestAction may be retrieved
     * via {@link AuditLogEntry#getReason() AuditLogEntry.getReason()}
     * in iterable {@link AuditLogPaginationAction AuditLogPaginationActions}
     * from {@link Guild#retrieveAuditLogs() Guild.retrieveAuditLogs()}!
     *
     * <p>This will specify the reason via the {@code X-Audit-Log-Reason} Request Header.
     * <br><b>Note: This may not be available to accounts for {@link AccountType#CLIENT AccountType.CLIENT}.
     * <br>Using methods with a reason parameter will always work and <u>override</u> this header.</b>
     * (ct. {@link Guild#ban(User, int, String) Guild.ban(User, int, String)})
     *
     * @param  reason
     *         The reason for this action which should be logged in the Guild's AuditLogs
     *
     * @return The current AuditableRestAction instance for chaining convenience
     *
     * @see    ThreadLocalReason
     */
    
    AuditableRestAction<T> reason( String reason);

    /**
     * {@inheritDoc}
     */
    
    @Override
    AuditableRestAction<T> setCheck( BooleanSupplier checks);
}

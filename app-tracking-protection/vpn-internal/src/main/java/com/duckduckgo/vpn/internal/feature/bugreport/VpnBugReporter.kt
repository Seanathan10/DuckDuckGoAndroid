/*
 * Copyright (c) 2021 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.vpn.internal.feature.bugreport

import com.duckduckgo.common.utils.DispatcherProvider
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.mobile.android.vpn.state.VpnStateCollector
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlinx.coroutines.withContext
import logcat.logcat

interface VpnBugReporter {
    suspend fun generateBugReport(): String
}

@ContributesBinding(ActivityScope::class)
class RealVpnBugReporter @Inject constructor(
    private val vpnStateCollector: VpnStateCollector,
    private val dispatcherProvider: DispatcherProvider,
) : VpnBugReporter {
    override suspend fun generateBugReport(): String {
        return withContext(dispatcherProvider.io()) {
            val state = vpnStateCollector.collectVpnState()
            val bugreport = state.toString(2)
            logcat { "AppTP bugreport generated: $bugreport" }

            bugreport
        }
    }
}

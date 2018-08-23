/*
 * Copyright (C) 2018 Embest
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.embest.gps.gnsstest.utils

import android.location.Location

class CepResult {
    var timeOut = 0
        internal set
    var maxTTFF = 0f
        internal set
    var avgTTFF = 0f
        internal set
    var minTTFF = 0f
        internal set
    var p95TTFF = 0f
        internal set
    var p67TTFF = 0f
        internal set
    var p50TTFF = 0f
        internal set
    var count = 0
        internal set

    var maxAcc = 0f
        internal set
    var avgAcc = 0f
        internal set
    var minAcc = 0f
        internal set
    var p95Acc = 0f
        internal set
    var p67Acc = 0f
        internal set
    var p50Acc = 0f
        internal set

    var trueLocation: Location? = null
        internal set
    var locationType: Boolean = false
        internal set

    init {
        trueLocation = Location("")
    }
}

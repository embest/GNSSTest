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

class GnssSatellite {
    var svid: Int = 0
    var constellation: Int = 0
    var cn0: Float = 0.0f
    var elevations: Float = 0.0f
    var azimuths: Float = 0.0f
    var frequency:Float = 0.0f
    var inUse: Boolean = false

    init {
        reset()
    }

    private fun reset() {
        svid = 0
        constellation = 0
        cn0 = 0.0f
        elevations = 0.0f
        azimuths = 0.0f
        frequency = 0.0f
        inUse = false
    }
    companion object {
        const val GPS_L5_FREQUENCY: Float = 1176450000.0f
        const val QZSS_L5_FREQUENCY: Float = 1176450000.0f
        const val GAL_L5_FREQUENCY: Float = 1176450000.0f
        const val BDS_L5_FREQUENCY: Float = 1176450000.0f
    }
}
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


import java.util.ArrayList

class GnssInfo {
    var time: Long = 0
    var latitude = 0.0
    var longitude = 0.0
    var altitude = 0.0
    var speed = 0.0f
    var accuracy = 0.0f
    var nmea = ""
    var inview = 0
    var inuse = 0
    var ttff = 0f



    val satellites: ArrayList<GnssSatellite> = ArrayList()

    init {
        reset()
    }

    private fun reset() {
        time = 0
        latitude = 0.0
        longitude = 0.0
        altitude = 0.0
        speed = 0f
        accuracy = 0f
        inview = 0
        inuse = 0
        ttff = 0f
        nmea = ""
        satellites.clear()
    }

    fun addSatellite(satellite: GnssSatellite) {
        this.satellites.add(satellite)
    }

    fun cleanSatellites() {
        this.satellites.clear()
    }
}

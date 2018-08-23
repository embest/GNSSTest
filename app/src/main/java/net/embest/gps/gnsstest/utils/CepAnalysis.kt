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
import android.util.Log

import java.util.ArrayList


class CepAnalysis {
    private val mGnssLocation: ArrayList<Location> = ArrayList()
    private val mGnssTTFF: ArrayList<Float> = ArrayList()
    private var mTruePosition: Boolean = false
    private var mTrueLocation: Location? = null
    private var gnssAccAvg = 0f
    private var gnssTTFFAvg = 0f
    private var mTimeOut = 0

    fun getGnssCepResult(): CepResult {
        val result = CepResult()

        result.maxAcc = getGnssAccCep(100)
        result.minAcc = getGnssAccCep(0)
        result.p50Acc = getGnssAccCep(50)
        result.p67Acc = getGnssAccCep(67)
        result.p95Acc = getGnssAccCep(95)
        result.avgAcc = gnssAccAvg

        result.maxTTFF = getGnssTTFFCep(100)
        result.minTTFF = getGnssTTFFCep(0)
        result.p50TTFF = getGnssTTFFCep(50)
        result.p67TTFF = getGnssTTFFCep(67)
        result.p95TTFF = getGnssTTFFCep(95)
        result.avgTTFF = gnssTTFFAvg

        result.timeOut = mTimeOut
        result.trueLocation = mTrueLocation
        result.locationType = mTruePosition
        result.count = mGnssTTFF.size

        return result
    }

    fun initGnssCep(position: Boolean, lon: Double, lat: Double) {
        mGnssLocation.clear()
        mGnssTTFF.clear()
        mTimeOut = 0
        gnssAccAvg = 0f
        gnssTTFFAvg = 0f
        mTrueLocation = Location("")
        this.mTruePosition = position
        if (position) {
            mTrueLocation!!.latitude = lat
            mTrueLocation!!.longitude = lon
        } else {
            mTrueLocation!!.latitude = 0.0
            mTrueLocation!!.longitude = 0.0
        }
        Log.e(TAG, "TruePos:$position Lat:$lat Lon:$lon")
    }

    fun addGnssLocation(latitude: Double, longitude: Double) {
        Log.e(TAG, "addGnssLocation Lat:$latitude Lon:$longitude")
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude
        this.mGnssLocation.add(location)
    }

    fun addGnssTTFF(ttff: Float) {
        Log.e(TAG, "AddTTFF:$ttff")
        this.mGnssTTFF.add(ttff)
    }

    fun addGnssTimeOut(timeout: Float) {
        this.mTimeOut += 1
        this.mGnssTTFF.add(timeout)
        val location = Location("")
        location.latitude = 0.0
        location.longitude = 0.0
        this.mGnssLocation.add(location)
    }

    fun getAllResult(): String {
        var data = "\r\nIndex, TTFF,         Lat,            Lon"
        var i = mGnssLocation.size
        if (i > mGnssTTFF.size) {
            i = mGnssTTFF.size
        }
        for (j in 0 until i) {
            data += "\r\n" + j + ":    " + mGnssTTFF[j] + ", " + mGnssLocation[j].latitude + ", " + mGnssLocation[j].longitude
        }
        return data
    }

    private fun calAveragePosition() {
        var lat = 0.0
        var lon = 0.0
        var i = 0
        for (loc in mGnssLocation) {
            if (loc.latitude > 0 && loc.longitude > 0) {
                lat += loc.latitude
                lon += loc.longitude
                i += 1
            }
        }

        lat /= i
        lon /= i

        mTrueLocation!!.latitude = lat
        mTrueLocation!!.longitude = lon
    }

    private fun getGnssAccCep(pos: Int): Float {
        if (!mTruePosition) {
            calAveragePosition()
        }

        val accList = ArrayList<Float>()

        for (loc in mGnssLocation) {
            val distance = mTrueLocation!!.distanceTo(loc)
            accList.add(distance)
        }

        if (accList.size > 0) {
            accList.sort()
            accList.reverse()
            var accSum = 0f
            for (acc in accList) {
                accSum += acc
            }
            gnssAccAvg = accSum / accList.size
            var index: Int
            if (pos >= 100) {
                index = 0
            } else if (pos <= 0) {
                index = accList.size - 1
            } else {
                index = Math.ceil((accList.size * (100 - pos) / 100.00f).toDouble()).toInt()
                if (index >= accList.size)
                    index = accList.size - 1
            }
//            Log.e(TAG, "Get ACC CEP:$pos Index:$index")
            return accList[index]
        } else
            return 0f
    }

    fun getGnssTTFFCep(pos: Int): Float {
        val ttffList = ArrayList(mGnssTTFF)
        if (ttffList.size > 0) {
            ttffList.sort()
            ttffList.reverse()
            var ttffSum = 0f
            for (ttff in ttffList) {
                ttffSum += ttff
            }
            gnssTTFFAvg = (ttffSum / ttffList.size)
            var index: Int
            if (pos >= 100) {
                index = 0
            } else if (pos <= 0) {
                index = ttffList.size - 1
            } else {
                index = Math.ceil((ttffList.size * (100 - pos) / 100.00f).toDouble()).toInt()
                if (index >= ttffList.size)
                    index = ttffList.size - 1
            }
//            Log.e(TAG, "Get TTFF CEP:$pos Index:$index")
            return ttffList[index]
        } else
            return 0f
    }

    companion object {
        private const val TAG = "GNSSTestCEP"
    }
}
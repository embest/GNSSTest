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

package net.embest.gps.gnsstest.fragments

import net.embest.gps.gnsstest.utils.GnssInfo
import net.embest.gps.gnsstest.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.Fragment

import kotlinx.android.synthetic.main.fragment_info.*
import android.icu.text.SimpleDateFormat
import net.embest.gps.gnsstest.utils.CepResult

import java.util.*

class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onResume() {
        super.onResume()
        layoutInfoResult.visibility = View.GONE //TODO CEP

    }

    fun onUpdateView(info: GnssInfo?, cep: CepResult) {

        if (info!!.time != 0L) {
            val utc = Date(info.time)
            val date = SimpleDateFormat("yy/MM/dd", Locale.US)
            val time = SimpleDateFormat("HH:mm:ss", Locale.US)
            textInfoTime.text = time.format(utc)
            textInfoDate.text = date.format(utc)
        }

        textInfoTtff.text = info.ttff.toString()
        textInfoAcc.text = info.accuracy.toString()
        textInfoLon.text = String.format("%.8f", info.longitude)
        textInfoLat.text = String.format("%.8f", info.latitude)
        textInfoAlt.text = String.format("%.5f", info.altitude)
        textInfoSpeed.text = info.speed.toString()
        textInfoInview.text = info.inview.toString()
        textInfoInuse.text = info.inuse.toString()
        textInfoNmea.text = info.nmea

        textInfoTtffMin.text = cep.minTTFF.toString()
        textInfoTtffMax.text = cep.maxTTFF.toString()
        textInfoTtffP50.text = cep.p50TTFF.toString()
        textInfoTtffP67.text = cep.p67TTFF.toString()
        textInfoTtffP95.text = cep.p95TTFF.toString()
        textInfoTtffAvg.text = cep.avgTTFF.toString()

        textInfoAccMin.text = cep.minAcc.toString()
        textInfoAccMax.text = cep.maxAcc.toString()
        textInfoAccP50.text = cep.p50Acc.toString()
        textInfoAccP67.text = cep.p67Acc.toString()
        textInfoAccP95.text = cep.p95Acc.toString()
        textInfoAccAvg.text = cep.avgAcc.toString()

        textInfoTruePosLat.text = String.format("%.10f", cep.trueLocation!!.latitude)
        textInfoTruePosLon.text = String.format("%.10f", cep.trueLocation!!.longitude)

        if (cep.locationType){
            textInfoPosType.text = resources.getString(R.string.text_loc_true_position)
        }
        else{
            textInfoPosType.text = resources.getString(R.string.text_loc_avg_position)
        }

        textInfoYled.text =  String.format("%d (TimeOut:%d)", cep.count,cep.timeOut)
    }

    companion object {
        fun newInstance() = InfoFragment()
    }
}

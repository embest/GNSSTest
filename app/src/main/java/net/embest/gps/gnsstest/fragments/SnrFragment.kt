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
import net.embest.gps.gnsstest.utils.GnssSatellite
import net.embest.gps.gnsstest.R

import android.support.v4.app.Fragment
import android.content.Context
import android.graphics.Color
import android.location.GnssStatus
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import java.util.ArrayList

class SnrFragment : Fragment() {

    private var mSnrListView: GridView? = null
    private var mSatelliteList: ArrayList<GnssSatellite>? = null
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_snr, container, false)
        mSatelliteList = ArrayList()
        mContext = rootView.context
        mSnrListView = rootView.findViewById(R.id.gridViewSnr)
        return rootView
    }


    fun onUpdateView(info: GnssInfo) {
        val mAdapter = SnrAdapter(this.mContext!!, R.layout.include_snr, info.satellites)
        mSnrListView!!.adapter = mAdapter
    }

    private inner class SnrAdapter internal constructor(context: Context, textViewResourceId: Int, satList: ArrayList<GnssSatellite>) : ArrayAdapter<GnssSatellite>(context, textViewResourceId, satList) {

        private val mAdapterSatList: ArrayList<GnssSatellite> = ArrayList()

        init {
            this.mAdapterSatList.addAll(satList.sortedBy { it.svid }.sortedBy { it.constellation }.sortedBy { it.frequency })
        }

        private inner class ViewHolder {
            internal var mImgSatFlag: ImageView? = null
            internal var mTextSatPrn: TextView? = null
            internal var mTextSatSnr: TextView? = null
            internal var mTextSatElev: TextView? = null
            internal var mTextSatAzi: TextView? = null
            internal var mBarSatSnr: TextView? = null
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
            var convertView = view
            val holder: ViewHolder
            if (convertView == null) {
                val vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = vi.inflate(R.layout.include_snr, parent, false)

                holder = ViewHolder()
                holder.mImgSatFlag = convertView!!.findViewById(R.id.imageSatelliteFlag)
                holder.mTextSatPrn = convertView.findViewById(R.id.textSatellitePrn)
                holder.mTextSatSnr = convertView.findViewById(R.id.textSatelliteSnr)
                holder.mTextSatElev = convertView.findViewById(R.id.textSatelliteElev)
                holder.mTextSatAzi = convertView.findViewById(R.id.textSatelliteAzi)
                holder.mBarSatSnr = convertView.findViewById(R.id.barSatelliteSnr)

                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            val satellite = mAdapterSatList[position]

            when (satellite.constellation) {
                GnssStatus.CONSTELLATION_GPS -> {
                    if (satellite.frequency == GnssSatellite.GPS_L5_FREQUENCY)
                    {
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.gps_df)
                    }else{
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.gps)
                    }
                }
                GnssStatus.CONSTELLATION_SBAS -> {
                    holder.mImgSatFlag!!.setBackgroundResource(R.drawable.gps)
                }
                GnssStatus.CONSTELLATION_GLONASS -> {
                    holder.mImgSatFlag!!.setBackgroundResource(R.drawable.glonass)
                }
                GnssStatus.CONSTELLATION_QZSS -> {
                    if (satellite.frequency == GnssSatellite.QZSS_L5_FREQUENCY)
                    {
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.qzss_df)
                    }else {
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.qzss)
                    }
                }
                GnssStatus.CONSTELLATION_BEIDOU -> {
                    holder.mImgSatFlag!!.setBackgroundResource(R.drawable.beidou)
                }
                GnssStatus.CONSTELLATION_GALILEO -> {
                    if (satellite.frequency == GnssSatellite.GAL_L5_FREQUENCY)
                    {
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.galileo_df)
                    }else {
                        holder.mImgSatFlag!!.setBackgroundResource(R.drawable.galileo)
                    }
                }
                7 ->{
                    holder.mImgSatFlag!!.setBackgroundResource(R.drawable.irnss_df)
                }
                else -> {
                    holder.mImgSatFlag!!.setBackgroundResource(R.drawable.un)
                }
            }

            holder.mTextSatPrn!!.text = satellite.svid.toString()
            holder.mTextSatSnr!!.text = satellite.cn0.toInt().toString()
            holder.mTextSatElev!!.text =satellite.elevations.toInt().toString()
            holder.mTextSatAzi!!.text = satellite.azimuths.toInt().toString()


            val paramsExample = LinearLayout.LayoutParams(satellite.cn0.toInt()*3, LinearLayout.LayoutParams.MATCH_PARENT)

            holder.mBarSatSnr!!.layoutParams = paramsExample
            holder.mBarSatSnr!!.setBackgroundColor(getColor(satellite.cn0.toInt(), satellite.inUse))


            return convertView
        }

        private fun getColor(snr: Int, used: Boolean): Int {
            if (used){
                return when {
                    snr < 10 -> Color.RED
                    snr < 20 -> -0xb86e0
                    snr < 30 -> -0x2c00
                    snr < 40 -> -0x4d2dcb
                    else -> -0xff0100}
            }
            return Color.GRAY
        }
    }

    companion object {
        fun newInstance() = SnrFragment()
    }
}

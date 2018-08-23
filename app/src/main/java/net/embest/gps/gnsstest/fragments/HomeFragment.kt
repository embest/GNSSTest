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

import android.content.Context
import net.embest.gps.gnsstest.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager

import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.ArrayAdapter
import net.embest.gps.gnsstest.utils.FileHelper
import android.app.Activity
import android.content.SharedPreferences
import android.preference.PreferenceManager
import net.embest.gps.gnsstest.utils.XmlHelper


class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var mListener: OnFragmentInteractionListener? = null
    private val mFile = FileHelper()
    private var mPosition: Int = 0
    private var mJobFileName = ""
    private var mIsEditable = false
    private var mPreferences: SharedPreferences? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()

        buttonSave.text = getString(R.string.home_button_text_edit)
        mIsEditable = false

        if(mPreferences!!.getBoolean("preference_gnss_test_profession_mode", false)){
            layoutHomeProfession.visibility = View.VISIBLE
            layoutHomeNormal.visibility = View.GONE
            editTextXml.isEnabled = false
            val job = mFile.getTestJobs()
            val jobAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, job)
            jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerJob.adapter = jobAdapter
            if (mPosition >= job.size ){
                mPosition = 0
            }
        }
        else{
            layoutHomeProfession.visibility = View.GONE
            layoutHomeNormal.visibility = View.VISIBLE

            val job = resources.getStringArray(R.array.built_in_test_job)

            val jobAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, job)
            jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerJob.adapter = jobAdapter
        }

        spinnerJob.setSelection(mPosition)
        spinnerJob.onItemSelectedListener = this

        buttonSave.setOnClickListener {
            hideKeyboard(it)
            if (mIsEditable){
                mIsEditable = false
                buttonSave.text = getString(R.string.home_button_text_edit)
                isEnableEdit(false, mJobFileName)
                if(mPreferences!!.getBoolean("preference_gnss_test_profession_mode", false)) {
                    mFile.writeConfigFile(mJobFileName, editTextXml.text.toString())
                }
                else{
                    mFile.writeConfigFile(mJobFileName, onGenerateXml(onGetDelAiding()))
                }
            }
            else{
                mIsEditable = true
                buttonSave.text = getString(R.string.home_button_text_save)
                isEnableEdit(true, mJobFileName)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mPosition = position

        if(mPreferences!!.getBoolean("preference_gnss_test_profession_mode", false)){
            mJobFileName = parent!!.getItemAtPosition(position).toString()
            editTextXml.setText(mFile.readConfigFile(mJobFileName))
        }
        else{
            mJobFileName = "." + parent!!.getItemAtPosition(position).toString() + ".xml"
            initDisplay(mJobFileName)
        }
        mListener?.onFragmentInteraction(mJobFileName)
        isEnableEdit(false, mJobFileName)
    }

    private fun initDisplay(name:String) {
        val mXmlHelper = XmlHelper()
        val job = mXmlHelper.parse(name)
        editTextTestRound.setText(job.round.toString())
        editTextTrueLat.setText(job.latitude.toString())
        editTextTrueLon.setText(job.longitude.toString())
        editTextTestBetween.setText("")
        switchInjectTime.isChecked = false
        switchInjectXtra.isChecked = false
        onCheckDelAiding(0)
        for (task in job.tasks){
            when {
                task.taskName == "REQUEST_LOCATION_UPDATES" -> {
                    editTextTestTimeout.setText(task.timeout.toString())
                    switchTimeout.isChecked = !task.needBreak
                }
                task.taskName == "STANDBY" -> {
                    editTextTestBetween.setText(task.timeout.toString())
                }
                task.taskName == "SEND_EXTRA_COMMAND" -> {
                    when {
                        task.argString == "delete_aiding_data" ->{
                            onCheckDelAiding(task.argLong.toInt())
                        }
                        task.argString == "force_time_injection" ->{
                            switchInjectTime.isChecked = true
                        }
                        task.argString == "force_xtra_injection" ->{
                            switchInjectXtra.isChecked = true
                        }
                    }
                }
            }
        }
    }

    private fun onCheckDelAiding(del:Int){
        checkBoxDelEph.isChecked = false
        checkBoxDelAlm.isChecked = false
        checkBoxDelPos.isChecked = false
        checkBoxDelTime.isChecked = false
        checkBoxDelIono.isChecked = false
        checkBoxDelUtc.isChecked = false
        checkBoxDelHealth.isChecked = false
        checkBoxDelSvdir.isChecked = false
        checkBoxDelSvsteer.isChecked = false
        checkBoxDelSadata.isChecked = false
        checkBoxDelRti.isChecked = false
        checkBoxDelCell.isChecked = false
        if ((del and GPS_DELETE_EPHEMERIS) == GPS_DELETE_EPHEMERIS){
            checkBoxDelEph.isChecked = true
        }
        if ((del and GPS_DELETE_ALMANAC) == GPS_DELETE_ALMANAC){
            checkBoxDelAlm.isChecked = true
        }
        if ((del and GPS_DELETE_POSITION) == GPS_DELETE_POSITION){
            checkBoxDelPos.isChecked = true
        }
        if ((del and GPS_DELETE_TIME) == GPS_DELETE_TIME){
            checkBoxDelTime.isChecked = true
        }
        if ((del and GPS_DELETE_IONO) == GPS_DELETE_IONO){
            checkBoxDelIono.isChecked = true
        }
        if ((del and GPS_DELETE_UTC) == GPS_DELETE_UTC){
            checkBoxDelUtc.isChecked = true
        }
        if ((del and GPS_DELETE_HEALTH) == GPS_DELETE_HEALTH){
            checkBoxDelHealth.isChecked = true
        }
        if ((del and GPS_DELETE_SVDIR) == GPS_DELETE_SVDIR){
            checkBoxDelSvdir.isChecked = true
        }
        if ((del and GPS_DELETE_SVSTEER) == GPS_DELETE_SVSTEER){
            checkBoxDelSvsteer.isChecked = true
        }
        if ((del and GPS_DELETE_SADATA) == GPS_DELETE_SADATA){
            checkBoxDelSadata.isChecked = true
        }
        if ((del and GPS_DELETE_RTI) == GPS_DELETE_RTI){
            checkBoxDelRti.isChecked = true
        }
        if ((del and GPS_DELETE_CELLDB_INFO) == GPS_DELETE_CELLDB_INFO){
            checkBoxDelCell.isChecked = true
        }
    }

    private fun isEnableEdit (enable:Boolean, mode: String){
        if (!enable){
            checkBoxDelEph.isEnabled = false
            checkBoxDelAlm.isEnabled = false
            checkBoxDelPos.isEnabled = false
            checkBoxDelTime.isEnabled = false
            checkBoxDelIono.isEnabled = false
            checkBoxDelUtc.isEnabled = false
            checkBoxDelHealth.isEnabled = false
            checkBoxDelSvdir.isEnabled = false
            checkBoxDelSvsteer.isEnabled = false
            checkBoxDelSadata.isEnabled = false
            checkBoxDelRti.isEnabled = false
            checkBoxDelCell.isEnabled = false
            editTextTestRound.isEnabled = false
            editTextTrueLat.isEnabled = false
            editTextTrueLon.isEnabled = false
            editTextTestTimeout.isEnabled = false
            switchTimeout.isEnabled = false
            editTextTestBetween.isEnabled = false
            switchInjectTime.isEnabled = false
            switchInjectXtra.isEnabled = false
            editTextXml.isEnabled = false
        }else{
            editTextXml.isEnabled = true
            editTextTestRound.isEnabled = true
            editTextTrueLat.isEnabled = true
            editTextTrueLon.isEnabled = true
            editTextTestTimeout.isEnabled = true
            switchTimeout.isEnabled = true
            editTextTestBetween.isEnabled = true
            //TODO not ready
//            switchInjectTime.isEnabled = true
//            switchInjectXtra.isEnabled = true
            editTextXml.isEnabled = true
            if (mode == ".Customized.xml"){
                checkBoxDelEph.isEnabled = true
                checkBoxDelAlm.isEnabled = true
                checkBoxDelPos.isEnabled = true
                checkBoxDelTime.isEnabled = true
                checkBoxDelIono.isEnabled = true
                checkBoxDelUtc.isEnabled = true
                checkBoxDelHealth.isEnabled = true
                checkBoxDelSvdir.isEnabled = true
                checkBoxDelSvsteer.isEnabled = true
                checkBoxDelSadata.isEnabled = true
                checkBoxDelRti.isEnabled = true
                checkBoxDelCell.isEnabled = true
            }
        }
    }

    private fun onGetDelAiding():Int{
        var del = 0
        if (checkBoxDelEph.isChecked){
             del +=  GPS_DELETE_EPHEMERIS
        }
        if (checkBoxDelAlm.isChecked){
            del += GPS_DELETE_ALMANAC
        }
        if (checkBoxDelPos.isChecked){
            del += GPS_DELETE_POSITION
        }
        if (checkBoxDelTime.isChecked){
            del += GPS_DELETE_TIME
        }
        if (checkBoxDelIono.isChecked){
            del += GPS_DELETE_IONO
        }
        if (checkBoxDelUtc.isChecked){
            del += GPS_DELETE_UTC
        }
        if (checkBoxDelHealth.isChecked){
            del += GPS_DELETE_HEALTH
        }
        if (checkBoxDelSvdir.isChecked){
            del += GPS_DELETE_SVDIR
        }
        if (checkBoxDelSvsteer.isChecked){
            del += GPS_DELETE_SVSTEER
        }
        if (checkBoxDelSadata.isChecked){
            del += GPS_DELETE_SADATA
        }
        if (checkBoxDelRti.isChecked){
            del += GPS_DELETE_RTI
        }
        if (checkBoxDelCell.isChecked){
            del += GPS_DELETE_CELLDB_INFO
        }
        if (del == 0x87ff)
            del = 0xffff

        return del
    }

    private fun onGenerateXml(del: Int) :String{
        var xml = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<test>\n" +
            "<round>" + editTextTestRound.text + "</round>\n" +
            "<cep>TTFF</cep>\n" +
            "<longitude>" + editTextTrueLon.text + "</longitude>\n" +
            "<latitude>" + editTextTrueLat.text +"</latitude>\n"
        if (del > 0){
            xml += "<task>\n" +
            "<name>SEND_EXTRA_COMMAND</name>\n" +
            "<long>" + del.toString() + "</long>\n" +
            "<string>delete_aiding_data</string>\n" +
            "</task>\n"
        }
        xml +=  "<task>\n" +
        "<name>REQUEST_LOCATION_UPDATES</name>\n" +
        "<long>1000</long>\n" +
        "<float>0.0</float>\n" +
        "<timeout>" + editTextTestTimeout.text + "</timeout>\n"

        xml += if (switchTimeout.isChecked){
            "<break>false</break>\n"
        }
        else{
            "<break>true</break>\n"
        }
        xml +=  "</task>\n" +
        "<task>\n" +
        "<name>STOP_REQUEST</name>\n" +
        "</task>\n"

        if (editTextTestBetween.text.any()){
            xml +=  "<task>\n" +
            "<name>STANDBY</name>\n" +
            "<timeout>" + editTextTestBetween.text + "</timeout>\n" +
            "</task>\n"
        }
        xml += "</test>\n"
        return xml
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(job: String)
    }

    companion object {
        fun newInstance() = HomeFragment()
        private const val GPS_DELETE_EPHEMERIS = 0x0001
        private const val GPS_DELETE_ALMANAC = 0x0002
        private const val GPS_DELETE_POSITION = 0x0004
        private const val GPS_DELETE_TIME = 0x0008
        private const val GPS_DELETE_IONO = 0x0010
        private const val GPS_DELETE_UTC = 0x0020
        private const val GPS_DELETE_HEALTH = 0x0040
        private const val GPS_DELETE_SVDIR = 0x0080
        private const val GPS_DELETE_SVSTEER = 0x0100
        private const val GPS_DELETE_SADATA = 0x0200
        private const val GPS_DELETE_RTI = 0x0400
        private const val GPS_DELETE_CELLDB_INFO = 0x8000
    }
}

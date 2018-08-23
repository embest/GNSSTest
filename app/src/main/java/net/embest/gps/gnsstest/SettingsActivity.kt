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

@file:Suppress("DEPRECATION")

package net.embest.gps.gnsstest

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.RingtonePreference
import android.text.TextUtils
import android.view.MenuItem

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean {
        return isXLargeTablet(this)
    }

    /**
     * {@inheritDoc}
     */
    override fun onBuildHeaders(target: List<PreferenceActivity.Header>) {
        loadHeadersFromResource(R.xml.pref_headers, target)
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean {
        return PreferenceFragment::class.java.name == fragmentName
                || GeneralPreferenceFragment::class.java.name == fragmentName
//                || TrackingPreferenceFragment::class.java.name == fragmentName
//                || HotStartPreferenceFragment::class.java.name == fragmentName
//                || WarmStartPreferenceFragment::class.java.name == fragmentName
//                || ColdStartPreferenceFragment::class.java.name == fragmentName
//                || CustomizationPreferenceFragment::class.java.name == fragmentName
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("preference_notifications_ringtone"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

//    /**
//     * This fragment shows notification preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class TrackingPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_test_tracking)
//            setHasOptionsMenu(true)
//
//            bindPreferenceSummaryToValue(findPreference("preference_track_test_name"))
//            bindPreferenceSummaryToValue(findPreference("preference_track_test_round"))
//            bindPreferenceSummaryToValue(findPreference("preference_track_test_timeout"))
//            bindPreferenceSummaryToValue(findPreference("preference_track_test_between"))
//            bindPreferenceSummaryToValue(findPreference("preference_track_true_location_lat"))
//            bindPreferenceSummaryToValue(findPreference("preference_track_true_location_lon"))
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    /**
//     * This fragment shows data and sync preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class HotStartPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_test_hot_start)
//            setHasOptionsMenu(true)
//
//            bindPreferenceSummaryToValue(findPreference("preference_hot_test_name"))
//            bindPreferenceSummaryToValue(findPreference("preference_hot_test_round"))
//            bindPreferenceSummaryToValue(findPreference("preference_hot_test_timeout"))
//            bindPreferenceSummaryToValue(findPreference("preference_hot_test_between"))
//            bindPreferenceSummaryToValue(findPreference("preference_hot_true_location_lat"))
//            bindPreferenceSummaryToValue(findPreference("preference_hot_true_location_lon"))
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    /**
//     * This fragment shows notification preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class WarmStartPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_test_warm_start)
//            setHasOptionsMenu(true)
//
//            bindPreferenceSummaryToValue(findPreference("preference_warm_test_name"))
//            bindPreferenceSummaryToValue(findPreference("preference_warm_test_round"))
//            bindPreferenceSummaryToValue(findPreference("preference_warm_test_timeout"))
//            bindPreferenceSummaryToValue(findPreference("preference_warm_test_between"))
//            bindPreferenceSummaryToValue(findPreference("preference_warm_true_location_lat"))
//            bindPreferenceSummaryToValue(findPreference("preference_warm_true_location_lon"))
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//
//    /**
//     * This fragment shows data and sync preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class ColdStartPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_test_cold_start)
//            setHasOptionsMenu(true)
//
//            bindPreferenceSummaryToValue(findPreference("preference_cold_test_name"))
//            bindPreferenceSummaryToValue(findPreference("preference_cold_test_round"))
//            bindPreferenceSummaryToValue(findPreference("preference_cold_test_timeout"))
//            bindPreferenceSummaryToValue(findPreference("preference_cold_test_between"))
//            bindPreferenceSummaryToValue(findPreference("preference_cold_true_location_lat"))
//            bindPreferenceSummaryToValue(findPreference("preference_cold_true_location_lon"))
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }
//    /**
//     * This fragment shows notification preferences only. It is used when the
//     * activity is showing a two-pane settings UI.
//     */
//    class CustomizationPreferenceFragment : PreferenceFragment() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//            super.onCreate(savedInstanceState)
//            addPreferencesFromResource(R.xml.pref_test_custom)
//            setHasOptionsMenu(true)
//
//            bindPreferenceSummaryToValue(findPreference("preference_custom_test_name"))
//            bindPreferenceSummaryToValue(findPreference("preference_custom_test_round"))
//            bindPreferenceSummaryToValue(findPreference("preference_custom_test_timeout"))
//            bindPreferenceSummaryToValue(findPreference("preference_custom_test_between"))
//            bindPreferenceSummaryToValue(findPreference("preference_custom_true_location_lat"))
//            bindPreferenceSummaryToValue(findPreference("preference_custom_true_location_lon"))
//        }
//
//        override fun onOptionsItemSelected(item: MenuItem): Boolean {
//            val id = item.itemId
//            if (id == android.R.id.home) {
//                startActivity(Intent(activity, SettingsActivity::class.java))
//                return true
//            }
//            return super.onOptionsItemSelected(item)
//        }
//    }

    companion object {

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()

            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                val index = preference.findIndexOfValue(stringValue)

                // Set the summary to reflect the new value.
                preference.setSummary(
                        if (index >= 0)
                            preference.entries[index]
                        else
                            null)

            } else if (preference is RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent)

                } else {
                    val ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue))

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null)
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        val name = ringtone.getTitle(preference.getContext())
                        preference.setSummary(name)
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.summary = stringValue
            }
            true
        }

        /**
         * Helper method to determine if the device has an extra-large screen. For
         * example, 10" tablets are extra-large.
         */
        private fun isXLargeTablet(context: Context): Boolean {
            return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE
        }

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.

         * @see .sBindPreferenceSummaryToValueListener
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.context)
                            .getString(preference.key, ""))
        }
    }
}

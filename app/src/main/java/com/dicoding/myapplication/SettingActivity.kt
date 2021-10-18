package com.dicoding.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.myapplication.NotificationWorker.Companion.NOTIFICATION_CHANNEL_ID
import com.dicoding.myapplication.databinding.ActivitySettingActvityBinding
import java.util.concurrent.TimeUnit

class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting, SettingFragment()).commit()

    }

    class SettingFragment : PreferenceFragmentCompat() {

        private lateinit var isNotification: SwitchPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            init()

        }

        private fun init() {
            isNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_notif_key_daily)) as SwitchPreference
            isNotification.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.pref_notif_key_daily)
                val workManager = context?.let { WorkManager.getInstance(it) }
                if (newValue as Boolean) {
                    val data = Data.Builder().apply {
                        putString(NOTIFICATION_CHANNEL_ID, channelName)
                    }.build()
                    val periodicNotifyRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                        15,
                        TimeUnit.MINUTES,
                        5,
                        TimeUnit.MINUTES
                    )
                        .setInputData(data)
                        .build()
                    workManager?.enqueue(periodicNotifyRequest)
                } else {
                    workManager?.cancelAllWork()
                }
                true
            }

            findPreference<ListPreference>(getString(R.string.pref_dark_key_dark_mode))?.setOnPreferenceChangeListener { _, newValue ->
                val valueDarkMode = newValue as String
                Log.d("DARKVALUE", valueDarkMode)
                valueDarkMode?.toUpperCase().let { DarkMode.valueOf(it).value }.let { updateTheme(it) }

                true
            }

        }

        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }

    }

}
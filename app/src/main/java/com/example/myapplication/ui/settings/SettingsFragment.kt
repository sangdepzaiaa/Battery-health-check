package com.example.myapplication.ui.settings

import java.util.concurrent.TimeUnit
import android.icu.util.Calendar
import android.widget.Toast
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.ui.dialog.DaysIntervalDialog
import com.example.myapplication.ui.dialog.TimePickerDialog
import com.example.myapplication.utils.BatteryCheckWorker
import com.example.myapplication.utils.tap

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    inflater = FragmentSettingsBinding::inflate
) {
    private var lastToastTime = 0L
    private var currentToast: Toast? = null

    override fun initView() {
        super.initView()
        setClick()
    }

    private fun setClick() {

        binding.run {

            layoutToolBar.txvTitle.text = getString(R.string.settings)

            layoutProgrammerSettings.run {

                // mặc định switch OFF
                customSwitch.setChecked(false)
                updateProgrammerUI(false)

                btnSetTime.tap {
                    TimePickerDialog(requireContext()) { hour, minute ->
                        val time = String.format("%02d:%02d", hour, minute)
                        txvSetTime.text = time
                        btnSetTime.text = time

                        scheduleBatteryCheck()

                        showToast(buildScheduleMessage())
                    }.show()
                }

                btnSetInterval.tap {
                    DaysIntervalDialog(requireContext()) { days ->
                        txvSetInterval.text = "$days ${context?.getString(R.string.days)}"
                        btnSetInterval.text = "$days ${context?.getString(R.string.days)}"

                        scheduleBatteryCheck()

                        showToast(buildScheduleMessage())
                    }.show()
                }
                // Sử dụng Listener của IOSSwitch thay vì extension .tap
                customSwitch.setOnCheckedChangeListener { isChecked ->
                    updateProgrammerUI(isChecked)
                }
            }
        }
    }

    private fun updateProgrammerUI(enable: Boolean) {
        with(binding.layoutProgrammerSettings) {
            // Cập nhật trạng thái enabled
            txvSetTime.isEnabled = enable
            txvSetInterval.isEnabled = enable
            btnSetTime.isEnabled = enable
            btnSetInterval.isEnabled = enable

            // Cập nhật Icon
            imgInfo.setImageResource(
                if (enable) R.drawable.ic_info_enable else R.drawable.ic_info
            )

            // Cập nhật Alpha
            val alphaValue = if (enable) 1f else 0.4f
            txvSetTime.alpha = alphaValue
            txvSetInterval.alpha = alphaValue
            btnSetTime.alpha = alphaValue
            btnSetInterval.alpha = alphaValue
        }
    }

    private fun getIntervalDays(): Int? {

        val text = binding.layoutProgrammerSettings.txvSetInterval.text.toString()

        return if (text.contains("${context?.getString(R.string.days)}")) {
            text.replace("${context?.getString(R.string.days)}", "").trim().toInt()
        } else {
            null
        }
    }

    private fun getTime(): Pair<Int, Int>? {

        val text = binding.layoutProgrammerSettings.txvSetTime.text.toString()

        return if (text.contains(":")) {

            val parts = text.split(":")
            Pair(parts[0].toInt(), parts[1].toInt())

        } else {
            null
        }
    }

    private fun scheduleBatteryCheck() {

        val interval = getIntervalDays()
        val time = getTime()

        val calendar = Calendar.getInstance()

        if (interval != null) {
            calendar.add(Calendar.DAY_OF_YEAR, interval)
        }

        if (time != null) {
            calendar.set(Calendar.HOUR_OF_DAY, time.first)
            calendar.set(Calendar.MINUTE, time.second)
            calendar.set(Calendar.SECOND, 0)
        }

        // FIX nếu time đã qua
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            if (interval == null) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()

        val workRequest =
            OneTimeWorkRequestBuilder<BatteryCheckWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(
                "battery_check",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    private fun showToast(message: String) {

        val now = System.currentTimeMillis()

        // chặn spam trong 1s
        if (now - lastToastTime < 2000) return

        lastToastTime = now

        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private fun buildScheduleMessage(): String {

        val interval = getIntervalDays()
        val time = getTime()

        return when {
            interval != null && time != null -> {
                "${interval} ${getString(R.string.days_at)} ${String.format("%02d:%02d", time.first, time.second)}"
            }

            interval != null -> {
                "${getString(R.string.after)} $interval ${getString(R.string.days)}"
            }

            time != null -> {
                "${getString(R.string.at)} ${String.format("%02d:%02d", time.first, time.second)}"
            }

            else -> {
                getString(R.string.no_schedule_set)
            }
        }
    }

}

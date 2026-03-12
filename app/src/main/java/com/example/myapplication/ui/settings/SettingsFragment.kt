package com.example.myapplication.ui.settings


import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.databinding.FragmentSettingsBinding
import com.example.myapplication.ui.settings.dialog.DaysIntervalDialog
import com.example.myapplication.ui.settings.dialog.TimePickerDialog
import com.example.myapplication.utils.tap

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    inflater = FragmentSettingsBinding::inflate
) {

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
                    }.show()
                }

                btnSetInterval.tap {
                    DaysIntervalDialog(requireContext()) { days ->
                        txvSetInterval.text = "$days days"
                        btnSetInterval.text = "$days days"
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

}

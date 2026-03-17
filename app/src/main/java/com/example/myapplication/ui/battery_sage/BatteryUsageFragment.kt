package com.example.myapplication.ui.battery_sage

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.AppUsageModel
import com.example.myapplication.databinding.FragmentBatteryUsageBinding
import com.example.myapplication.ui.battery_sage.adapter.AppUsageAdapter
import com.example.myapplication.utils.BatteryUsageManager
import com.example.myapplication.utils.tap

class BatteryUsageFragment : BaseFragment<FragmentBatteryUsageBinding>(
    inflater = FragmentBatteryUsageBinding::inflate
) {

    private lateinit var adapter: AppUsageAdapter

    private var allApps: List<AppUsageModel> = emptyList()
    private var systemApps: List<AppUsageModel> = emptyList()
    private var userApps: List<AppUsageModel> = emptyList()

    override fun initView() {
        super.initView()

        binding.layoutToolBar.imgInfo.setOnClickListener {
            startActivity(Intent(requireContext(), BatteryTipsActivity::class.java))
        }

        binding.layoutToolBar.txvTitle.text = getString(R.string.battery_usage)

        binding.layoutToolBar.imgInfo.tap {
            startActivity(Intent(requireContext(), BatteryTipsActivity::class.java))
        }



        setupRecyclerView()
        setupTabLayout()
        loadData()

        if (!hasUsagePermission(requireContext())) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            return
        }


    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized && hasUsagePermission(requireContext())) {
            loadData()
        }
    }

    private fun setupRecyclerView() {

        adapter = AppUsageAdapter()

        binding.rcvApps.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvApps.adapter = adapter
    }

    private fun setupTabLayout() {

        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText(context?.getString(R.string.system_apps))
        )

        binding.layoutTabLayout.tabLayout.addTab(
            binding.layoutTabLayout.tabLayout.newTab().setText(context?.getString(R.string.user_apps))
        )

        binding.layoutTabLayout.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when (tab?.position) {

                    0 -> adapter.submitList(systemApps)

                    1 -> adapter.submitList(userApps)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadData() {

        lifecycleScope.launch(Dispatchers.IO) {

            val data = BatteryUsageManager.loadAppUsage(requireContext())

            withContext(Dispatchers.Main) {

                allApps = data

                systemApps = data.filter { it.isSystemApp }
                userApps = data.filter { !it.isSystemApp }

                adapter.submitList(systemApps)
            }
        }
    }

    private fun hasUsagePermission(context: Context): Boolean {

        val appOps =
            context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )

        return mode == AppOpsManager.MODE_ALLOWED
    }
}


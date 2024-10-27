package org.zipper.ant.forest.xposed.utils

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


object PermissionCompat {
    private const val MANAGE_EXTERNAL_STORAGE_PERMISSION = "android:manage_external_storage"

    fun getPermissionStatus(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkStoragePermissionApi30(context)
        } else {
            checkStoragePermission(context)
        }
    }

    fun requestStoragePermission(activity: FragmentActivity, onGrantResult: (Boolean) -> Unit) {
        RequestPermissionFragment.requestPermission(activity, onGrantResult)
    }


    private fun getStoragePermissionName(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    @RequiresApi(30)
    private fun checkStoragePermissionApi30(context: Context): Boolean {
        val appOps = context.getSystemService(AppOpsManager::class.java)
        val mode = appOps.unsafeCheckOpNoThrow(
            MANAGE_EXTERNAL_STORAGE_PERMISSION,
            context.applicationInfo.uid,
            context.packageName
        )

        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun checkStoragePermission(context: Context): Boolean {
        val status = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return status == PackageManager.PERMISSION_GRANTED
    }

    class RequestPermissionFragment : Fragment() {

        private var requestCallback: (Boolean) -> Unit = {}

        companion object {
            fun requestPermission(activity: FragmentActivity, callback: (Boolean) -> Unit) {
                if (getPermissionStatus(activity)) {
                    callback.invoke(true)
                    return
                }
                activity.supportFragmentManager.beginTransaction()
                    .add(RequestPermissionFragment().apply {
                        requestCallback = callback
                    }, "RequestPermissionFragment")
                    .commit()
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val innerCallback: () -> Unit = {
                try {
                    requestCallback.invoke(getPermissionStatus(requireContext()))
                } finally {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissionApi30(innerCallback)
            } else {
                requestPermission(innerCallback)
            }

        }

        @RequiresApi(30)
        private fun requestPermissionApi30(callback: () -> Unit) {
            val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                callback()
            }
            launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
        }

        private fun requestPermission(callback: () -> Unit) {
            val launcher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                callback()
            }
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}
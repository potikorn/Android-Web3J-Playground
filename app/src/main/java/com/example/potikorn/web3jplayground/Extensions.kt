package com.example.potikorn.web3jplayground

import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Activity.checkPermission(
    permissions: List<String>,
    onPermissionAllowed: () -> Unit,
    onPermissionDenied: () -> Unit
) = Dexter.withActivity(this)
    .withPermissions(permissions)
    .withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            when (!hasDeniedPermission(report)) {
                true -> onPermissionAllowed.invoke()
                else -> onPermissionDenied.invoke()
            }
        }

        override fun onPermissionRationaleShouldBeShown(
            permissions: List<PermissionRequest>,
            token: PermissionToken
        ) = token.continuePermissionRequest()

        private fun hasDeniedPermission(report: MultiplePermissionsReport): Boolean {
            val denyPermission = report.deniedPermissionResponses
            return denyPermission != null && denyPermission.size > 0
        }
    })
    .check()
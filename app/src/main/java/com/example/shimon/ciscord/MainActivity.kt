package com.example.shimon.ciscord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()

    }

    private fun requestPermission() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (p0 != null) {
                    if (p0.areAllPermissionsGranted()) {
                        Toast.makeText(
                            this@MainActivity,
                            "All Permission Granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if (p0 != null) {
                    if (p0.isAnyPermissionPermanentlyDenied){

                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).withErrorListener {error: DexterError ->
            Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()

        }.onSameThread().check()

    }


}
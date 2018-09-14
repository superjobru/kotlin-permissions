package com.superjob.kotlinpermissionsample

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kotlinpermissions.KotlinPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationButton.setOnClickListener { _ ->
            KotlinPermissions.with(this)
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .onAccepted {
                        setLocationStatus("Granted")
                    }
                    .onDenied {
                        setLocationStatus("Denied")
                    }
                    .onForeverDenied {
                        setLocationStatus("Forever denied")
                    }
                    .ask()
        }

        cameraButton.setOnClickListener { _ ->

            KotlinPermissions.with(this)
                    .permissions(Manifest.permission.CAMERA)
                    .onAccepted {
                        setCameraStatus("Granted")
                    }
                    .onDenied {
                        setCameraStatus("Denied")
                    }
                    .onForeverDenied {
                        setCameraStatus("Forever denied")
                    }
                    .ask()

        }
    }

    private fun setCameraStatus(status: String) {
        cameraStatusTextView.text = status
    }

    private fun setLocationStatus(status: String) {
        locationStatusTextView.text = status
    }
}
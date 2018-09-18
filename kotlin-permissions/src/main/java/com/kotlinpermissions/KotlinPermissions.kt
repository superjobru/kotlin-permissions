package com.kotlinpermissions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.Semaphore

object KotlinPermissions {
    private const val TAG = "KotlinPermission"
    private val semaphore: Semaphore = Semaphore(1)

    @JvmStatic
    fun with(activity: FragmentActivity): PermissionCore {
        return PermissionCore(activity)
    }

    class PermissionCore(activity: FragmentActivity) {
        private val activityReference: WeakReference<FragmentActivity> = WeakReference(activity)
        private var permissions: List<String> = ArrayList()
        private var acceptedCallback: WeakReference<ResponsePermissionCallback>? = null
        private var deniedCallback: WeakReference<ResponsePermissionCallback>? = null
        private var foreverDeniedCallback: WeakReference<ResponsePermissionCallback>? = null
        private val listener = object : PermissionFragment.PermissionListener {
            override fun onRequestPermissionsResult(acceptedPermissions: List<String>, refusedPermissions: List<String>, askAgainPermissions: List<String>) {
                onReceivedPermissionResult(acceptedPermissions, refusedPermissions, askAgainPermissions)
            }
        }

        internal fun onReceivedPermissionResult(acceptedPermissions: List<String>?, foreverDenied: List<String>?, denied: List<String>?) {

            acceptedPermissions.whenNotNullNorEmpty {
                acceptedCallback?.get()?.onResult(it)
            }

            foreverDenied.whenNotNullNorEmpty {
                foreverDeniedCallback?.get()?.onResult(it)
            }

            denied.whenNotNullNorEmpty {
                deniedCallback?.get()?.onResult(it)
            }
        }

        fun permissions(vararg permission: String): PermissionCore {
            permissions = permission.toList()
            return this@PermissionCore
        }

        fun onAccepted(callback: (List<String>) -> Unit): PermissionCore {
            this.acceptedCallback = WeakReference(object : ResponsePermissionCallback {
                override fun onResult(permissionResult: List<String>) {
                    callback(permissionResult)
                }
            })
            return this@PermissionCore
        }

        fun onAccepted(callback: ResponsePermissionCallback): PermissionCore {
            this.acceptedCallback = WeakReference(callback)
            return this@PermissionCore
        }

        fun onDenied(callback: (List<String>) -> Unit): PermissionCore {
            this.deniedCallback = WeakReference(object : ResponsePermissionCallback {
                override fun onResult(permissionResult: List<String>) {
                    callback(permissionResult)
                }
            })
            return this@PermissionCore
        }

        fun onDenied(callback: ResponsePermissionCallback): PermissionCore {
            this.deniedCallback = WeakReference(callback)
            return this@PermissionCore
        }

        fun onForeverDenied(callback: (List<String>) -> Unit): PermissionCore {
            this.foreverDeniedCallback = WeakReference(object : ResponsePermissionCallback {
                override fun onResult(permissionResult: List<String>) {
                    callback(permissionResult)
                }
            })
            return this@PermissionCore
        }

        fun onForeverDenied(callback: ResponsePermissionCallback): PermissionCore {
            this.foreverDeniedCallback = WeakReference(callback)
            return this@PermissionCore
        }

        fun ask() {
            semaphore.acquire()
            val activity = activityReference.get()
            activity?.let { fragmentActivity ->
                if (fragmentActivity.isFinishing) {
                    semaphore.release()
                    return
                }

                //ne need < Android Marshmallow
                if (permissions.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                        permissionAlreadyAccepted(activity, permissions)) {
                    onAcceptedPermission(permissions)
                    semaphore.release()
                } else {
                    val oldFragment = fragmentActivity.supportFragmentManager.findFragmentByTag(TAG) as PermissionFragment?

                    oldFragment.ifNotNullOrElse({
                        it.addPermissionForRequest(listener, permissions)
                        semaphore.release()
                    }, {
                        val newFragment = PermissionFragment.newInstance()
                        newFragment.addPermissionForRequest(listener, permissions)
                        Try.withThreadIfFail({
                            fragmentActivity.runOnUiThread {
                                fragmentActivity.supportFragmentManager.beginTransaction().add(newFragment, TAG).commitNowAllowingStateLoss()
                                semaphore.release()
                            }
                        }, 3)

                    })
                }

            }
        }

        private fun onAcceptedPermission(permissions: List<String>) {
            onReceivedPermissionResult(permissions, null, null)
        }

        private fun permissionAlreadyAccepted(context: Context, permissions: List<String>): Boolean {
            for (permission in permissions) {
                val permissionState = ContextCompat.checkSelfPermission(context, permission)
                if (permissionState == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
            return true
        }
    }
}
package com.kotlinpermissions

interface ResponsePermissionCallback {
     fun onResult(permissionResult: List<String>)
}
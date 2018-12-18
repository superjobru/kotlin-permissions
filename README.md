# Kotlin Permissions

[![Build Status](https://travis-ci.org/superjobru/kotlin-permissions.svg?branch=master)](https://travis-ci.org/superjobru/kotlin-permissions) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-KotlinPermissions-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7238)

An Android library, written in Kotlin, which helps avoid boilerplate code when your request runtime permissions. You don't need to override onPermissionResult, all results will be in one place.

## Gradle
[ ![Download](https://api.bintray.com/packages/superjob/android/kotlin-permissions/images/download.svg) ](https://bintray.com/superjob/android/kotlin-permissions/_latestVersion)
```gradle
dependencies {
    implementation 'ru.superjob:kotlin-permissions:1.0.3'
}
```

## Usage

Call a `KotlinPermissions` instance :

```java
KotlinPermissions.with(this) // where this is an FragmentActivity instance
                    .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                    .onAccepted { permissions ->
                   		//List of accepted permissions
                    }
                    .onDenied { permissions ->
                   		//List of denied permissions
                    }
                    .onForeverDenied { permissions ->
                   		//List of forever denied permissions
                    }
                    .ask()
```

You can request more than one permission: 
```java
.permissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA)
```
# License

```
Copyright (C) 2018

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

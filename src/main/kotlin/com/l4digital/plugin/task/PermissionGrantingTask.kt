/*
 * Copyright 2017 L4 Digital LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.l4digital.plugin.task

import com.l4digital.plugin.grantPermissionsTaskName
import org.gradle.api.tasks.TaskAction

@Suppress("MemberVisibilityCanPrivate")
open class PermissionGrantingTask : AbstractAdbTask() {

    // Properties to be configured in build.gradle
    var targetPackageName: String? = null
    var permissions: List<String> = emptyList()

    init {
        description = "Grants permissions for UI testing purposes on devices running Marshmallow " +
                "or greater"
        group = "Pre UI Test"

        onlyIf {
            isRunningMarshmallowOrGreater() && isTargetPackageNameSet() && hasPermissions()
        }
    }

    private fun isRunningMarshmallowOrGreater(): Boolean {
        return getAndroidVersion() >= 23
    }

    private fun isTargetPackageNameSet(): Boolean {
        targetPackageName?.let {
            return it.isNotEmpty()
        }

        logger.warn("Calling task '$grantPermissionsTaskName' without setting the target " +
                "package name has no effect.")
        return false
    }

    private fun hasPermissions(): Boolean {
        if (permissions.isEmpty()) {
            logger.warn("Calling task '$grantPermissionsTaskName' without providing " +
                    "permissions to grant has no effect.")
            return false
        }

        return true
    }

    @TaskAction
    fun grant() {
        targetPackageName?.let { packageName ->
            permissions.forEach { permission ->
                adb.exec("shell", "pm", "grant", packageName, permission)
            }
        }
    }
}
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

import com.l4digital.plugin.ConfigurationExtension
import com.l4digital.plugin.helper.AdbHelper
import org.gradle.api.DefaultTask

abstract class AbstractAdbTask : DefaultTask() {
    private val config: ConfigurationExtension = project.extensions
            .findByType(ConfigurationExtension::class.java)
    protected val adb = AdbHelper(project, config)

    protected fun delete(remotePath: String) {
        adb.exec("shell", "rm", "-r", remotePath)
        val message = "Finished deleting $remotePath from device"
        project.logger.info(message)
    }

    protected fun pull(remotePath: String, localOutputPath: String) {
        adb.exec("pull", remotePath, localOutputPath)
        val message = "Finished pulling $remotePath from device to $localOutputPath"
        project.logger.info(message)
    }

    protected fun getAndroidVersion(): Int {
        val result = adb.exec("shell", "getprop", "ro.build.version.sdk").trim()
        return result.toInt()
    }
}
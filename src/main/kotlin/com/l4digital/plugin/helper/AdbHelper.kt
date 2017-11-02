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

package com.l4digital.plugin.helper

import com.l4digital.plugin.ConfigurationExtension
import org.gradle.api.Project
import java.io.ByteArrayOutputStream

class AdbHelper(private val project: Project, private val config: ConfigurationExtension) {

    fun checkExistenceOfRemotePath(remotePath: String?): Boolean {
        remotePath?.let {
            val output = exec("shell", "[", "-e", it, "]", "&&", "echo", "true")
            val exists = output.trim().toBoolean()

            if (!exists) {
                project.logger.warn("Remote path '$remotePath' does not exists")
            }

            return exists
        }

        return false
    }

    fun exec(vararg args: Any): String {
        ensureAdbPathIsSet()
        val os = ByteArrayOutputStream()
        project.exec { execSpec ->
            execSpec.isIgnoreExitValue = true
            execSpec.executable(config.adbExecutablePath)
            execSpec.args(*args)
            execSpec.standardOutput = os
            execSpec.errorOutput = os
        }
        project.logger.debug(os.toString())
        return os.toString()
    }

    @Throws(ConfigurationExtension.ConfigurationException::class)
    private fun ensureAdbPathIsSet() {
        if (config.adbExecutablePath == null) {
            val message = "ADB executable path has not been set."
            throw ConfigurationExtension.ConfigurationException(message)
        }
    }
}
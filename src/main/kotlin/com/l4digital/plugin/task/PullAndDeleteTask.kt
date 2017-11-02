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
import com.l4digital.plugin.pullAndDeleteTaskTypeName
import org.gradle.api.tasks.TaskAction

open class PullAndDeleteTask : AbstractAdbTask() {

    var remotePath: String? = null
    var localOutputPath: String = "../"

    init {
        onlyIf {
            validateRemotePath()
        }
    }

    @Throws(ConfigurationExtension.ConfigurationException::class)
    private fun validateRemotePath(): Boolean {
        if (remotePath.isNullOrEmpty()) {
            val message = "Property 'remotePath' has not been set for task with type " +
                    pullAndDeleteTaskTypeName
            throw ConfigurationExtension.ConfigurationException(message)
        }

        return adb.checkExistenceOfRemotePath(remotePath)
    }

    @TaskAction
    fun taskAction() {

        remotePath?.let { remotePath ->
            pull(remotePath, localOutputPath)
            delete(remotePath)
        }
    }
}
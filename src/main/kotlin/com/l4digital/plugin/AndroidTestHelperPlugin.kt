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

package com.l4digital.plugin

import com.l4digital.plugin.task.PermissionGrantingTask
import com.l4digital.plugin.task.PullAndDeleteTask
import org.gradle.api.Plugin
import org.gradle.api.Project

val grantPermissionsTaskName = "grantPermissions"
val pullAndDeleteTaskTypeName = "PullAndDelete"

class AndroidTestHelperPlugin : Plugin<Project> {
    private lateinit var config: ConfigurationExtension

    override fun apply(project: Project?) {
        project?.let {
            config = it.extensions
                    .create("androidTestHelperConfig", ConfigurationExtension::class.java)
            it.tasks.create(grantPermissionsTaskName, PermissionGrantingTask::class.java)
            it.tasks.create("pullAndDelete", PullAndDeleteTask::class.java)

            // This allows us to use the task types without having to specify the fully qualified
            // name.
            it.extensions.extraProperties.set(pullAndDeleteTaskTypeName, PullAndDeleteTask::class.java)
        }
    }
}
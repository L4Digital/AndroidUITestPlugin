# Android UI Test Helper
A Gradle plugin for common ADB actions, primarily focused around tasks needed for UI automation.

## Configuration

The path to an ADB executable must be passed to the `adbExecutablePath` property within `androidTestHelperConfig` for this plugin to function properly.

_Example:_

```groovy
androidTestHelperConfig {
    adbExecutablePath = android.adbExecutable
}
```

## Pulling and deleting remote files

Creating a "pull-and-delete" task requires the `PullAndDelete` task type. In order to pull and delete a remote file you must specify its path using the `remotePath` property. Specifying the local output path (via the `localOutputPath` property) is optional and will default to the parent directory.

_Example:_

```groovy
task pullScreenshots(type: PullAndDelete) {
    // This would default to ../ if not overwritten here
    localOutputPath = './'
    
    // Setting this property is required
    remotePath = 'sdcard/test-screenshots/'
}
```

## Granting Android Permissions

For granting permissions there are a couple things required:

- Target application package name
- List of permissions to grant

_Example:_

```groovy
grantPermissions {
    targetPackageName = yourAppsPackageName
    permissions = [
        'android.permission.READ_EXTERNAL_STORAGE',
        'android.permission.WRITE_EXTERNAL_STORAGE'
    ]
}
``` 

The best way to utilize this `grantPermissions` task is to make it depend on the application variant's `install` task and make the `connectedAndroidTest` task dependent on this `grantPermissions` task. This is because the application under test must be installed before permissions can be granted and permissions must be granted before running any UI tests that require said permissions. Here's a working example of this with the `debug` build type:

```
android {
    ...
    
    applicationVariants.all { variant ->
        if (variant.getBuildType().name == "debug") {
            tasks.findByName('grantPermissions').dependsOn variant.install
            variant.testVariant.connectedInstrumentTest.dependsOn 'grantPermissions'
        }
    }
}
```

_NOTE: The `grantPermissions` task will automatically be skipped for any device that is running below Android 6.0._
  
## License

    Copyright 2017 L4 Digital LLC. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
         
         http://www.apache.org/licenses/LICENSE-2.0
         
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
package cli

import androidx.compose.ui.platform.DesktopPlatform
import java.util.concurrent.TimeUnit

class AvdManagerCli {

    // TODO: This should get passed in, maybe through some EnvProvider sorta thing?
    private val androidHome by lazy {
        System.getenv("ANDROID_HOME")
    }

    private val avdManagerBinary: String = when (DesktopPlatform.Current) {
        DesktopPlatform.Linux, DesktopPlatform.MacOS -> "$androidHome/tools/bin/avdmanager"
        DesktopPlatform.Windows -> "$androidHome/tools/bin/avdmanager.exe"
    }

    /**
     * Attempt to create an AVD based on the given values.
     *
     * @return `true` if the AVD was successfully create, `false` otherwise
     */
    fun create(name: String): Boolean {
        // TODO: Validate name -- push this up to the form dialog
        // TODO: Pass -k options as params
        // TODO: Pass -d options as params
        val command = "$avdManagerBinary --silent create avd --name $name --package 'system-images;android-28;google_apis;x86_64' --device \"Nexus 5\""

        println("gonna run: $command")

        val parts = command.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(30, TimeUnit.SECONDS)

        val output = proc.inputStream.bufferedReader().readText().trim()

        println(output)

        return false
    }
}

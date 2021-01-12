package cli

import androidx.compose.ui.platform.DesktopPlatform
import java.util.concurrent.TimeUnit

sealed class EmulatorCli {

    abstract fun list(): List<String>
    abstract fun start(name: String)

    private val androidHome by lazy {
        System.getenv("ANDROID_HOME")
    }

    protected val emulatorExe = "${androidHome}\\emulator\\emulator.exe"

    companion object {
        operator fun invoke(): EmulatorCli = when (DesktopPlatform.Current) {
            DesktopPlatform.Linux -> TODO("No Linux EmulatorCli implementation yet! :(")
            DesktopPlatform.Windows -> WindowsEmulatorCli()
            DesktopPlatform.MacOS -> TODO("No Mac EmulatorCli implementation yet! :(")
        }
    }

    private class WindowsEmulatorCli : EmulatorCli() {

        override fun list(): List<String> {
            val command = "$emulatorExe -list-avds"

            val parts = command.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(5, TimeUnit.SECONDS)

            val output = proc.inputStream.bufferedReader().readText().trim()

            return output
                .replace("\r\n", "\n")
                .split("\n")
        }

        override fun start(name: String) {
            val command = "$emulatorExe -avd $name"
            Runtime.getRuntime().exec(command)
        }
    }
}

package cli

import androidx.compose.ui.platform.DesktopPlatform
import java.util.concurrent.TimeUnit

sealed class EmulatorCli {

    protected val androidHome by lazy {
        System.getenv("ANDROID_HOME")
    }

    protected abstract val emulatorExecutable: String

    fun list(): List<String> {
        val command = "$emulatorExecutable -list-avds"

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

    fun start(name: String) {
        val command = "$emulatorExecutable -avd $name"
        Runtime.getRuntime().exec(command)
    }

    private class WindowsEmulatorCli : EmulatorCli() {
        override val emulatorExecutable = "${androidHome}\\emulator\\emulator.exe"
    }

    private class LinuxEmulatorCli : EmulatorCli() {
        override val emulatorExecutable = "${androidHome}/emulator/emulator"
    }

    private class MacEmulatorCli : EmulatorCli() {
        override val emulatorExecutable = "${androidHome}/emulator/emulator"
    }

    companion object {
        operator fun invoke(): EmulatorCli = when (DesktopPlatform.Current) {
            DesktopPlatform.Linux -> LinuxEmulatorCli()
            DesktopPlatform.Windows -> WindowsEmulatorCli()
            DesktopPlatform.MacOS -> MacEmulatorCli()
        }
    }
}

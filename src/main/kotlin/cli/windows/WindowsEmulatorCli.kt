package cli.windows

import cli.EmulatorCli
import java.util.concurrent.TimeUnit

object WindowsEmulatorCli : EmulatorCli {

    private val ANDROID_HOME by lazy {
        System.getenv("ANDROID_HOME")
    }

    private val EMULATOR_EXE = "${ANDROID_HOME}\\emulator\\emulator.exe"

    override fun list(): List<String> {
        val command = "$EMULATOR_EXE -list-avds"

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
        val command = "$EMULATOR_EXE -avd $name"
        Runtime.getRuntime().exec(command)
    }
}

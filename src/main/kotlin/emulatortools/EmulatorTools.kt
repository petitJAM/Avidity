package emulatortools

import data.Emulator
import java.util.concurrent.TimeUnit

private const val ANDROID_HOME = "C:\\Users\\alexm\\AppData\\Local\\Android\\Sdk\\"

private const val EMULATOR_EXE = "${ANDROID_HOME}emulator\\emulator.exe"

fun findEmulators(): List<Emulator> {
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
        .map { Emulator(it) }
}

fun startEmulator(name: String) {
    val command = "$EMULATOR_EXE -avd $name"
    Runtime.getRuntime().exec(command)
}

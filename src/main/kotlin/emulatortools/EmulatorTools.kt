package emulatortools

import data.Emulator
import java.util.concurrent.TimeUnit

fun findEmulators(): List<Emulator> {
    val command = "C:\\Users\\alexm\\AppData\\Local\\Android\\Sdk\\tools\\emulator.exe -list-avds"

    val parts = command.split("\\s".toRegex())
    val proc = ProcessBuilder(*parts.toTypedArray())
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

    proc.waitFor(5, TimeUnit.SECONDS)

    val output = proc.inputStream.bufferedReader().readText().trim()

    println(output)

    return output
        .replace("\r\n", "\n")
        .split("\n")
        .map { Emulator(it) }
        .onEach { println(it) }
}

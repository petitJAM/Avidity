package data

import cli.AvdManagerCli
import cli.EmulatorCli
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EmulatorsRepository(
    private val emulatorCli: EmulatorCli,
    private val avdManagerCli: AvdManagerCli,
) {

    private val _emulators = MutableStateFlow<List<Emulator>>(emptyList())

    val emulators: Flow<List<Emulator>> by ::_emulators

    fun refresh() {
        _emulators.value = emulatorCli.list()
            .map { Emulator(name = it) }
    }

    fun start(emulator: Emulator) {
        emulatorCli.start(emulator.name)
    }

    fun create(name: String) {
        avdManagerCli.create(name)
    }

    fun edit(emulator: Emulator) {
        // TODO: Implement
        println("Edit: $emulator")
    }

    fun delete(emulator: Emulator) {
        // TODO: Implement
        println("Delete: $emulator")
        refresh()
    }
}

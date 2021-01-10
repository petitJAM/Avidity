import data.Emulator
import emulatortools.findEmulators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class EmulatorsRepository {

    private val _emulators = MutableStateFlow<List<Emulator>>(emptyList())

    val emulators: Flow<List<Emulator>> by ::_emulators

    fun refresh() {
        _emulators.value = findEmulators()
    }

    fun start(emulator: Emulator) {
        // TODO: Implement
        println("Start: $emulator")
    }

    fun create(emulator: Emulator) {
        // TODO: Implement
        println("Create: $emulator")
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

package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DesktopDialogProperties
import androidx.compose.ui.window.Dialog
import cli.AvdManagerCli
import cli.EmulatorCli
import data.Emulator
import data.EmulatorFormData
import data.EmulatorsRepository

@Composable
fun AvidityApp(
    onThemeToggle: () -> Unit,
) {
    val emulatorsRepository = EmulatorsRepository(
        EmulatorCli(),
        AvdManagerCli()
    ).also { it.refresh() }

    val emulators = emulatorsRepository.emulators.collectAsState(emptyList())

    val dialogState = remember { mutableStateOf(false) }

    AvidityContent(
        emulators = emulators,
        onThemeToggle = onThemeToggle,
        onCreateEmulatorClick = { dialogState.value = true },
        onEmulatorsRefreshClick = emulatorsRepository::refresh,
        onEmulatorStart = emulatorsRepository::start,
        onEmulatorEdit = emulatorsRepository::edit,
        onEmulatorDelete = emulatorsRepository::delete,
    )

    if (dialogState.value) {
        Dialog(
            properties = DesktopDialogProperties(title = "Create Emulator"),
            onDismissRequest = { dialogState.value = false }
        ) {
            NewEmulatorForm(
                onCreateClick = {
                    emulatorsRepository.create(it)
                    dialogState.value = false
                }
            )
        }
    }
}

@Composable
fun AvidityContent(
    emulators: State<List<Emulator>>,
    onThemeToggle: () -> Unit,
    onCreateEmulatorClick: () -> Unit,
    onEmulatorsRefreshClick: () -> Unit,
    onEmulatorStart: (emulator: Emulator) -> Unit,
    onEmulatorEdit: (emulator: Emulator) -> Unit,
    onEmulatorDelete: (emulator: Emulator) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emulators") },
                actions = {
                    IconButton(onClick = onEmulatorsRefreshClick) {
                        Icon(imageVector = Icons.TwoTone.Refresh, contentDescription = "Refresh")
                    }

                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = vectorXmlResource(if (MaterialTheme.colors.isLight) "dark_mode_24.xml" else "light_mode_24.xml"),
                            contentDescription = "Toggle theme",
                        )
                    }
                }
            )
        },
        bodyContent = {
            EmulatorList(
                emulators = emulators.value,
                modifier = Modifier.fillMaxSize(),
                onItemPlayClick = onEmulatorStart,
                onItemEditClick = onEmulatorEdit,
                onItemDeleteClick = onEmulatorDelete,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Emulator") },
                icon = { Icon(imageVector = Icons.TwoTone.Add, contentDescription = null) },
                onClick = onCreateEmulatorClick,
            )
        }
    )
}


@Composable
fun NewEmulatorForm(
    // TODO: This lambda should return a Result object
    //  sealed class Result { object Success; data class Error }
    onCreateClick: (formData: EmulatorFormData) -> Unit,
) {
    val valid = remember { mutableStateOf(false) }

    val name = remember { mutableStateOf(TextFieldValue()) }

    val revalidate = { valid.value = name.value.text.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name.value,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            label = { Text("Name") },
            onValueChange = { name.value = it; revalidate() },
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 16.dp),
            enabled = valid.value,
            onClick = {
                val formData = EmulatorFormData(name.value.text)
                onCreateClick(formData)
            },
        ) {
            Text("Create Emulator")
        }
    }
}

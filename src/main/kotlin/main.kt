import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarStyleAmbient
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Divider
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
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material.icons.twotone.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cli.EmulatorCli
import data.Emulator
import theme.AvidityDarkColorPalette
import theme.AvidityLightColorPalette
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

fun main() =
    Window(
        title = "AViDity",
        icon = getWindowIcon(),
    ) {
        val darkTheme = rememberSaveable { mutableStateOf(true) }

        DesktopMaterialTheme(colors = if (darkTheme.value) AvidityDarkColorPalette else AvidityLightColorPalette) {
            AvidityApp(darkTheme)
        }
    }

private fun getWindowIcon(): BufferedImage =
    try {
        ImageIO.read(Thread.currentThread().contextClassLoader.getResource("android-arms-crossed.png"))
    } catch (ignored: IOException) {
        BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    }

@Composable
fun AvidityApp(darkTheme: MutableState<Boolean>) {
    val emulatorsRepository = EmulatorsRepository(EmulatorCli())
        .also { it.refresh() }

    val emulators = emulatorsRepository.emulators.collectAsState(emptyList())

    val dialogState = remember { mutableStateOf(false) }

    AvidityContent(
        darkTheme = darkTheme,
        emulators = emulators,
        onCreateEmulatorClick = { dialogState.value = true },
        onEmulatorsRefreshClick = emulatorsRepository::refresh,
        onEmulatorStart = emulatorsRepository::start,
        onEmulatorEdit = emulatorsRepository::edit,
        onEmulatorDelete = emulatorsRepository::delete,
    )

    if (dialogState.value) {
        Dialog(
            properties = DialogProperties(title = "Create Emulator"),
            onDismissRequest = { dialogState.value = false }
        ) {
            NewEmulatorForm(
                onEmulatorCreated = {
                    emulatorsRepository.create(it)
                    dialogState.value = false
                }
            )
        }
    }
}

@Composable
fun AvidityContent(
    darkTheme: MutableState<Boolean>,
    emulators: State<List<Emulator>>,
    onCreateEmulatorClick: () -> Unit,
    onEmulatorsRefreshClick: () -> Unit,
    onEmulatorStart: (emulator: Emulator) -> Unit,
    onEmulatorEdit: (emulator: Emulator) -> Unit,
    onEmulatorDelete: (emulator: Emulator) -> Unit,
) {
    val bottomNavState = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emulators") },
                actions = {
                    IconButton(onClick = onEmulatorsRefreshClick) {
                        Icon(imageVector = Icons.TwoTone.Refresh, contentDescription = "Refresh")
                    }

                    IconButton(
                        onClick = { darkTheme.value = !darkTheme.value },
                    ) {
                        Icon(
                            imageVector = vectorXmlResource(if (darkTheme.value) "light_mode_24.xml" else "dark_mode_24.xml"),
                            contentDescription = "Toggle theme",
                        )
                    }
                }
            )
        },
        content = {
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
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = {
                        Icon(imageVector = vectorXmlResource("smartphone_24.xml"), contentDescription = "")
                    },
                    selected = true,
                    onClick = {
                        // TODO:
                    },
                    label = { Text("Manage Emulators") },
                    interactionSource = bottomNavState,
                )

                BottomNavigationItem(
                    icon = {
                        Icon(imageVector = vectorXmlResource("smartphone_24.xml"), contentDescription = "")
                    },
                    selected = false,
                    onClick = {
                        // TODO
                    },
                    label = { Text("Other thing") },
                    interactionSource = bottomNavState,
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmulatorList(
    emulators: List<Emulator>,
    modifier: Modifier = Modifier,
    onItemPlayClick: (emulator: Emulator) -> Unit,
    onItemEditClick: (emulator: Emulator) -> Unit,
    onItemDeleteClick: (emulator: Emulator) -> Unit,
) {
    Box(modifier = modifier) {
        val state = rememberLazyListState()
        val itemCount = emulators.size

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(end = 10.dp),
            state = state,
        ) {
            itemsIndexed(
                items = emulators,
                key = { index, emulator ->
                    "${emulator.name} $index"
                },
                itemContent = { _, emulator ->
                    EmulatorItem(
                        emulator = emulator,
                        onPlayClick = { onItemPlayClick(emulator) },
                        onEditClick = { onItemEditClick(emulator) },
                        onDeleteClick = { onItemDeleteClick(emulator) },
                    )
                }
            )
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            style = ScrollbarStyleAmbient.current,
            adapter = rememberScrollbarAdapter(
                scrollState = state,
                itemCount = itemCount,
                averageItemSize = 32.dp + 28.dp,
            )
        )
    }
}

@Composable
fun EmulatorItem(
    emulator: Emulator,
    onPlayClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = emulator.name,
            modifier = Modifier.align(Alignment.CenterStart).padding(16.dp),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSurface,
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onPlayClick,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.PlayArrow,
                    contentDescription = "Start ${emulator.name}",
                    tint = MaterialTheme.colors.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onEditClick,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Edit,
                    contentDescription = "Edit ${emulator.name}",
                    tint = MaterialTheme.colors.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
                    contentDescription = "Delete ${emulator.name}",
                    tint = MaterialTheme.colors.onSurface,
                )
            }
        }
    }

    Divider()
}

@Composable
fun NewEmulatorForm(
    onEmulatorCreated: (emulator: Emulator) -> Unit,
) {
    val valid = remember { mutableStateOf(false) }

    val name = remember { mutableStateOf(TextFieldValue()) }

    val revalidate = { valid.value = name.value.text.isNotBlank() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name.value,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                label = { Text("Name") },
                onValueChange = { name.value = it; revalidate() },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = valid.value,
                onClick = {
                    val newEmulator = Emulator(name.value.text)
                    onEmulatorCreated(newEmulator)
                },
            ) {
                Text("Create Emulator")
            }
        }
    }
}

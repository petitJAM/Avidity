import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import data.Emulator
import theme.AvidityDarkColorPalette
import theme.AvidityLightColorPalette
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

fun main() = Window(
    title = "AViDity",
    icon = getWindowIcon(),
) {
    val darkTheme = savedInstanceState { true }
    MaterialTheme(colors = if (darkTheme.value) AvidityDarkColorPalette else AvidityLightColorPalette) {
        AvidityApp(darkTheme)
    }
}

private fun getWindowIcon(): BufferedImage = try {
    ImageIO.read(Thread.currentThread().contextClassLoader.getResource("android-arms-crossed.png"))
} catch (ignored: IOException) {
    BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
}

@Composable
fun AvidityApp(darkTheme: MutableState<Boolean>) {
    val emulators = remember {
        mutableStateListOf(
            Emulator("Pixel 2 XL"),
            Emulator("Nexus 5X"),
            Emulator("Pixel 4"),
            Emulator("Pixel 5"),
        )
    }

    val dialogState = remember { mutableStateOf(false) }

    AvidityContent(darkTheme, emulators, dialogState)

    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false }
        ) {
            NewEmulatorForm(
                onEmulatorCreated = {
                    emulators += it
                    dialogState.value = false
                }
            )
        }
    }
}

@Composable
fun AvidityContent(
    darkTheme: MutableState<Boolean>,
    emulators: SnapshotStateList<Emulator>,
    dialogState: MutableState<Boolean>,
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        TopAppBar(
            title = { Text("Emulators") },
            actions = {
                IconButton(
                    onClick = { darkTheme.value = !darkTheme.value }
                ) {
                    Icon(
                        imageVector = vectorXmlResource(if (darkTheme.value) "light_mode_24.xml" else "dark_mode_24.xml")
                    )
                }
            }
        )

        Box {
            EmulatorList(
                emulators = emulators,
                modifier = Modifier.fillMaxSize(),
                onItemDeleteClick = {
                    emulators.remove(it)
                }
            )

            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 24.dp, end = 16.dp),
                text = { Text("Add Emulator") },
                icon = { Icon(imageVector = Icons.TwoTone.Add) },
                onClick = {
                    dialogState.value = true
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmulatorList(
    emulators: List<Emulator>,
    modifier: Modifier = Modifier,
    onItemPlayClick: (emulator: Emulator) -> Unit = {},
    onItemEditClick: (emulator: Emulator) -> Unit = {},
    onItemDeleteClick: (emulator: Emulator) -> Unit = {},
) {
    Box(modifier = modifier) {
        val state = rememberLazyListState()
        val itemCount = emulators.size

        LazyColumnFor(
            items = emulators,
            modifier = Modifier.fillMaxSize().padding(end = 10.dp),
            state = state,
        ) { emulator ->
            EmulatorItem(
                emulator = emulator,
                onPlayClick = { onItemPlayClick(emulator) },
                onEditClick = { onItemEditClick(emulator) },
                onDeleteClick = { onItemDeleteClick(emulator) },
            )
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state,
                itemCount = itemCount,
                averageItemSize = 32.dp + 20.dp,
            )
        )
    }
}

@Composable
fun EmulatorItem(
    emulator: Emulator,
    onPlayClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
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
                    tint = MaterialTheme.colors.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onEditClick,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Edit,
                    tint = MaterialTheme.colors.onSurface,
                )
            }

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onDeleteClick,
            ) {
                Icon(
                    imageVector = Icons.TwoTone.Delete,
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

    ScrollableColumn(
        modifier = Modifier.fillMaxSize(),
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
                val newEmulator = Emulator(name.value.text)
                onEmulatorCreated(newEmulator)
            },
        ) {
            Text("Create Emulator")
        }
    }
}

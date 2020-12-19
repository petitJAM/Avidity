import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import theme.AvidityColorPalette

fun main() = Window(title = "AViDity") {
    MaterialTheme(colors = AvidityColorPalette) {
        AvidityApp()
    }
}

@Composable
fun AvidityApp() {
    val emulators = mutableStateListOf(
        Emulator("Pixel 2 XL"),
        Emulator("Nexus 5X"),
        Emulator("Pixel 4"),
        Emulator("Pixel 5"),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(title = { Text("Emulators") })

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
                    emulators.add(Emulator("New Emulator"))
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
    val hovered = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerMoveFilter(
                onEnter = {
                    hovered.value = true
                    false
                },
                onExit = {
                    hovered.value = false
                    false
                }
            )
            .background(if (hovered.value) Color.LightGray else Color.Unspecified)
    ) {
        Text(
            text = emulator.name,
            modifier = Modifier.align(Alignment.CenterStart).padding(16.dp),
            style = MaterialTheme.typography.h6
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onPlayClick,
            ) {
                Icon(imageVector = Icons.TwoTone.PlayArrow)
            }
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onEditClick,
            ) {
                Icon(imageVector = Icons.TwoTone.Edit)
            }

            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onDeleteClick,
            ) {
                Icon(imageVector = Icons.TwoTone.Delete)
            }
        }
    }

    Divider()
}

data class Emulator(
    val name: String,
)
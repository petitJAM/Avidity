import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp

fun main() = Window(title = "AViDity") {
    MaterialTheme {
        AvidityApp()
    }
}

@Composable
fun AvidityApp() {
    val fakeEmulators = listOf(
        Emulator("Pixel 2 XL"),
        Emulator("Nexus 5X"),
        Emulator("Pixel 4"),
        Emulator("Pixel 5"),
    ).let {
        // Make a lot of them
        it + it + it + it + it + it + it + it + it + it + it + it + it + it
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Emulators",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.CenterStart).padding(8.dp),
            )

            Icon(
                imageVector = Icons.TwoTone.Add,
                modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp),
            )
        }
        Divider()
        EmulatorList(fakeEmulators)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmulatorList(
    emulators: List<Emulator>,
) {
    Box {
        val state = rememberLazyListState()
        val itemCount = emulators.size

        LazyColumnFor(
            items = emulators,
            modifier = Modifier.fillMaxSize().padding(end = 12.dp),
            state = state,
        ) { emulator ->
            EmulatorItem(emulator)
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
fun EmulatorItem(emulator: Emulator) {
    val hovered = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.h6
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            Icon(
                imageVector = Icons.TwoTone.PlayArrow,
                modifier = Modifier.padding(4.dp),
            )
            Icon(
                imageVector = Icons.TwoTone.Edit,
                modifier = Modifier.padding(4.dp),
            )
        }
    }

    Divider()
}

data class Emulator(
    val name: String,
)
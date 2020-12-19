import androidx.compose.animation.expandHorizontally
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
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
        Text("Emulators", style = MaterialTheme.typography.h4)
        Divider()
        EmulatorList(fakeEmulators)
    }
}

@Composable
fun EmulatorList(
    emulators: List<Emulator>,
) {
    LazyColumnFor(modifier = Modifier.fillMaxWidth(), items = emulators) { emulator ->
        EmulatorItem(emulator)
    }
}

@Composable
fun EmulatorItem(emulator: Emulator) {
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(emulator.name, modifier = Modifier.align(Alignment.CenterStart))

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
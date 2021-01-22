package ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollbarStyleAmbient
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Emulator

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
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollbarStyleAmbient
import androidx.compose.foundation.VerticalScrollbar
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DesktopDialogProperties
import androidx.compose.ui.window.Dialog
import cli.EmulatorCli
import data.Emulator
import theme.AvidityDarkColorPalette
import theme.AvidityLightColorPalette
import ui.AvidityApp
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

fun main() =
    Window(
        title = "AViDity",
        icon = getWindowIcon(),
    ) {
        val darkTheme = savedInstanceState { true }
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

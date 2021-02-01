import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.Window
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
        val darkTheme = remember { mutableStateOf(true) }

        DesktopMaterialTheme(colors = if (darkTheme.value) AvidityDarkColorPalette else AvidityLightColorPalette) {
            AvidityApp(onThemeToggle = { darkTheme.value = !darkTheme.value })
        }
    }

private fun getWindowIcon(): BufferedImage =
    try {
        ImageIO.read(Thread.currentThread().contextClassLoader.getResource("android-arms-crossed.png"))
    } catch (ignored: IOException) {
        BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    }

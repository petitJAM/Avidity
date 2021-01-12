package cli

import androidx.compose.ui.platform.DesktopPlatform
import cli.windows.WindowsEmulatorCli

interface EmulatorCli {
    fun list(): List<String>
    fun start(name: String)

    companion object {
        val currentImplementation: EmulatorCli
            get() = when (DesktopPlatform.Current) {
                DesktopPlatform.Linux -> TODO("No Linux EmulatorCli implementation yet! :(")
                DesktopPlatform.Windows -> WindowsEmulatorCli
                DesktopPlatform.MacOS -> TODO("No Mac EmulatorCli implementation yet! :(")
            }
    }
}

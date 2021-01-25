package org.qme.installer;

import org.qme.release.QmeRelease;
import java.io.*;

/**
 * Installer for Windows 10
 * @since 1.0.0
 * @author Tom
 */

public class WindowsInstaller extends Installer {

    public WindowsInstaller() {
        steps = 4;
    }

    @Override
    public void install(QmeRelease release) {
        coreInstall(release, System.getProperty("user.home"));
        complete();
    }

    @Override
    public boolean isInstalled(String version) {
        return new File(System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar").exists();
    }

    @Override
    public void launchVersion(String version) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar " + System.getProperty("user.home") +
                    "/.qme/" + version + "/" + version + ".jar");
        } catch (IOException exception) {
            System.out.println("Failed to launch version " + version);
            exception.printStackTrace();
        }
    }
}

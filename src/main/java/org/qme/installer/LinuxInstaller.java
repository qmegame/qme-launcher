package org.qme.installer;

import org.qme.release.QmeRelease;

import java.io.File;
import java.io.IOException;

/**
 * Installer for linux based operating systems
 * @since 1.0.0
 * @author cameron
 */
public class LinuxInstaller extends Installer {

    public LinuxInstaller() {
        steps = 4;
    }

    @Override
    public void install(QmeRelease release) {
        coreInstall(release, System.getProperty("user.home"));
        complete();
    }

    @Override
    public boolean isInstalled(String version) {
        // this is kinda duplicated. >:-(
        log("Installing for linux");
        return new File(
                System.getProperty("user.home") + "/.qme/"
                        + version + "/" + version + ".jar"
        ).exists();
    }

    @Override
    public void launchVersion(String version) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar " + System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar");
            // TODO: Log stdout of process to the output text area.
        } catch (IOException exception) {
            System.out.println("Failed to launch version " + version);
            exception.printStackTrace();
        }
    }

}

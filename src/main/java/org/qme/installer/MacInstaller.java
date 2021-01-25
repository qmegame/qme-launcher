package org.qme.installer;

import java.io.File;

/**
 * Installation for Mac - nearly identical to that for Linux.
 * @since 1.0.0
 */
import org.qme.release.QmeRelease;

public class MacInstaller extends Installer {

    /**
     * Same as Linux
     */
    public MacInstaller() {
        steps = 4;
    }

    /**
     * Install in home directory.
     * @param version which version
     */
    @Override
    public void install(String version) {
        System.out.println("Installing for mac ...");
        coreInstall(version, System.getProperty("user.home"));
    }

    @Override
    public boolean isInstalled(String version) {
        return new File(
                System.getProperty("user.home") + "/.qme/"
                        + version + "/" + version + ".jar"
        ).exists();
    }

    @Override
    public void launchVersion(String version) {
        Runtime.getRuntimee().exec(
                "java -XstartOnFirstThread -jar " +
                System.getProperty("user.home") + "/.qme/"
                + version + "/" + version + ".jar"
        );
    }
}

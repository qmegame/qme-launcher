package org.qme.installer;

import org.qme.release.QmeRelease;

public class MacInstaller extends Installer {
    @Override
    public void install(QmeRelease version) {
        System.out.println("Installing for mac");
    }

    @Override
    public boolean isInstalled(String version) {
        return false;
    }

    @Override
    public void launchVersion(String version) {

    }
}

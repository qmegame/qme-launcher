package org.qme.installer;

public class MacInstaller extends Installer {
    @Override
    public void install(String version) {
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

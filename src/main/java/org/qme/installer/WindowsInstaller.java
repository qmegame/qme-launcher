package org.qme.installer;

public class WindowsInstaller extends Installer {
    @Override
    public void install(String version) {
        System.out.println("Installing for windows");
    }

    @Override
    public boolean isInstalled(String version) {
        return false;
    }
}

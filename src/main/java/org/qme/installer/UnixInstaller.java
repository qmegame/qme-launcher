package org.qme.installer;

public class UnixInstaller extends Installer {
    @Override
    public void install(String version) {
        System.out.println("Installing for unix");
    }

    @Override
    public boolean isInstalled(String version) {
        return false;
    }

    @Override
    public void launchVersion(String version) {

    }
}

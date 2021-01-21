package org.qme.installer;

public class MacInstaller extends Installer {
    @Override
    public void install(String version) {
        System.out.println("Installing for mac");
        super.coreInstall(version, System.getProperty("user.home"));
    }

    @Override
    public boolean isInstalled(String version) {
        return false;
    }

    @Override
    public void launchVersion(String version) {
        // "java -XstartOnFirstThread -jar ~/.qme/" + version + "qme5.jar";
    }
}

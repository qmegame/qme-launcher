package org.qme.installer;

import org.qme.Main;

import javax.swing.*;

public abstract class Installer {

    Integer steps = 0;
    Integer currentStep = 0;
    JTextArea progressMonitor;

    public static Installer getInstaller() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("Detecting proper installation for operating system " + os);
        switch (os) {
            case "win":
                return new WindowsInstaller();
            case "linux":
                return new LinuxInstaller();
            case "mac":
                return new MacInstaller();
            case "nix":
            case "nux":
            case "aix":
                return new UnixInstaller();
            default:
                return null;
        }
    }

    /**
     * Marks the start of a step for installation
     * @param string the step to be logged
     */
    void step(String string) {
        currentStep += 1;
        System.out.println("Installing [" + currentStep + "/"+ steps +"]: " + string);
        progressMonitor.append("\nInstalling [" + currentStep + "/"+ steps +"]: " + string);
    }

    /**
     * Logs debug information about the installtion
     */
    void log(String string) {
        System.out.println(string);
        progressMonitor.append("\n" + string);
    }

    /**
     * Fails an installation
     * @param string a reason to be reported for why it failed
     */
    void fail(String string) {
        System.out.println("Failed: " + string);
        progressMonitor.append("\nFailed:" + string);
    }

    /**
     * Marks an installation as complete
     */
    void complete() {
        currentStep = 0;
        progressMonitor.append("\nInstallation successful");
    }

    /**
     * Sets the progress bar that should be updated
     * @param progressMonitor the progress bar to be updated
     */
    public void setProgress(JTextArea progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    /**
     * Downloads and installs a specified version
     * @param version the version to be installed
     */
    public abstract void install(String version);

    /**
     * Checks if a version is installed
     * @param version the version to check
     * @return if the version is installed
     */
    public abstract boolean isInstalled(String version);

    /**
     * Launches a version
     * @param version the version to be launched
     */
    public abstract void launchVersion(String version);

}

package org.qme.installer;

import javax.swing.*;

/**
 * Abstract class for installers
 * @author cameron
 * @since 1.0.0
 */
public abstract class Installer {

    /**
     * The total amount of steps in this installation
     */
    Integer steps = 0;

    /**
     * The current step in this installation
     */
    Integer currentStep = 0;

    /**
     * The UI component to report progress to
     */
    JTextArea progressMonitor;

    /**
     * Get the proper installer instance for the users operating system
     * @return A valid installer instance
     */
    public static Installer getInstaller() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("Detecting proper installation for operating system " + os);
        return switch (os) {
            case "windows 10" -> new WindowsInstaller();
            case "linux" -> new LinuxInstaller();
            case "mac" -> new MacInstaller();
            case "nix", "nux", "aix" -> new UnixInstaller();
            default -> null;
        };
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

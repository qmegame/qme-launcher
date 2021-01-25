package org.qme.installer;

import org.qme.release.QmeRelease;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        switch (os) {
            case "win":
                return new WindowsInstaller();
            case "linux":
                return new LinuxInstaller();
            case "mac os x":
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
        if (progressMonitor != null) {
            progressMonitor.append("\nInstalling [" + currentStep + "/"+ steps +"]: " + string);
        }
    }

    /**
     * Logs debug information about the installtion
     */
    void log(String string) {
        System.out.println(string);
        if (progressMonitor != null) {
            progressMonitor.append("\n" + string);
        }
    }

    /**
     * Fails an installation
     * @param string a reason to be reported for why it failed
     */
    void fail(String string) {
        System.out.println("Failed: " + string);
        if (progressMonitor != null) {
            progressMonitor.append("\nFailed:" + string);
        }
    }

    /**
     * Marks an installation as complete
     */
    void complete() {
        currentStep = 0;
        if (progressMonitor != null) {
            progressMonitor.append("\nInstallation successful");
        }
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
     * @param release the release to be installed
     * @param qdirectory the directory that the .qme folder will be placed in
     */
    public void coreInstall(QmeRelease release, String qdirectory) {
        String version = release.getVersion();

        step("Validating runtime");
        String jre = System.getProperty("java.version");
        log("Runtime version " + jre + " detected.");
        if (!jre.startsWith("15")) {
            fail("Unsupported java runtime environment " + jre + " installation failed. Please update your java version.");
            return;
        }
        log("Supported runtime detected");

        step("Creating directories");
        File mainDirectory = new File(qdirectory + "/.qme/" + version);
        File workingDirectory = new File(  qdirectory + "/.qme/" + version + "/qdata");
        workingDirectory.mkdirs();
        mainDirectory.mkdirs();
        log("Directories have been created.");

        step("Downloading version " + version);

        // https://stackoverflow.com/questions/22273045/java-getting-download-progress
        URL url = null;
        try {
            url = new URL(release.getDownloadUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) (url.openConnection());
            if (httpConnection.getResponseCode() == 404) {
                fail("Failed to start download. Please try again later.");
                return;
            }
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(qdirectory + "/.qme/" + version + "/" + version + ".jar");
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fileOutputStream, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final double currentProgress = ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);

                log("Downloading... " + currentProgress + "%");

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (IOException exception) {
            fail("Failed to download version " + version + ".");
            exception.printStackTrace();
            return;
        }

        step("Finalizing...");
    }

    /**
     * Install with the given version - on a per-OS basis.
     * @param version which version
     */
    public abstract void install(QmeRelease version);

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

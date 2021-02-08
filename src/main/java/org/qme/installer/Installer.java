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
 * This class acts as a base for installers
 * Installers that extend this class contain code that is for each specific operating system
 * When an installer needs to override code it should contain Java docs explaining the changes
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

        if (os.equalsIgnoreCase("windows 10")) {
            return new WindowsInstaller();
        } else if (os.equalsIgnoreCase("linux")) {
            return new LinuxInstaller();
        } else if (os.equalsIgnoreCase("mac") || os.equalsIgnoreCase("mac os x")) {
            return new MacInstaller();
        } else {
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
     * Logs debug information about the installation
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
    void coreInstall(QmeRelease release, String qdirectory) {
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
            long startTime = System.currentTimeMillis();
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);

                // Only show progress every 2 seconds
                if (startTime + 2000 < System.currentTimeMillis()) {
                    startTime = System.currentTimeMillis();
                    log("Downloading game files " + currentProgress + "%");
                }

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (IOException exception) {
            fail("Failed to download version " + version + ".");
            exception.printStackTrace();
            return;
        }

        step("Core install complete.");
    }

    /**
     * Install with the given version - on a per-OS basis.
     * @param release which release to install
     */
    public void install(QmeRelease release) {
        coreInstall(release, System.getProperty("user.home"));
        complete();
    }

    /**
     * Checks if a version is installed
     * @param version the version to check
     * @return if the version is installed
     */
    public boolean isInstalled(String version) {
        return new File(System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar").exists();
    }

    /**
     * Launches a version
     * @param version the version to be launched
     */
    public Process launchVersion(String version) throws IOException {
        return Runtime.getRuntime().exec("java -jar " + System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar");
    }

}

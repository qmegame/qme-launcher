package org.qme.installer;

import org.qme.release.QmeRelease;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Installer for linux based operating systems
 * @since 1.0.0
 * @author cameron
 */
public class LinuxInstaller extends Installer {

    public LinuxInstaller() {
        steps = 4;
    }

    @Override
    public void install(QmeRelease release) {
        String version = release.getVersion();

        /*
         * Step 1: Verify that the user is running Java JRE 15 or similar.
         * In the future we may need to update this to allow versions higher than 15.
         */
        step("Validating runtime");
        String jre = System.getProperty("java.version");
        log("Runtime version " + jre + " detected.");
        if (!jre.startsWith("15")) {
            fail("Unsupported java runtime environment " + jre + " installation failed. Please update your java version.");
            return;
        }
        log("Supported runtime detected.");
        // End of step 1

        /*
         * Step 2: Create necessary directories to install the application to if they don't exist
         */
        step("Creating directories");
        File mainDirectory = new File(System.getProperty("user.home") + "/.qme/" + version);
        File workingDirectory = new File(System.getProperty("user.home") +  "/.qme/" + version + "/qdata");
        workingDirectory.mkdirs();
        mainDirectory.mkdirs();
        log("Directories have been created.");
        // End of step 2

        /*
         * Step 3: Download the proper version of the game from the internet.
         * TODO: Make sure this is using jake's CDN and not just my upload server
         */
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
            FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.home") + "/.qme/" + version + "/qme.jar");
            java.io.BufferedOutputStream bout = new BufferedOutputStream(fileOutputStream, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                final int currentProgress = (int) ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100d);

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
        log("Download completed successfully.");
        // End of step 3

        /*
         * Step 4: Any final most likely operating system specific steps like for example adding a shortcut
         * TODO: Add a desktop entry for the launcher after installation is finished (Prompt the user first) so the OS recognises the launcher as an application
         */
        step("Finalizing...");
        // Literally nothing here yet.
        // End of step 4

        complete();
    }

    @Override
    public boolean isInstalled(String version) {
        return new File(System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar").exists();
    }

    @Override
    public void launchVersion(String version) {
        try {
            Process process = Runtime.getRuntime().exec("java -jar " + System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar");
            // TODO: Log stdout of process to the output text area.
        } catch (IOException exception) {
            System.out.println("Failed to launch version " + version);
            exception.printStackTrace();
        }
    }
}

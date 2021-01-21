package org.qme.installer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Installer for linux based operating systems
 * @since 0.0.1
 * @author cameron
 */
public class LinuxInstaller extends Installer {

    public LinuxInstaller() {
        steps = 4;
    }

    @Override
    public void install(String version) {
        step("Validating runtime");
        String jre = System.getProperty("java.version");
        log("Runtime version " + jre + " detected.");
        if (!jre.startsWith("15")) {
            fail("Unsupported java runtime environment " + jre + " installation failed. Please update your java version.");
            return;
        }
        log("Supported runtime detected");

        step("Creating directories");
        File mainDirectory = new File(System.getProperty("user.home") + "/.qme/" + version);
        File workingDirectory = new File(System.getProperty("user.home") +  "/.qme/" + version + "/qdata");
        workingDirectory.mkdirs();
        mainDirectory.mkdirs();
        log("Directories have been created.");

        step("Downloading version " + version);

        // https://stackoverflow.com/questions/22273045/java-getting-download-progress
        URL url = null;
        try {
            url = new URL("https://cameron.media/u/82QtAR8wjb.png");
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
            FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.home") + "/.qme/" + version + "/test.png");
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

        step("Finalizing...");

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
        } catch (IOException exception) {
            System.out.println("Failed to launch version " + version);
            exception.printStackTrace();
        }
    }
}

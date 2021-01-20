package org.qme.installer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LinuxInstaller extends Installer {
    @Override
    public void install(String version) {
        System.out.println("Validating runtime...");
        String jre = System.getProperty("java.version");
        System.out.println("Runtime version " + jre + " detected.");
        if (!jre.startsWith("15")) {
            System.out.println("Failed: Unsupported java runtime environment " + jre + " installation failed. Please update your java version.");
            return;
        }

        System.out.println("Creating directories...");
        File mainDirectory = new File(System.getProperty("user.home") + "/.qme/" + version);
        File workingDirectory = new File("~/.qme/" + version + "/qdata");
        workingDirectory.mkdirs();
        mainDirectory.mkdirs();
        System.out.println("Directories have been created.");

        System.out.println("Downloading version " + version + "...");

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
                System.out.println("Failed: Failed to start download. Please try again later.");
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

                System.out.println("Downloading... " + currentProgress + "%");

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (IOException exception) {
            System.out.println("Failed: Failed to download version " + version + ".");
            exception.printStackTrace();
            return;
        }

        System.out.println("Finalizing...");

        System.out.println("Installation complete.");
    }

    @Override
    public boolean isInstalled(String version) {
        return false;
    }
}

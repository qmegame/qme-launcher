package org.qme.installer;

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
    public void install(String version) {
        super.coreInstall(version, System.getProperty("user.home"));
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

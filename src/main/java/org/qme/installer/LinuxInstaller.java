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
    public void install(String version) {
        super.coreInstall(version, System.getProperty("user.home"));
    }
}

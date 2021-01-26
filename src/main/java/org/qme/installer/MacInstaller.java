package org.qme.installer;

import java.io.IOException;

/**
 * Installer for mac
 * @since 1.0.0
 */
public class MacInstaller extends Installer {

    public MacInstaller() {
        steps = 4;
    }

    /**
     * Override because MacOS requires -XstartOnFirstThread arguments when launching jars
     */
    @Override
    public Process launchVersion(String version) throws IOException {
        return Runtime.getRuntime().exec("java -XstartOnFirstThread -jar " + System.getProperty("user.home") + "/.qme/" + version + "/" + version + ".jar");
    }
}

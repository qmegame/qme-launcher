package org.qme.installer;

public abstract class Installer {
    public abstract void install(String version);
    public abstract boolean isInstalled(String version);

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

}

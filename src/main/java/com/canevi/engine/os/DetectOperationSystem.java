package com.canevi.engine.os;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DetectOperationSystem {
    private static final String osName = System.getProperty("os.name").toLowerCase();
    private static final String osArch = System.getProperty("os.arch").toLowerCase();

    public static void main(String[] args) {
        String os = getOS();
        String arch = getArch();
        log.info("Arcitecture: " + arch);

        // Use os and arch to select the appropriate Maven profile
        String profileId = selectProfile(os, arch);

        // Execute Maven build with the selected profile
        executeMavenBuild(profileId);
    }

    public static String getOS() {
        if (osName.contains("win")) {
            log.info("Running on Windows");
            return "windows";
        } else if(osName.contains("nix") || osName.contains("nux")) {
            log.info("Running on Unix/Linux");
            return "unix";
        } else if (osName.contains("mac")) {
            log.info("Running on Mac");
            return "mac";
        } else {
            log.info("Running on other OS");
            return "other";
        }
    }

    public static String getArch() {
        if (osArch.contains("aarch")) {
            return "aarch";
        }
        return osArch.contains("64") ? "64" : "32";
    }

    private static String selectProfile(String os, String arch) {
        // Your logic to determine the Maven profile based on os and arch
        // Example: return a profile id based on the detected os and arch
        if ("windows".equals(os) && "64".equals(arch)) {
            return "lwjgl-natives-windows-amd64";
        } else if ("unix".equals(os)) {
            return "lwjgl-natives-macos-x86_64";
        } else if ("mac".equals(os) && "aarch".equals(arch)) {
            return "lwjgl-natives-macos-aarch64";
        } else {
            return "default-profile";
        }
    }

    private static void executeMavenBuild(String profileId) {
        String mavenCommand = "\nmvn clean package -P " + profileId + "\njava -jar .\\target\\lwjgl_engine-1.0-SNAPSHOT-shaded.jar  ";
        log.info(mavenCommand);
    }
}

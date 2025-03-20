package com.ldplayer.auto.examples;

import com.ldplayer.auto.LDPlayer;
import com.ldplayer.auto.LDType;

public class AutoLDPlayerExample {
    public static void main(String[] args) {
        // Set the path to your LDPlayer installation
        LDPlayer.setPathLD("D:\\LDPlayer\\LDPlayer9\\ldconsole.exe");

        try {
            // List all devices
            System.out.println("Available devices:");
            LDPlayer.getDevices().forEach(System.out::println);

            // Create a new instance if none exists
            String emulatorName = "test-instance";
            System.out.println("\nCreating new instance: " + emulatorName);
            LDPlayer.create(emulatorName);

            // Start the emulator
            System.out.println("Starting emulator...");
            LDPlayer.open(LDType.NAME, emulatorName);

            // Wait for emulator to start
            Thread.sleep(60000);

            // Check if it's running
            boolean isRunning = LDPlayer.isDeviceRunning(LDType.NAME, emulatorName);
            System.out.println("Emulator running: " + isRunning);

            if (isRunning) {
                // Example: Install and run an app
                // Replace with your actual APK path
                String apkPath = "C:\\path\\to\\your\\app.apk";
                if (new java.io.File(apkPath).exists()) {
                    System.out.println("Installing app...");
                    LDPlayer.installAppFile(LDType.NAME, emulatorName, apkPath);
                    
                    // Replace with your app's package name
                    String packageName = "com.example.app";
                    System.out.println("Running app...");
                    LDPlayer.runApp(LDType.NAME, emulatorName, packageName);
                }

                // Example: Change some properties
                System.out.println("Configuring emulator...");
                LDPlayer.changeProperty(LDType.NAME, emulatorName, "--cpu 2 --memory 2048");

                // Example: Set GPS location
                System.out.println("Setting GPS location...");
                LDPlayer.locate(LDType.NAME, emulatorName, "37.7749", "-122.4194");

                // Wait a bit before closing
                Thread.sleep(5000);

                // Close the emulator
                System.out.println("Closing emulator...");
                LDPlayer.close(LDType.NAME, emulatorName);
            }

        } catch (Exception e) {
            System.err.println("Error running example: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
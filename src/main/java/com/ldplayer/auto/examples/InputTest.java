package com.ldplayer.auto.examples;

import com.ldplayer.auto.LDPlayer;
import java.awt.image.BufferedImage;
import java.io.File;

public class InputTest {
    public static void main(String[] args) {
        try {
            // Get list of devices
            System.out.println("Available devices:");
            var devices = LDPlayer.getDevices();
            if (devices.isEmpty()) {
                System.out.println("No devices found!");
                return;
            }

            // Use the first device
            String deviceId = devices.get(0);
            System.out.println("Using device: " + deviceId);

            // Take initial screenshot
            System.out.println("Taking initial screenshot...");
            BufferedImage screenshot1 = LDPlayer.getScreenshot(deviceId);
            LDPlayer.saveScreenshot(deviceId, "before_input.png");

            // Perform some input operations
            System.out.println("Performing input operations...");
            
            // Open an app (e.g., calculator)
            LDPlayer.runApp(LDType.NAME, deviceId, "com.android.calculator2");
            Thread.sleep(2000); // Wait for app to open

            // Input some numbers and operations
            LDPlayer.tap(deviceId, 100, 200);  // Tap a number
            Thread.sleep(500);
            
            LDPlayer.inputText(deviceId, "123");
            Thread.sleep(500);

            // Swipe gesture
            LDPlayer.swipe(deviceId, 100, 300, 300, 300, 1000);
            Thread.sleep(500);

            // Long press
            LDPlayer.longPress(deviceId, 200, 200, 2000);
            Thread.sleep(500);

            // Take final screenshot
            System.out.println("Taking final screenshot...");
            BufferedImage screenshot2 = LDPlayer.getScreenshot(deviceId);
            LDPlayer.saveScreenshot(deviceId, "after_input.png");

            // Press some keys
            LDPlayer.back(deviceId);
            Thread.sleep(500);
            LDPlayer.home(deviceId);

            System.out.println("Test completed! Check before_input.png and after_input.png");

        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
package com.ldplayer.auto;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LDPlayer {
    private static String pathLD = "D:\\LDPlayer\\LDPlayer9\\ldconsole.exe";
    
    public static void setPathLD(String path) {
        pathLD = path;
    }

    public static String getPathLD() {
        return pathLD;
    }

    // Core control methods
    public static void open(LDType ldType, String nameOrId) {
        executeLDCommand(String.format("launch --%s %s", ldType.getName(), nameOrId));
    }

    public static void openApp(LDType ldType, String nameOrId, String packageName) {
        executeLDCommand(String.format("launchex --%s %s --packagename %s", ldType.getName(), nameOrId, packageName));
    }

    public static void close(LDType ldType, String nameOrId) {
        executeLDCommand(String.format("quit --%s %s", ldType.getName(), nameOrId));
    }

    public static void closeAll() {
        executeLDCommand("quitall");
    }

    public static void reboot(LDType ldType, String nameOrId) {
        executeLDCommand(String.format("reboot --%s %s", ldType.getName(), nameOrId));
    }

    // Emulator management methods
    public static void create(String name) {
        executeLDCommand(String.format("add --name %s", name));
    }

    public static void copy(String name, String fromNameOrId) {
        executeLDCommand(String.format("copy --name %s --from %s", name, fromNameOrId));
    }

    public static void delete(LDType ldType, String nameOrId) {
        executeLDCommand(String.format("remove --%s %s", ldType.getName(), nameOrId));
    }

    public static void rename(LDType ldType, String nameOrId, String titleNew) {
        executeLDCommand(String.format("rename --%s %s --title %s", ldType.getName(), nameOrId, titleNew));
    }

    // App management methods
    public static void installAppFile(LDType ldType, String nameOrId, String fileName) {
        executeLDCommand(String.format("installapp --%s %s --filename \"%s\"", ldType.getName(), nameOrId, fileName));
    }

    public static void installAppPackage(LDType ldType, String nameOrId, String packageName) {
        executeLDCommand(String.format("installapp --%s %s --packagename %s", ldType.getName(), nameOrId, packageName));
    }

    public static void uninstallApp(LDType ldType, String nameOrId, String packageName) {
        executeLDCommand(String.format("uninstallapp --%s %s --packagename %s", ldType.getName(), nameOrId, packageName));
    }

    public static void runApp(LDType ldType, String nameOrId, String packageName) {
        executeLDCommand(String.format("runapp --%s %s --packagename %s", ldType.getName(), nameOrId, packageName));
    }

    public static void killApp(LDType ldType, String nameOrId, String packageName) {
        executeLDCommand(String.format("killapp --%s %s --packagename %s", ldType.getName(), nameOrId, packageName));
    }

    // Device configuration methods
    public static void locate(LDType ldType, String nameOrId, String lng, String lat) {
        executeLDCommand(String.format("locate --%s %s --LLI %s,%s", ldType.getName(), nameOrId, lng, lat));
    }

    public static void changeProperty(LDType ldType, String nameOrId, String cmd) {
        executeLDCommand(String.format("modify --%s %s %s", ldType.getName(), nameOrId, cmd));
    }

    public static void setProp(LDType ldType, String nameOrId, String key, String value) {
        executeLDCommand(String.format("setprop --%s %s --key %s --value %s", ldType.getName(), nameOrId, key, value));
    }

    public static String getProp(LDType ldType, String nameOrId, String key) {
        return executeLDCommandWithResult(String.format("getprop --%s %s --key %s", ldType.getName(), nameOrId, key));
    }

    public static String adb(LDType ldType, String nameOrId, String cmd) {
        return executeLDCommandWithResult(String.format("adb --%s \"%s\" --command \"%s\"", ldType.getName(), nameOrId, cmd));
    }

    // Device list methods
    public static List<String> getDevices() {
        String result = executeLDCommandWithResult("list").trim();
        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(result.split("\\n"));
    }

    public static List<String> getDevicesRunning() {
        String result = executeLDCommandWithResult("runninglist").trim();
        if (result.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(result.split("\\n"));
    }

    public static boolean isDeviceRunning(LDType ldType, String nameOrId) {
        String result = executeLDCommandWithResult(String.format("isrunning --%s %s", ldType.getName(), nameOrId)).trim();
        return "running".equals(result);
    }

    // Utility methods
    private static void executeLDCommand(String command) {
        try {
            List<String> commands = new ArrayList<>();
            commands.add(pathLD);
            commands.addAll(Arrays.asList(command.split(" ")));
            
            System.out.println("Executing command: " + String.join(" ", commands));
            
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // Read and print the output
            byte[] bytes = process.getInputStream().readAllBytes();
            String output = new String(bytes).trim();
            System.out.println("Command output:\n" + output);
            
            // Wait for completion and check exit code
            boolean completed = process.waitFor(30, TimeUnit.SECONDS);
            if (!completed) {
                process.destroyForcibly();
                throw new RuntimeException("Command timed out after 30 seconds: " + command);
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("Command failed with exit code " + exitCode + ": " + command + "\nOutput: " + output);
            }
            
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute LDPlayer command: " + command, e);
        }
    }

    private static String executeLDCommandWithResult(String command) {
        try {
            List<String> commands = new ArrayList<>();
            commands.add(pathLD);
            commands.addAll(Arrays.asList(command.split(" ")));
            
            System.out.println("Executing command: " + String.join(" ", commands));
            
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            // Read the output
            byte[] bytes = process.getInputStream().readAllBytes();
            String output = new String(bytes).trim();
            System.out.println("Command output:\n" + output);
            
            // Wait for completion and check exit code
            boolean completed = process.waitFor(30, TimeUnit.SECONDS);
            if (!completed) {
                process.destroyForcibly();
                throw new RuntimeException("Command timed out after 30 seconds: " + command);
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("Command failed with exit code " + exitCode + ": " + command + "\nOutput: " + output);
            }
            
            return output;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute LDPlayer command: " + command, e);
        }
    }

    // Input methods
    public static void tap(String deviceId, int x, int y) {
        adb(LDType.NAME, deviceId, String.format("shell input tap %d %d", x, y));
    }

    public static void longPress(String deviceId, int x, int y, int duration) {
        // Using swipe command with same start/end coordinates to simulate long press
        adb(LDType.NAME, deviceId, String.format("shell input swipe %d %d %d %d %d", x, y, x, y, duration));
    }

    public static void swipe(String deviceId, int startX, int startY, int endX, int endY, int duration) {
        adb(LDType.NAME, deviceId, String.format("shell input swipe %d %d %d %d %d", startX, startY, endX, endY, duration));
    }

    public static void inputText(String deviceId, String text) {
        // Replace spaces with %s for ADB input
        String escapedText = text.replace(" ", "%s");
        adb(LDType.NAME, deviceId, String.format("shell input text %s", escapedText));
    }

    public static void keyEvent(String deviceId, int keyCode) {
        adb(LDType.NAME, deviceId, String.format("shell input keyevent %d", keyCode));
    }

    public static BufferedImage getScreenshot(String deviceId) {
        try {
            // Create a temporary file for the screenshot
            File tempFile = File.createTempFile("ldplayer_screenshot_", ".png");
            String tempPath = tempFile.getAbsolutePath();
            
            // Take screenshot using ADB and save to temp file
            adb(LDType.NAME, deviceId, String.format("shell screencap -p /sdcard/screenshot.png"));
            adb(LDType.NAME, deviceId, String.format("pull /sdcard/screenshot.png \"%s\"", tempPath));
            adb(LDType.NAME, deviceId, "shell rm /sdcard/screenshot.png");

            // Read the image using ImageIO
            BufferedImage screenshot = javax.imageio.ImageIO.read(tempFile);
            
            // Clean up temp file
            tempFile.delete();
            
            return screenshot;
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot: " + e.getMessage(), e);
        }
    }

    public static void saveScreenshot(String deviceId, String filePath) {
        try {
            BufferedImage screenshot = getScreenshot(deviceId);
            String format = filePath.substring(filePath.lastIndexOf('.') + 1);
            javax.imageio.ImageIO.write(screenshot, format, new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot: " + e.getMessage(), e);
        }
    }

    // Common Android key codes
    public static final class KeyEvent {
        public static final int HOME = 3;
        public static final int BACK = 4;
        public static final int MENU = 82;
        public static final int VOLUME_UP = 24;
        public static final int VOLUME_DOWN = 25;
        public static final int POWER = 26;
        public static final int CAMERA = 27;
        public static final int ENTER = 66;
        public static final int DELETE = 67;
        public static final int TAB = 61;
        public static final int ESCAPE = 111;
        public static final int SPACE = 62;
    }

    // Directional methods (using KeyEvent constants)
    public static void back(String deviceId) {
        keyEvent(deviceId, KeyEvent.BACK);
    }

    public static void home(String deviceId) {
        keyEvent(deviceId, KeyEvent.HOME);
    }

    public static void menu(String deviceId) {
        keyEvent(deviceId, KeyEvent.MENU);
    }
} 
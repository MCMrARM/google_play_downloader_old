package com.github.mcmrarm.playdl;

import com.github.mcmrarm.playdl.proto.GooglePlay;
import sun.rmi.runtime.Log;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, LoginManager.LoginException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Email: ");
        String email = reader.readLine();
        System.out.print("Password: ");
        String pass = reader.readLine();

        System.out.print("Language [en]: ");
        DeviceInfo.lang = reader.readLine();
        if (DeviceInfo.lang.equals("")) {
            DeviceInfo.lang = "en";
        }

        File aIdFile = new File("android_id.txt");
        if (aIdFile.exists()) {
            DeviceInfo.androidId = new BufferedReader(new FileReader(aIdFile)).readLine();
            System.out.println("Using android id from file: " + DeviceInfo.androidId);
        } else {
            System.out.print("Do you want to use x86 as the architecture? [y/N]: ");
            String x86 = reader.readLine();
            if (x86.toLowerCase().startsWith("y")) {
                DeviceInfo.isX86 = true;
            }

            CheckinManager mgr = new CheckinManager();
            mgr.login(email, pass);
            String aId = mgr.getAndroidId();
            if (aId == null) {
                throw new RuntimeException("Failed to get an android ID");
            }
            DeviceInfo.androidId = aId;
            System.out.println("Using android id: " + aId);
            BufferedWriter writer = new BufferedWriter(new FileWriter(aIdFile));
            writer.write(aId + "\n");
            writer.flush();
            writer.close();
        }

        LoginManager lmgr = new LoginManager("androidmarket", "com.android.vending");
        lmgr.setEmail(email);
        lmgr.login(pass, DeviceInfo.androidId);

        System.out.print("Package name: ");
        String pkg = reader.readLine();
        App app = new App(lmgr, pkg);
        app.download(app.deliver(), pkg + app.versionCode + ".apk");
    }
}

package gg.codie.mineonline.patches;

import net.bytebuddy.asm.Advice;

import java.util.Arrays;

public class StringToCharArrayAdvice {
    public static String versionString;

    @Advice.OnMethodExit
    public static void intercept(@Advice.This String thisObj, @Advice.Return(readOnly = false) char[] returnObj) {
        try {
            // Version strings always start with Minecraft or 0.
            if (thisObj.startsWith("Minecraft") || thisObj.startsWith("0.")) {
                String versionString = (String) ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.patches.StringToCharArrayAdvice").getField("versionString").get(null);
                if(versionString != null) {
                    // Comparing them as strings doens't play well with the patch.
                    if (Arrays.equals(thisObj.getBytes(), versionString.getBytes())) {
                        returnObj = new char[]{' '};
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

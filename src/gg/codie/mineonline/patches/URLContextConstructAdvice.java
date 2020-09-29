package gg.codie.mineonline.patches;

import net.bytebuddy.asm.Advice;

import java.net.URL;

public class URLContextConstructAdvice {
    @Advice.OnMethodEnter
    static void intercept(@Advice.Argument(value = 0, readOnly = false) URL context, @Advice.Argument(value = 1, readOnly = false) String spec) {
        try {
            String updateURL = (String)ClassLoader.getSystemClassLoader().loadClass("gg.codie.mineonline.patches.URLConstructAdvice").getField("updateURL").get(null);
            if (updateURL != null && spec.matches("minecraft.jar\\?user=([^<]*)&ticket=deprecated")) {
                context = new URL(updateURL.substring(0, updateURL.lastIndexOf("/") + 1));
                spec = updateURL.substring(updateURL.lastIndexOf("/") + 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
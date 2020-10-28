package gg.codie.mineonline.patches.lwjgl;

import gg.codie.mineonline.Settings;
import gg.codie.mineonline.lwjgl.OnCreateListener;
import gg.codie.mineonline.lwjgl.OnUpdateListener;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

public class LWJGLDisplayPatch {

    public static OnUpdateListener updateListener;
    public static OnCreateListener createListener;

    public static void hijackLWJGLThreadPatch(boolean enableGreyScreenPatch) {
        Settings.loadSettings();

        LWJGLDisplayCreateAdvice.sampleCount = Settings.settings.optInt(Settings.SAMPLE_COUNT, 0);
        LWJGLDisplayCreateAdvice.coverageSampleCount = Settings.settings.optInt(Settings.COVERAGE_SAMPLE_COUNT, 0);
        LWJGLDisplayCreateAdvice.stencilCount = Settings.settings.optInt(Settings.STENCIL_COUNT, 0);

        try {
            if(enableGreyScreenPatch) {
                new ByteBuddy()
                        .redefine(LWJGLDisplayPatch.class.getClassLoader().loadClass("org.lwjgl.opengl.Display"))
                        .visit(Advice.to(LWJGLDisplayCreateAdvice.class).on(ElementMatchers.named("create").and(ElementMatchers.takesArgument(2, LWJGLDisplayPatch.class.getClassLoader().loadClass("org.lwjgl.opengl.ContextAttribs")))))
                        .visit(Advice.to(LWJGLDisplayUpdateAdvice.class).on(ElementMatchers.named("update").and(ElementMatchers.takesArgument(0, boolean.class))))
                        .visit(Advice.to(LWJGLSetDisplayConfigurationAdvice.class).on(ElementMatchers.named("setDisplayConfiguration")))
                        .make()
                        .load(Class.forName("org.lwjgl.opengl.Display").getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            } else {
                new ByteBuddy()
                        .redefine(LWJGLDisplayPatch.class.getClassLoader().loadClass("org.lwjgl.opengl.Display"))
                        .visit(Advice.to(LWJGLDisplayCreateAdvice.class).on(ElementMatchers.named("create").and(ElementMatchers.takesArgument(2, LWJGLDisplayPatch.class.getClassLoader().loadClass("org.lwjgl.opengl.ContextAttribs")))))
                        .visit(Advice.to(LWJGLDisplayUpdateAdvice.class).on(ElementMatchers.named("update").and(ElementMatchers.takesArgument(0, boolean.class))))
                        .make()
                        .load(Class.forName("org.lwjgl.opengl.Display").getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }
        } catch (ClassNotFoundException ex) {
            // If the lib isn't loaded the version must not need it, no need to patch it.
        }
    }
}

package gg.codie.mineonline.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import gg.codie.mineonline.LauncherFiles;
import gg.codie.mineonline.Properties;
import gg.codie.mineonline.gui.rendering.*;
import gg.codie.mineonline.gui.rendering.Renderer;
import gg.codie.mineonline.gui.rendering.animation.IPlayerAnimation;
import gg.codie.mineonline.gui.rendering.animation.IdlePlayerAnimation;
import gg.codie.mineonline.gui.rendering.shaders.StaticShader;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;

public class FormManager {
    static JFrame singleton;
    private static Canvas glCanvas = new Canvas();

    static StaticShader shader;
    static gg.codie.mineonline.gui.rendering.Renderer renderer;
    static Loader loader;
    static GameObject playerPivot;
    static PlayerGameObject playerGameObject;
    static Camera camera;
    static IPlayerAnimation playerAnimation;

    public static void main(String[] args) throws Exception {
        Properties.loadProperties();

        JFrame frame = new JFrame();
        frame.setVisible(true);

        singleton = frame;

        switchScreen(new LoginForm());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setPreferredSize(new Dimension(845, 476));
        frame.pack();

        frame.setResizable(false);
        frame.setVisible(true);

        glCanvas.setIgnoreRepaint(true);

        try {
            Display.setParent(glCanvas);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void switchScreen(IContainerForm containerForm) {
        singleton.getContentPane().setVisible(false);
        singleton.setContentPane(containerForm.getContent());
        singleton.getContentPane().setVisible(true);

        JPanel renderPanel = containerForm.getRenderPanel();

        if(renderPanel != null) {
            renderPanel.add(glCanvas, new GridConstraints());
            glCanvas.setSize(renderPanel.getSize());

            gamePrepare.run();
            EventQueue.invokeLater(gameMainLoop);
        }
    }



    public static Runnable gamePrepare = new Runnable() {
        public void run() {
            try {
                Display.setParent(glCanvas);
            } catch (Exception e) {}
            DisplayManager.createDisplay(glCanvas.getSize().width, glCanvas.getSize().height);

            shader = new StaticShader();
            renderer = new Renderer(shader);

            loader = new Loader();

            playerPivot = new GameObject("player_origin", new Vector3f(0, -2    , -40), new Vector3f(0, 30, 0), new Vector3f(1, 1, 1));

            playerGameObject = new PlayerGameObject("player", loader, shader, new Vector3f(0, -16, 0), new Vector3f(), new Vector3f(1, 1, 1));

            playerPivot.addChild(playerGameObject);

            playerGameObject.setSkin(LauncherFiles.CACHED_SKIN_PATH);
            playerGameObject.setCloak(LauncherFiles.CACHED_CLOAK_PATH);

            camera = new Camera();

            playerAnimation = new IdlePlayerAnimation();
            playerAnimation.reset(playerGameObject);
        }
    };



    public static Runnable gameMainLoop = new Runnable() {
        public void run() {
            renderer.prepare();
            // Camera roll lock.
            // Broken and not necessary.

//            if(playerPivot.getLocalRotation().z > 0) {
//                playerPivot.increaseRotation(new Vector3f(0, 0, -playerPivot.getLocalRotation().z));
//            }

            if(Mouse.isButtonDown(0)) {
                Vector3f currentRotation = playerPivot.getLocalRotation();
                Vector3f rotation = new Vector3f();

                // Camera pitch rotation with lock.
                // Currently broken.

//                float dy = Mouse.getDY();

//                if(currentRotation.x + (dy * -0.3f) > 30) {
//                    rotation.x = 30 - currentRotation.x;
//                } else if(currentRotation.x + (dy * -0.3f) < -30) {
//                    rotation.x = -30 - currentRotation.x;
//                } else {
//                    rotation.x = dy * -0.3f;
//                }

                rotation.y = (Mouse.getDX() * 0.5f);

//                System.out.println(rotation.toString());

                playerPivot.increaseRotation(rotation);
            }

            playerGameObject.update();

            playerAnimation.animate(playerGameObject);

            camera.move();

            shader.start();
            shader.loadViewMatrix(camera);

            renderer.render(playerGameObject, shader);

            shader.stop();

            //DisplayManager.updateDisplay();
            Display.update();

            try {
                Thread.sleep(12);
            } catch (Exception e) {

            }


            EventQueue.invokeLater(this);
        }
    };

}

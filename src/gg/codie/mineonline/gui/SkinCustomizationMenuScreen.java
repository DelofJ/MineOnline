package gg.codie.mineonline.gui;

import gg.codie.minecraft.client.Options;
import gg.codie.mineonline.LauncherFiles;
import gg.codie.mineonline.Settings;
import gg.codie.mineonline.gui.components.LargeButton;
import gg.codie.mineonline.gui.components.MediumButton;
import gg.codie.mineonline.gui.events.IOnClickListener;
import gg.codie.mineonline.gui.font.GUIText;
import gg.codie.mineonline.gui.rendering.Camera;
import gg.codie.mineonline.gui.rendering.DisplayManager;
import gg.codie.mineonline.gui.rendering.PlayerGameObject;
import gg.codie.mineonline.gui.rendering.Renderer;
import gg.codie.mineonline.gui.rendering.font.TextMaster;
import gg.codie.mineonline.gui.rendering.shaders.GUIShader;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;

public class SkinCustomizationMenuScreen implements IMenuScreen {
    MediumButton hatButton;
    MediumButton leftSleeveButton;
    MediumButton jacketButton;
    MediumButton rightSleeveButton;
    MediumButton leftPantsLegButton;
    MediumButton rightPantsLegButton;
    LargeButton doneButton;
    GUIText label;

    boolean hat = true;
    boolean jacket = true;
    boolean leftSleeve = true;
    boolean rightSleeve = true;
    boolean leftPantsLeg = true;
    boolean rightPantsLeg = true;

    public SkinCustomizationMenuScreen() {
        hat = Settings.singleton.getShowHat();
        jacket = Settings.singleton.getShowJacket();
        leftSleeve = Settings.singleton.getShowLeftSleeve();
        rightSleeve = Settings.singleton.getShowRightSleeve();
        leftPantsLeg = Settings.singleton.getShowLeftPantsLeg();
        rightPantsLeg = Settings.singleton.getShowRightPantsLeg();

        hatButton = new MediumButton("Hat: " + (hat ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) - 308, (DisplayManager.getDefaultHeight() / 2) - 40), new IOnClickListener() {
            @Override
            public void onClick() {
                hat = !hat;
                hatButton.setName("Hat: " + (hat ? "ON" : "OFF"));
            }
        });

        jacketButton = new MediumButton("Jacket: " + (jacket ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) + 8, (DisplayManager.getDefaultHeight() / 2) - 40), new IOnClickListener() {
            @Override
            public void onClick() {
                jacket = !jacket;
                jacketButton.setName("Jacket: " + (jacket ? "ON" : "OFF"));
            }
        });

        leftSleeveButton = new MediumButton("Left Sleeve: " + (leftSleeve ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) - 308, (DisplayManager.getDefaultHeight() / 2) + 8), new IOnClickListener() {
            @Override
            public void onClick() {
                leftSleeve = !leftSleeve;
                leftSleeveButton.setName("Left Sleeve: " + (leftSleeve ? "ON" : "OFF"));
            }
        });

        rightSleeveButton = new MediumButton("Right Sleeve: " + (rightSleeve ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) + 8, (DisplayManager.getDefaultHeight() / 2) + 8), new IOnClickListener() {
            @Override
            public void onClick() {
                rightSleeve = !rightSleeve;
                rightSleeveButton.setName("Right Sleeve: " + (rightSleeve ? "ON" : "OFF"));
            }
        });

        leftPantsLegButton = new MediumButton("Left Pants Leg: " + (leftPantsLeg ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) - 308, (DisplayManager.getDefaultHeight() / 2) + 56), new IOnClickListener() {
            @Override
            public void onClick() {
                leftPantsLeg = !leftPantsLeg;
                leftPantsLegButton.setName("Left Pants Leg: " + (leftPantsLeg ? "ON" : "OFF"));
            }
        });

        rightPantsLegButton = new MediumButton("Right Pants Leg: " + (rightPantsLeg ? "ON" : "OFF"), new Vector2f((DisplayManager.getDefaultWidth() / 2) + 8, (DisplayManager.getDefaultHeight() / 2) + 56), new IOnClickListener() {
            @Override
            public void onClick() {
                rightPantsLeg = !rightPantsLeg;
                rightPantsLegButton.setName("Right Pants Leg: " + (rightPantsLeg ? "ON" : "OFF"));
            }
        });

        doneButton = new LargeButton("Done", new Vector2f((DisplayManager.getDefaultWidth() / 2) - 200, DisplayManager.getDefaultHeight() - 20), new IOnClickListener() {
            @Override
            public void onClick() {
                Settings.singleton.setShowHat(hat);
                Settings.singleton.setShowJacket(jacket);
                Settings.singleton.setShowRightSleeve(rightSleeve);
                Settings.singleton.setShowLeftSleeve(leftSleeve);
                Settings.singleton.setShowLeftPantsLeg(leftPantsLeg);
                Settings.singleton.setShowRightPantsLeg(rightPantsLeg);
                Settings.singleton.saveSettings();

                PlayerGameObject.thePlayer.setSkinCustomization(hat, jacket, leftSleeve, rightSleeve, leftPantsLeg, rightPantsLeg);
                MenuManager.setMenuScreen(new OptionsMenuScreen());
            }
        });

        label = new GUIText("Skin Customization", 1.5f, TextMaster.minecraftFont, new Vector2f(0, 40), DisplayManager.getDefaultWidth(), true, true);
    }

    public void update() {
        hatButton.update();
        jacketButton.update();
        rightSleeveButton.update();
        leftSleeveButton.update();
        doneButton.update();
        leftPantsLegButton.update();
        rightPantsLegButton.update();
    }

    public void render(Renderer renderer) {
        GUIShader.singleton.start();
        GUIShader.singleton.loadViewMatrix(Camera.singleton);
        renderer.prepareGUI();
        hatButton.render(renderer, GUIShader.singleton);
        jacketButton.render(renderer, GUIShader.singleton);
        rightSleeveButton.render(renderer, GUIShader.singleton);
        doneButton.render(renderer, GUIShader.singleton);
        leftSleeveButton.render(renderer, GUIShader.singleton);
        leftPantsLegButton.render(renderer, GUIShader.singleton);
        rightPantsLegButton.render(renderer, GUIShader.singleton);
        GUIShader.singleton.stop();
    }

    public boolean showPlayer() {
        return false;
    }

    public void resize() {
        hatButton.resize();
        jacketButton.resize();
        rightSleeveButton.resize();
        doneButton.resize();
        leftSleeveButton.resize();
        leftPantsLegButton.resize();
        rightPantsLegButton.resize();
    }

    @Override
    public void cleanUp() {
        hatButton.cleanUp();
        jacketButton.cleanUp();
        rightSleeveButton.cleanUp();
        doneButton.cleanUp();
        leftSleeveButton.cleanUp();
        label.remove();
        leftPantsLegButton.cleanUp();
        rightPantsLegButton.cleanUp();
    }
}

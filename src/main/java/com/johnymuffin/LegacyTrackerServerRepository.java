package com.johnymuffin;

import gg.codie.mineonline.api.SavedMinecraftServer;
import org.json.JSONObject;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class LegacyTrackerServerRepository {
    private LinkedList<LegacyTrackerServer> servers;
    private boolean failed = false;

    public LinkedList<LegacyTrackerServer> getServers() {
        return servers;
    }

    public boolean didFail() {
        return failed;
    }

    public boolean gotServers() {
        return servers != null;
    }

    public void loadServers() {
        servers = null;
        failed = false;

        CompletableFuture.runAsync(() -> {
            try {
                servers = getServerList();
            } catch (Exception ex) {
                ex.printStackTrace();
                servers = new LinkedList<LegacyTrackerServer>();

                String serverIcon = null;
                try {
                    InputStream iconStream = LegacyTrackerServerRepository.class.getClassLoader().getResourceAsStream("textures/mineonline/gui/server-icon.png");
                    byte[] data = new byte[iconStream.available()];
                    iconStream.read(data);

                    serverIcon = Base64.getEncoder().encodeToString(data);
                } catch (Exception ex2) {
                    ex.printStackTrace();
                }

                servers.push(new LegacyTrackerServer(
                        "",
                        "mc.craftycodie.com",
                        "mc.craftycodie.com",
                        25565,
                        0,
                        64,
                        "Ampersand SMP &",
                        "b1.2_02",
                        true,
                        null,
                        "An Early-Beta, Vanilla SMP Experience!",
                        false,
                        false,
                        false,
                        serverIcon,
                        false
                ));
                failed = true;
            }
            for(GotServersListener listener : listeners) {
                listener.GotServers(servers);
            }
        });
    }

    public interface GotServersListener {
        void GotServers(LinkedList<LegacyTrackerServer> servers);
    }

    private LinkedList<GotServersListener> listeners = new LinkedList<>();

    public void onGotServers(GotServersListener listener) {
        listeners.add(listener);
    }

    public LegacyTrackerServer getServer(String serverIP, String port) throws IOException {
        HttpURLConnection connection;

        URL url = new URL("http://servers.api.legacyminecraft.com/api/v1/getServer?serverip=" + serverIP + "&port=" + port);
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        JSONObject jsonResponse = new JSONObject(response.toString());

        if (connection != null)
            connection.disconnect();

        return LegacyTrackerServer.parseServer(jsonResponse);
    }

    public void offGotServers(GotServersListener listener) {
        listeners.remove(listener);
    }

    public static LinkedList<LegacyTrackerServer> getServerList() throws IOException {
        HttpURLConnection connection;

        URL url = new URL("http://servers.api.legacyminecraft.com/api/v1/getServers?type=all&icons=true");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        JSONObject jsonResponse = new JSONObject(response.toString());

        if (connection != null)
            connection.disconnect();

        return LegacyTrackerServer.parseServers(jsonResponse.getJSONArray("servers"));
    }
}

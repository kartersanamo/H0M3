package com.sanamo.h0M3.managers;

import com.sanamo.h0M3.api.chat.ColorUtil;
import com.sanamo.h0M3.api.config.ConfigFile;
import com.sanamo.h0M3.api.util.ConfigUtil;
import com.sanamo.h0M3.api.util.LocationUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.api.util.TimeUtil;
import com.sanamo.h0M3.models.Home;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class HomeManager {

    public static final int MAX_NUM_HOMES = 9;
    private final Map<UUID, Map<String, Home>> homes;// Maps UUID -> Map of ID -> Home
    private final Plugin plugin;

    public HomeManager(Plugin plugin) {
        this.plugin = plugin;
        this.homes = new HashMap<>();
    }

    private ConfigFile getPlayerFile(UUID uuid) {
        return new ConfigFile(plugin, "homes/" + uuid + ".yml");
    }

    public void loadHomes(UUID uuid) {
        ConfigFile file = getPlayerFile(uuid);
        ConfigurationSection section = file.getConfig().getConfigurationSection("homes");

        if (section == null) {
            homes.put(uuid, new HashMap<>());
            return;
        }

        Map<String, Home> playerHomes = new HashMap<>();

        for (String homeId : section.getKeys(false)) {
            ConfigurationSection homeSection = section.getConfigurationSection(homeId);
            if (homeSection == null) continue;

            Location location = homeSection.getLocation("location");
            if (location == null) continue;

            String displayName = homeSection.getString("display-name", homeId);

            Material material;
            try {
                material = Material.valueOf(
                        homeSection.getString("material", "CHEST").toUpperCase()
                );
            } catch (IllegalArgumentException e) {
                material = Material.CHEST;
            }

            List<String> lore = homeSection.getStringList("lore");
            Player owner = plugin.getServer().getPlayer(uuid);
            if (owner == null) {
                continue;
            }

            long createdAt = homeSection.getLong("created-at");
            long lastUsedAt = homeSection.getLong("last-used-at");

            Home home = new Home(
                    homeId,
                    owner,
                    displayName,
                    lore,
                    material,
                    location,
                    createdAt,
                    lastUsedAt
            );

            playerHomes.put(homeId, home);
        }

        homes.put(uuid, playerHomes);
    }

    public void unloadHomes(UUID uuid) {
        homes.remove(uuid);
    }

    private void saveHomes(UUID uuid) {
        ConfigFile file = getPlayerFile(uuid);
        file.getConfig().set("homes", null);

        Map<String, Home> playerHomes = homes.get(uuid);
        if (playerHomes == null) return;

        for (Home home : playerHomes.values()) {
            String path = "homes." + home.getId();

            file.getConfig().set(path + ".display-name", home.getDisplayName());
            file.getConfig().set(path + ".material", home.getMaterial().name());
            file.getConfig().set(path + ".lore", home.getLore());
            file.getConfig().set(path + ".location", home.getLocation());
            file.getConfig().set(path + ".created-at", home.getCreatedAt());
            file.getConfig().set(path + ".last-used-at", home.getLastUsedAt());
        }

        file.save();
    }

    public void addHome(UUID uuid, Home home) {
        homes.computeIfAbsent(uuid, k -> new HashMap<>())
                .put(home.getId(), home);

        saveHomes(uuid);
    }

    private ConfigurationSection getSection(UUID uuid) {
        ConfigFile cf = getPlayerFile(uuid);

        return cf.getConfig().getConfigurationSection("homes");
    }

    public Map<String, String> getHomeNameToId(UUID uuid) {
        Map<String, String> homeNametoId = new HashMap<>();
        ConfigurationSection section = getSection(uuid);
        if (section == null) {
            return null;
        }

        for (String id : section.getKeys(false)) {
            String name = (String) section.get(id + ".display-name");
            if (name != null) {
                homeNametoId.put(name, id);
            }
        }
        return homeNametoId;
    }

    public boolean exists(UUID uuid, String name) {
        return getHome(uuid, name) != null;
    }

    public Map<String, Home> getHomes(UUID uuid) {
        return homes.get(uuid);
    }

    public Home getHome(UUID uuid, String name) {
        Map<String, String> nameToId = getNameToId(uuid);
        if (nameToId == null) {
            return null;
        }
        String id = nameToId.get(name);

        Map<String, Home> playerHomes = homes.get(uuid);
        if (playerHomes == null) {
            return null;
        }

        return homes.get(uuid).get(id);
    }

    private Map<String, String> getNameToId(UUID uuid) {
        return getHomeNameToId(uuid);
    }

    public boolean isHomeNameCorrectSize(String newName) {
        return (newName.length() < ConfigUtil.homeNameMinLength
                || newName.length() > ConfigUtil.homeNameMaxLength);
    }

    private String getIdByName(UUID uniqueID, String homeName) {
        ConfigurationSection section = getSection(uniqueID);
        if (section == null) return null; // No homes for this player

        for (String id : section.getKeys(false)) {
            String name = section.getString(id + ".display-name");
            if (name != null && name.equalsIgnoreCase(homeName)) {
                return id;
            }
        }

        return null; // Not found
    }

    public void deleteHome(UUID uniqueId, String homeName) {
        Map<String, Home> playerHomes = homes.get(uniqueId);
        if (playerHomes == null) {
            return; // No homes for this player
        }

        String id = getIdByName(uniqueId, homeName);
        if (id == null || !playerHomes.containsKey(id)) {
            return; // Home not found
        }

        playerHomes.remove(id);
        saveHomes(uniqueId);
    }

    public int getHomeCount(UUID uuid) {
        Map<String, Home> playerHomes = homes.get(uuid);
        return playerHomes != null ? playerHomes.size() : 0;
    }

    public void update(Home home) {
        String normalizedId = home.getId().toLowerCase().trim();
        UUID uuid = home.getOwner().getUniqueId();

        homes
                .computeIfAbsent(uuid, k -> new HashMap<>())
                .put(normalizedId, home);

        saveHomes(uuid);
    }

    public boolean homeNameHasColor(String homeName) {
        return !ChatColor.stripColor(
                ChatColor.translateAlternateColorCodes('&', homeName)
        ).equals(homeName);
    }

    public void sendInfo(Home home, Player player) {
        player.sendMessage(ColorUtil.translate(MessagesUtil.homeInfoDivider));
        player.sendMessage(ColorUtil.translate(MessagesUtil.homeInfoTitle));
        player.sendMessage(ColorUtil.translate(MessagesUtil.homeInfoSpacer));
        List<String> lines = getInformationLines(home);
        for (String line : lines) {
            player.sendMessage(ColorUtil.translate(line));
        }
        player.sendMessage(ColorUtil.translate(MessagesUtil.homeInfoDivider));
    }

    public List<String> getInformationLines(Home home) {
        List<String> lines = new ArrayList<>();

        lines.add(ColorUtil.translate(
                PlaceholderUtil.replace(MessagesUtil.homeInfoName, "%name%", home.getDisplayName())
        ));
        lines.add(ColorUtil.translate(
                PlaceholderUtil.replace(MessagesUtil.homeInfoCreated, "%time%", TimeUtil.formatUnix(home.getCreatedAt()))
        ));
        lines.add(ColorUtil.translate(
                PlaceholderUtil.replace(MessagesUtil.homeInfoLastUsed, "%time%", TimeUtil.formatUnix(home.getLastUsedAt()))
        ));
        lines.add(ColorUtil.translate(
                PlaceholderUtil.replace(MessagesUtil.homeInfoMaterial, "%material%", home.getMaterial().name())
        ));
        lines.add(ColorUtil.translate(
                PlaceholderUtil.replace(MessagesUtil.homeInfoLocation, "%location%", LocationUtil.format(home.getLocation()))
        ));
        if (home.getLore().isEmpty()) {
            lines.add(ColorUtil.translate(MessagesUtil.homeInfoLoreNone));
        } else {
            lines.add(ColorUtil.translate(MessagesUtil.homeInfoLoreLabel));
            for (String loreLine : home.getLore()) {
                lines.add(ColorUtil.translate(
                        PlaceholderUtil.replace(MessagesUtil.homeInfoLoreEntry, "%line%", loreLine)
                ));
            }
        }

        return lines;
    }
}

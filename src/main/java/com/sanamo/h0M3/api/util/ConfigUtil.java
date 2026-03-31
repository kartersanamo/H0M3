package com.sanamo.h0M3.api.util;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {
    public static int maxNumberOfHomes;
    public static int homeNameMinLength;
    public static int homeNameMaxLength;
    public static Material defaultHomeMaterial;

    public static int manageHomeGuiSize;
    public static int homesGuiMinSize;
    public static int homesGuiMaxRows;

    public static boolean teleportDelayEnabled;
    public static int teleportDelaySeconds;
    public static Particle teleportDelayParticle;
    public static int teleportDelayParticleCount;
    public static double teleportDelayParticleRadius;
    public static Sound teleportDelaySound;
    public static float teleportDelaySoundVolume;
    public static float teleportDelaySoundPitch;

    public static Particle teleportCompleteParticle;
    public static int teleportCompleteParticleCount;
    public static double teleportCompleteParticleRadius;
    public static Sound teleportCompleteSound;
    public static float teleportCompleteSoundVolume;
    public static float teleportCompleteSoundPitch;

    public static Sound setHomeSound;
    public static float setHomeSoundVolume;
    public static float setHomeSoundPitch;
    public static Particle setHomeParticle;
    public static int setHomeParticleCount;
    public static double setHomeParticleRadius;
    public static Sound deleteHomeSound;
    public static float deleteHomeSoundVolume;
    public static float deleteHomeSoundPitch;
    public static Particle deleteHomeParticle;
    public static int deleteHomeParticleCount;
    public static double deleteHomeParticleRadius;
    public static Sound renameHomeSound;
    public static float renameHomeSoundVolume;
    public static float renameHomeSoundPitch;
    public static Particle renameHomeParticle;
    public static int renameHomeParticleCount;
    public static double renameHomeParticleRadius;
    public static Sound moveHomeSound;
    public static float moveHomeSoundVolume;
    public static float moveHomeSoundPitch;
    public static Particle moveHomeParticle;
    public static int moveHomeParticleCount;
    public static double moveHomeParticleRadius;
    public static Sound editMaterialSound;
    public static float editMaterialSoundVolume;
    public static float editMaterialSoundPitch;
    public static Particle editMaterialParticle;
    public static int editMaterialParticleCount;
    public static double editMaterialParticleRadius;
    public static Sound editLoreSound;
    public static float editLoreSoundVolume;
    public static float editLoreSoundPitch;
    public static Particle editLoreParticle;
    public static int editLoreParticleCount;
    public static double editLoreParticleRadius;
    public static Sound editLocationSound;
    public static float editLocationSoundVolume;
    public static float editLocationSoundPitch;
    public static Particle editLocationParticle;
    public static int editLocationParticleCount;
    public static double editLocationParticleRadius;

    public static void load(FileConfiguration cfg) {
        maxNumberOfHomes = cfg.getInt("home.max-number", 10);
        homeNameMinLength = cfg.getInt("home.name.min-length", 3);
        homeNameMaxLength = cfg.getInt("home.name.max-length", 16);
        defaultHomeMaterial = parseMaterial(cfg.getString("home.default-material", "CHEST"), Material.CHEST);

        manageHomeGuiSize = normalizeGuiSize(cfg.getInt("gui.manage-home.size", 27), 27);
        homesGuiMinSize = normalizeGuiSize(cfg.getInt("gui.homes.min-size", 9), 9);
        homesGuiMaxRows = clamp(cfg.getInt("gui.homes.max-rows", 6), 1, 6);

        teleportDelayEnabled = cfg.getBoolean("teleport.delay.enabled", false);
        teleportDelaySeconds = cfg.getInt("teleport.delay.seconds", 3);
        teleportDelayParticle = parseParticle(cfg.getString("teleport.delay.particle", "DRIPPING_LAVA"));
        teleportDelayParticleCount = cfg.getInt("teleport.delay.particle-count", 40);
        teleportDelayParticleRadius = cfg.getDouble("teleport.delay.particle-radius", 1.0);
        teleportDelaySound = parseSound(cfg.getString("teleport.delay.sound", "BLOCK_NOTE_BLOCK_BELL"));
        teleportDelaySoundVolume = (float) cfg.getDouble("teleport.delay.sound-volume", 1.0);
        teleportDelaySoundPitch = (float) cfg.getDouble("teleport.delay.sound-pitch", 1.0);

        teleportCompleteParticle = parseParticle(cfg.getString("teleport.complete.particle", "HAPPY_VILLAGER"));
        teleportCompleteParticleCount = cfg.getInt("teleport.complete.particle-count", 40);
        teleportCompleteParticleRadius = cfg.getDouble("teleport.complete.particle-radius", 1.0);
        teleportCompleteSound = parseSound(cfg.getString("teleport.complete.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        teleportCompleteSoundVolume = (float) cfg.getDouble("teleport.complete.sound-volume", 1.0);
        teleportCompleteSoundPitch = (float) cfg.getDouble("teleport.complete.sound-pitch", 1.0);

        setHomeSound = parseSound(cfg.getString("effects.set-home.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        setHomeSoundVolume = (float) cfg.getDouble("effects.set-home.sound-volume", 1.0);
        setHomeSoundPitch = (float) cfg.getDouble("effects.set-home.sound-pitch", 1.0);
        setHomeParticle = parseParticle(cfg.getString("effects.set-home.particle", "NONE"));
        setHomeParticleCount = cfg.getInt("effects.set-home.particle-count", 40);
        setHomeParticleRadius = cfg.getDouble("effects.set-home.particle-radius", 1.0);

        deleteHomeSound = parseSound(cfg.getString("effects.delete-home.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        deleteHomeSoundVolume = (float) cfg.getDouble("effects.delete-home.sound-volume", 1.0);
        deleteHomeSoundPitch = (float) cfg.getDouble("effects.delete-home.sound-pitch", 1.0);
        deleteHomeParticle = parseParticle(cfg.getString("effects.delete-home.particle", "NONE"));
        deleteHomeParticleCount = cfg.getInt("effects.delete-home.particle-count", 40);
        deleteHomeParticleRadius = cfg.getDouble("effects.delete-home.particle-radius", 1.0);

        renameHomeSound = parseSound(cfg.getString("effects.rename-home.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        renameHomeSoundVolume = (float) cfg.getDouble("effects.rename-home.sound-volume", 1.0);
        renameHomeSoundPitch = (float) cfg.getDouble("effects.rename-home.sound-pitch", 1.0);
        renameHomeParticle = parseParticle(cfg.getString("effects.rename-home.particle", "NONE"));
        renameHomeParticleCount = cfg.getInt("effects.rename-home.particle-count", 40);
        renameHomeParticleRadius = cfg.getDouble("effects.rename-home.particle-radius", 1.0);

        moveHomeSound = parseSound(cfg.getString("effects.move-home.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        moveHomeSoundVolume = (float) cfg.getDouble("effects.move-home.sound-volume", 1.0);
        moveHomeSoundPitch = (float) cfg.getDouble("effects.move-home.sound-pitch", 1.0);
        moveHomeParticle = parseParticle(cfg.getString("effects.move-home.particle", "NONE"));
        moveHomeParticleCount = cfg.getInt("effects.move-home.particle-count", 40);
        moveHomeParticleRadius = cfg.getDouble("effects.move-home.particle-radius", 1.0);

        editMaterialSound = parseSound(cfg.getString("effects.edit-material.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        editMaterialSoundVolume = (float) cfg.getDouble("effects.edit-material.sound-volume", 1.0);
        editMaterialSoundPitch = (float) cfg.getDouble("effects.edit-material.sound-pitch", 1.0);
        editMaterialParticle = parseParticle(cfg.getString("effects.edit-material.particle", "NONE"));
        editMaterialParticleCount = cfg.getInt("effects.edit-material.particle-count", 40);
        editMaterialParticleRadius = cfg.getDouble("effects.edit-material.particle-radius", 1.0);

        editLoreSound = parseSound(cfg.getString("effects.edit-lore.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        editLoreSoundVolume = (float) cfg.getDouble("effects.edit-lore.sound-volume", 1.0);
        editLoreSoundPitch = (float) cfg.getDouble("effects.edit-lore.sound-pitch", 1.0);
        editLoreParticle = parseParticle(cfg.getString("effects.edit-lore.particle", "NONE"));
        editLoreParticleCount = cfg.getInt("effects.edit-lore.particle-count", 40);
        editLoreParticleRadius = cfg.getDouble("effects.edit-lore.particle-radius", 1.0);

        editLocationSound = parseSound(cfg.getString("effects.edit-location.sound", "ENTITY_EXPERIENCE_ORB_PICKUP"));
        editLocationSoundVolume = (float) cfg.getDouble("effects.edit-location.sound-volume", 1.0);
        editLocationSoundPitch = (float) cfg.getDouble("effects.edit-location.sound-pitch", 1.0);
        editLocationParticle = parseParticle(cfg.getString("effects.edit-location.particle", "NONE"));
        editLocationParticleCount = cfg.getInt("effects.edit-location.particle-count", 40);
        editLocationParticleRadius = cfg.getDouble("effects.edit-location.particle-radius", 1.0);
    }

    private static int normalizeGuiSize(int size, int fallback) {
        if (size < 9 || size > 54 || size % 9 != 0) {
            return fallback;
        }
        return size;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static Sound parseSound(String value) {
        if (value == null || value.equalsIgnoreCase("none")) {
            return null;
        }
        try {
            return Sound.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Particle parseParticle(String value) {
        if (value == null || value.equalsIgnoreCase("none")) {
            return null;
        }
        try {
            return Particle.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Material parseMaterial(String value, Material fallback) {
        if (value == null) {
            return fallback;
        }
        Material material = Material.matchMaterial(value);
        return material != null ? material : fallback;
    }
}

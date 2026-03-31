package com.sanamo.h0M3.api.util;

import org.bukkit.configuration.file.FileConfiguration;

public class MessagesUtil {
    public static String commandUsage;
    public static String commandPlayerOnly;
    public static String commandNoPermission;
    public static String commandError;
    public static String subCommandNoPermission;
    public static String messagesReloaded;
    public static String blankLine;
    public static String chatPrefixDefault;
    public static String chatPrefixSuccess;
    public static String chatPrefixWarning;

    public static String homeNameSize;
    public static String homeNameColorCodes;
    public static String homeLimitReached;
    public static String homeSet;
    public static String homeNotFoundName;
    public static String homeGetFailed;
    public static String homeDeleted;
    public static String homeLocationUpdated;
    public static String homeNameChanged;
    public static String homeNameInvalid;
    public static String homeNameExists;
    public static String homeResultNull;
    public static String homeMaterialInvalid;
    public static String homeLoreEmpty;
    public static String homeLoreMinLines;
    public static String homeLoreUpdated;
    public static String homeMaterialUpdated;

    public static String manageHomeTitle;
    public static String manageHomeErrorName;
    public static String manageHomeErrorLine1;
    public static String manageHomeErrorLine2;
    public static String manageHomeErrorLine3;
    public static String manageHomeErrorLine4;
    public static String manageHomeChangeNameTitle;
    public static String manageHomeChangeNameLine1;
    public static String manageHomeChangeNameLine2;
    public static String manageHomeChangeMaterialTitle;
    public static String manageHomeChangeMaterialLine1;
    public static String manageHomeChangeMaterialLine2;
    public static String manageHomeChangeLoreTitle;
    public static String manageHomeChangeLoreLine1;
    public static String manageHomeChangeLoreLine2;
    public static String manageHomeChangeLocationTitle;
    public static String manageHomeChangeLocationLine1;
    public static String manageHomeChangeLocationLine2;
    public static String manageHomeDeleteTitle;
    public static String manageHomeDeleteLine1;
    public static String manageHomeDeleteLine2;
    public static String manageHomeDeleteLine3;
    public static String manageHomeDeleteLine4;
    public static String manageHomeBackTitle;
    public static String manageHomeBackLine1;
    public static String manageHomeBackLine2;
    public static String manageHomeInfoTitle;
    public static String manageHomeCurrent;
    public static String manageHomeCurrentLabel;
    public static String manageHomeLoreNone;
    public static String manageHomeLoreEntry;

    public static String promptChangeName;
    public static String promptChangeMaterial;
    public static String promptChangeLore;
    public static String promptDeleteHome;
    public static String promptDeleteConfirmValue;

    public static String nameInputCancelled;
    public static String materialInputCancelled;
    public static String loreInputCancelled;
    public static String homeDeletionCancelled;

    public static String homesGuiTitle;
    public static String homesGuiEmptyName;
    public static String homesGuiEmptyLore;
    public static String homesGuiInstructionTeleport;
    public static String homesGuiInstructionManage;
    public static String homesGuiHomeName;

    public static String homeInfoDivider;
    public static String homeInfoTitle;
    public static String homeInfoSpacer;
    public static String homeInfoName;
    public static String homeInfoCreated;
    public static String homeInfoLastUsed;
    public static String homeInfoMaterial;
    public static String homeInfoLocation;
    public static String homeInfoLoreNone;
    public static String homeInfoLoreLabel;
    public static String homeInfoLoreEntry;

    public static String teleportCountdown;
    public static String teleportSuccessLocation;
    public static String teleportSuccessPlayer;
    public static String teleportCancelledMoved;

    public static String chatInputCancelHint;
    public static String chatInputCancelKeyword;

    public static void load(FileConfiguration cfg) {
        commandUsage = cfg.getString("command-usage", "Usage: %usage%");
        commandPlayerOnly = cfg.getString("command-player-only", "This command can only be executed by players.");
        commandNoPermission = cfg.getString("command-no-permission", "You do not have permission to use this command.");
        commandError = cfg.getString("command-error", "An error occurred while executing this command.");
        subCommandNoPermission = cfg.getString("subcommand-no-permission", "You do not have permission to use this sub-command.");
        messagesReloaded = cfg.getString("messages-reloaded", "Config and messages reloaded.");
        blankLine = cfg.getString("blank-line", " ");
        chatPrefixDefault = cfg.getString("chat-prefix-default", "&8&l[&4&lH0M3&8&l]");
        chatPrefixSuccess = cfg.getString("chat-prefix-success", "&8&l[&a&lH0M3&8&l]");
        chatPrefixWarning = cfg.getString("chat-prefix-warning", "&8&l[&e&lH0M3&8&l]");

        homeNameSize = cfg.getString("home-name-size", "Please enter a home name between %min% and %max% characters");
        homeNameColorCodes = cfg.getString("home-name-color-codes", "Home names cannot contain color codes");
        homeLimitReached = cfg.getString("home-limit-reached", "You have reached the maximum number of homes (%max% homes)");
        homeSet = cfg.getString("home-set", "Set your home %name% at %location%");
        homeNotFoundName = cfg.getString("home-not-found-name", "You do not have a home by the name of %name%");
        homeGetFailed = cfg.getString("home-get-failed", "I failed to get the home by the name of %name%");
        homeDeleted = cfg.getString("home-deleted", "Your home %name% has been deleted");
        homeLocationUpdated = cfg.getString("home-location-updated", "Successfully updated your home's location (%old% -> %new%)");
        homeNameChanged = cfg.getString("home-name-changed", "Successfully changed your home's name (%old% -> %new%)");
        homeNameInvalid = cfg.getString("home-name-invalid", "That is not a valid home name");
        homeNameExists = cfg.getString("home-name-exists", "A home with this name already exists");
        homeResultNull = cfg.getString("home-result-null", "Home result is null");
        homeMaterialInvalid = cfg.getString("home-material-invalid", "Failed to grab the material by that name");
        homeLoreEmpty = cfg.getString("home-lore-empty", "Lore cannot be empty");
        homeLoreMinLines = cfg.getString("home-lore-min-lines", "Lore must contain at least one line");
        homeLoreUpdated = cfg.getString("home-lore-updated", "Successfully updated your home's lore to %count% lines");
        homeMaterialUpdated = cfg.getString("home-material-updated", "Successfully updated your home's material (%old% -> %new%)");

        manageHomeTitle = cfg.getString("manage-home-title", "Home Manager");
        manageHomeErrorName = cfg.getString("manage-home-error-name", "&c&lERROR");
        manageHomeErrorLine1 = cfg.getString("manage-home-error-line-1", "&7Sorry, but this home is corrupted,");
        manageHomeErrorLine2 = cfg.getString("manage-home-error-line-2", "&7invalid, or could not be found.");
        manageHomeErrorLine3 = cfg.getString("manage-home-error-line-3", "&7Please try deleting the home with");
        manageHomeErrorLine4 = cfg.getString("manage-home-error-line-4", "&e/delhome &7and retry. Sorry!");
        manageHomeChangeNameTitle = cfg.getString("manage-home-change-name-title", "&6Change Name");
        manageHomeChangeNameLine1 = cfg.getString("manage-home-change-name-line-1", "&eLeft-Click &7here to change");
        manageHomeChangeNameLine2 = cfg.getString("manage-home-change-name-line-2", "&7the display name of your home.");
        manageHomeChangeMaterialTitle = cfg.getString("manage-home-change-material-title", "&6Change Material");
        manageHomeChangeMaterialLine1 = cfg.getString("manage-home-change-material-line-1", "&eLeft-Click &7here to change");
        manageHomeChangeMaterialLine2 = cfg.getString("manage-home-change-material-line-2", "&7the material of your home.");
        manageHomeChangeLoreTitle = cfg.getString("manage-home-change-lore-title", "&6Change Lore");
        manageHomeChangeLoreLine1 = cfg.getString("manage-home-change-lore-line-1", "&eLeft-Click &7here to change");
        manageHomeChangeLoreLine2 = cfg.getString("manage-home-change-lore-line-2", "&7the lore of your home.");
        manageHomeChangeLocationTitle = cfg.getString("manage-home-change-location-title", "&6Change Location");
        manageHomeChangeLocationLine1 = cfg.getString("manage-home-change-location-line-1", "&eLeft-Click &7here to change");
        manageHomeChangeLocationLine2 = cfg.getString("manage-home-change-location-line-2", "&7the location of your home.");
        manageHomeDeleteTitle = cfg.getString("manage-home-delete-title", "&6Delete Home");
        manageHomeDeleteLine1 = cfg.getString("manage-home-delete-line-1", "&eLeft-Click &7here to permanently");
        manageHomeDeleteLine2 = cfg.getString("manage-home-delete-line-2", "&7delete your home.");
        manageHomeDeleteLine3 = cfg.getString("manage-home-delete-line-3", "&7");
        manageHomeDeleteLine4 = cfg.getString("manage-home-delete-line-4", "&c&lWARNING: THIS IS IRREVERSIBLE!");
        manageHomeBackTitle = cfg.getString("manage-home-back-title", "&c&lGo Back");
        manageHomeBackLine1 = cfg.getString("manage-home-back-line-1", "&7Left-Click here to go back");
        manageHomeBackLine2 = cfg.getString("manage-home-back-line-2", "&7to the previous page");
        manageHomeInfoTitle = cfg.getString("manage-home-info-title", "&6Home Information");
        manageHomeCurrent = cfg.getString("manage-home-current", "&eCurrent: &7%value%");
        manageHomeCurrentLabel = cfg.getString("manage-home-current-label", "&eCurrent:");
        manageHomeLoreNone = cfg.getString("manage-home-lore-none", "&7None");
        manageHomeLoreEntry = cfg.getString("manage-home-lore-entry", "&8> &7%line%");

        promptChangeName = cfg.getString("prompt-change-name", "&ePlease type the new name for your home");
        promptChangeMaterial = cfg.getString("prompt-change-material", "&ePlease type the name of the new material for your home (EX: DIAMOND_BLOCK, GRASS, STONE, etc.)");
        promptChangeLore = cfg.getString("prompt-change-lore", "&ePlease enter the new lore for your home (Use | to separate lines)");
        promptDeleteHome = cfg.getString("prompt-delete-home", "&c&lWARNING: Are you sure you want to delete your home? This action is IRREVERSIBLE\n&7Type '&a%confirm%&7' to confirm");
        promptDeleteConfirmValue = cfg.getString("prompt-delete-confirm-value", "confirm");

        nameInputCancelled = cfg.getString("name-input-cancelled", "Name input cancelled");
        materialInputCancelled = cfg.getString("material-input-cancelled", "Material input cancelled");
        loreInputCancelled = cfg.getString("lore-input-cancelled", "Lore input cancelled");
        homeDeletionCancelled = cfg.getString("home-deletion-cancelled", "Home deletion cancelled");

        homesGuiTitle = cfg.getString("homes-gui-title", "Homes (%count%)");
        homesGuiEmptyName = cfg.getString("homes-gui-empty-name", "&cNo Homes");
        homesGuiEmptyLore = cfg.getString("homes-gui-empty-lore", "&7You have no homes created yet.");
        homesGuiInstructionTeleport = cfg.getString("homes-gui-instruction-teleport", "&eLeft-Click &7to teleport");
        homesGuiInstructionManage = cfg.getString("homes-gui-instruction-manage", "&eRight-Click &7to manage");
        homesGuiHomeName = cfg.getString("homes-gui-home-name", "&6%name%");

        homeInfoDivider = cfg.getString("home-info-divider", "&8&l&m----------------------------------------");
        homeInfoTitle = cfg.getString("home-info-title", "&4&lHome Information");
        homeInfoSpacer = cfg.getString("home-info-spacer", "&4&l");
        homeInfoName = cfg.getString("home-info-name", "&7Name - %name%");
        homeInfoCreated = cfg.getString("home-info-created", "&7Created At - %time%");
        homeInfoLastUsed = cfg.getString("home-info-last-used", "&7Last Used - %time%");
        homeInfoMaterial = cfg.getString("home-info-material", "&7Material - %material%");
        homeInfoLocation = cfg.getString("home-info-location", "&7Location - %location%");
        homeInfoLoreNone = cfg.getString("home-info-lore-none", "&7Lore - None");
        homeInfoLoreLabel = cfg.getString("home-info-lore-label", "&7Lore - ");
        homeInfoLoreEntry = cfg.getString("home-info-lore-entry", "&8> &7%line%");

        teleportCountdown = cfg.getString("teleport-countdown", "You will be teleported in %seconds%...");
        teleportSuccessLocation = cfg.getString("teleport-success-location", "You have been teleported to %location%");
        teleportSuccessPlayer = cfg.getString("teleport-success-player", "You have been teleported to %player%");
        teleportCancelledMoved = cfg.getString("teleport-cancelled-moved", "You moved! Cancelling your teleportation request.");

        chatInputCancelHint = cfg.getString("chatinput-cancel-hint", "&7Type '&c%keyword%&7' to cancel");
        chatInputCancelKeyword = cfg.getString("chatinput-cancel-keyword", "cancel");
    }
}

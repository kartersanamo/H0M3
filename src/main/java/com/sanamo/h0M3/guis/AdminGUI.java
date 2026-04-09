package com.sanamo.h0M3.guis;

import com.sanamo.h0M3.H0M3;
import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.chat.ColorUtil;
import com.sanamo.h0M3.api.gui.GUI;
import com.sanamo.h0M3.api.item.ItemBuilder;
import com.sanamo.h0M3.api.util.LocationUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AdminGUI extends GUI {

    private static final int PAGE_SIZE = 45;
    private static final int GUI_SIZE = 54;

    private final HomeManager homeManager;
    private final Player admin;
    private final ViewMode viewMode;
    private final UUID selectedTarget;
    private final String selectedHomeId;
    private final int page;

    public AdminGUI(HomeManager homeManager, Player admin) {
        this(homeManager, admin, ViewMode.PLAYER_LIST, null, null, 0);
    }

    public AdminGUI(HomeManager homeManager, Player admin, UUID targetId) {
        this(homeManager, admin, ViewMode.PLAYER_HOMES, targetId, null, 0);
    }

    private AdminGUI(HomeManager homeManager, Player admin, ViewMode viewMode, UUID selectedTarget, String selectedHomeId, int page) {
        super(
                "admin_gui_" + viewMode.name().toLowerCase(),
                buildTitle(viewMode, selectedTarget, selectedHomeId, homeManager),
                GUI_SIZE
        );
        this.homeManager = homeManager;
        this.admin = admin;
        this.viewMode = viewMode;
        this.selectedTarget = selectedTarget;
        this.selectedHomeId = selectedHomeId;
        this.page = Math.max(page, 0);

        build();
    }

    private static String buildTitle(ViewMode viewMode, UUID selectedTarget, String selectedHomeId, HomeManager homeManager) {
        if (viewMode == ViewMode.PLAYER_HOMES && selectedTarget != null) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(selectedTarget);
            String targetName = target.getName() != null ? target.getName() : selectedTarget.toString();
            return ColorUtil.translate("&8Homes of &e" + targetName);
        }

        if (viewMode == ViewMode.MANAGE_HOME && selectedTarget != null && selectedHomeId != null) {
            Map<String, Home> homes = homeManager.getHomes(selectedTarget);
            String homeName = selectedHomeId;
            if (homes != null) {
                Home home = homes.get(selectedHomeId);
                if (home != null) {
                    homeName = home.getDisplayName();
                }
            }
            return ColorUtil.translate("&8Manage &6" + homeName);
        }

        return ColorUtil.translate("&8Admin Home Manager");
    }

    private void build() {
        if (viewMode == ViewMode.PLAYER_LIST) {
            buildPlayerListView();
            return;
        }

        if (viewMode == ViewMode.PLAYER_HOMES) {
            buildPlayerHomesView();
            return;
        }

        buildManageHomeView();
    }

    private void buildPlayerListView() {
        List<UUID> ownerIds = new ArrayList<>(homeManager.getKnownHomeOwners());
        ownerIds.sort(Comparator.comparing(this::getOwnerDisplayName, String.CASE_INSENSITIVE_ORDER));

        int maxPage = getMaxPage(ownerIds.size());
        int currentPage = Math.min(page, maxPage);
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, ownerIds.size());

        if (ownerIds.isEmpty()) {
            setItem(22, new ItemBuilder(Material.BARRIER)
                    .name(ColorUtil.translate("&cNo Player Data"))
                    .lore(ColorUtil.translate("&7No player home files were found."))
                    .build());
        }

        for (int index = start; index < end; index++) {
            UUID targetId = ownerIds.get(index);
            int slot = index - start;
            setItem(slot, createPlayerHeadItem(targetId));
            setClickHandler(slot, event -> openPlayerHomes(targetId, 0));
        }

        setPageControls(currentPage, maxPage,
                () -> openPlayerList(currentPage - 1),
                () -> openPlayerList(currentPage + 1));

        setItem(49, new ItemBuilder(Material.SUNFLOWER)
                .name(ColorUtil.translate("&eRefresh"))
                .lore(ColorUtil.translate("&7Rebuild player list"))
                .build());
        setClickHandler(49, event -> openPlayerList(currentPage));

        setItem(50, new ItemBuilder(Material.BARRIER)
                .name(ColorUtil.translate("&cClose"))
                .lore(ColorUtil.translate("&7Close this menu"))
                .build());
        setClickHandler(50, event -> admin.closeInventory());
    }

    private void buildPlayerHomesView() {
        OfflinePlayer target = selectedTarget != null ? Bukkit.getOfflinePlayer(selectedTarget) : null;

        List<Home> homes = new ArrayList<>();
        Map<String, Home> homeMap = homeManager.getHomes(target.getUniqueId());
        if (homeMap != null) {
            homes.addAll(homeMap.values());
            homes.sort(Comparator.comparing(Home::getDisplayName, String.CASE_INSENSITIVE_ORDER));
        }

        int maxPage = getMaxPage(homes.size());
        int currentPage = Math.min(page, maxPage);
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, homes.size());

        if (homes.isEmpty()) {
            setItem(22, new ItemBuilder(Material.BARRIER)
                    .name(ColorUtil.translate("&cNo Homes"))
                    .lore(ColorUtil.translate("&7" + getOwnerDisplayName(target) + " has no homes loaded."))
                    .build());
        }

        for (int index = start; index < end; index++) {
            Home home = homes.get(index);
            int slot = index - start;
            setItem(slot, createHomeListItem(home, target));
            setClickHandler(slot, event -> {
                switch (event.getClick()) {
                    case RIGHT, SHIFT_RIGHT -> openManageHome(target.getUniqueId(), home.getId(), currentPage);
                    case LEFT, SHIFT_LEFT -> teleportAdminToHome(home);
                    default -> {
                    }
                }
            });
        }

        setPageControls(currentPage, maxPage,
                () -> openPlayerHomes(target.getUniqueId(), currentPage - 1),
                () -> openPlayerHomes(target.getUniqueId(), currentPage + 1));

        setItem(48, new ItemBuilder(Material.ARROW)
                .name(ColorUtil.translate("&cBack"))
                .lore(ColorUtil.translate("&7Return to player list"))
                .build());
        setClickHandler(48, event -> openPlayerList(0));

        setItem(49, new ItemBuilder(Material.SUNFLOWER)
                .name(ColorUtil.translate("&eRefresh"))
                .lore(ColorUtil.translate("&7Reload this player's homes"))
                .build());
        setClickHandler(49, event -> openPlayerHomes(target.getUniqueId(), currentPage));

        setItem(50, new ItemBuilder(Material.BARRIER)
                .name(ColorUtil.translate("&cClose"))
                .lore(ColorUtil.translate("&7Close this menu"))
                .build());
        setClickHandler(50, event -> admin.closeInventory());
    }

    private void buildManageHomeView() {
        if (selectedTarget == null || selectedHomeId == null) {
            openPlayerList(0);
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(selectedTarget);
        Home home = getHomeById(selectedTarget, selectedHomeId);
        if (home == null) {
            setItem(22, new ItemBuilder(Material.BARRIER)
                    .name(ColorUtil.translate("&cHome Not Found"))
                    .lore(ColorUtil.translate("&7This home could not be loaded."))
                    .build());
            setItem(49, new ItemBuilder(Material.ARROW)
                    .name(ColorUtil.translate("&cBack"))
                    .lore(ColorUtil.translate("&7Return to player's homes"))
                    .build());
            setClickHandler(49, event -> openPlayerHomes(selectedTarget, 0));
            return;
        }

        setItem(4, new ItemBuilder(home.getMaterial())
                .name(ColorUtil.translate("&6" + home.getDisplayName()))
                .lore(ColorUtil.translate("&7Owner: &f" + getOwnerDisplayName(target)))
                .build());

        setItem(10, new ItemBuilder(Material.NAME_TAG)
                .name(ColorUtil.translate("&6Rename Home"))
                .lore(ColorUtil.translate("&eLeft-Click &7to rename this home"))
                .build());
        setClickHandler(10, event -> renameHome(home));

        setItem(11, new ItemBuilder(home.getMaterial())
                .name(ColorUtil.translate("&6Change Material"))
                .lore(ColorUtil.translate("&eLeft-Click &7to set a new icon material"))
                .build());
        setClickHandler(11, event -> changeHomeMaterial(home));

        setItem(12, new ItemBuilder(Material.PAPER)
                .name(ColorUtil.translate("&6Edit Lore"))
                .lore(ColorUtil.translate("&eLeft-Click &7to edit lore (use | separators)"))
                .build());
        setClickHandler(12, event -> changeHomeLore(home));

        setItem(13, new ItemBuilder(Material.RECOVERY_COMPASS)
                .name(ColorUtil.translate("&6Move Home Here"))
                .lore(ColorUtil.translate("&eLeft-Click &7to set home to your location"))
                .build());
        setClickHandler(13, event -> moveHomeToAdmin(home));

        setItem(14, new ItemBuilder(Material.ENDER_PEARL)
                .name(ColorUtil.translate("&6Teleport To Home"))
                .lore(ColorUtil.translate("&eLeft-Click &7to teleport yourself"))
                .build());
        setClickHandler(14, event -> teleportAdminToHome(home));

        setItem(15, new ItemBuilder(Material.BARRIER)
                .name(ColorUtil.translate("&cDelete Home"))
                .lore(ColorUtil.translate("&eLeft-Click &7to permanently delete this home"),
                        ColorUtil.translate("&cType confirmation in chat"))
                .build());
        setClickHandler(15, event -> deleteHome(home));

        setItem(16, new ItemBuilder(Material.BOOK)
                .name(ColorUtil.translate("&6Send Home Info"))
                .lore(ColorUtil.translate("&eLeft-Click &7to send details in chat"))
                .build());
        setClickHandler(16, event -> sendHomeInfo(home, target));

        setItem(18, new ItemBuilder(Material.ARROW)
                .name(ColorUtil.translate("&cBack"))
                .lore(ColorUtil.translate("&7Return to player's homes"))
                .build());
        setClickHandler(18, event -> openPlayerHomes(selectedTarget, page));

        setItem(26, new ItemBuilder(Material.BARRIER)
                .name(ColorUtil.translate("&cClose"))
                .lore(ColorUtil.translate("&7Close this menu"))
                .build());
        setClickHandler(26, event -> admin.closeInventory());
    }

    private ItemStack createPlayerHeadItem(UUID targetId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetId);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        if (meta != null) {
            meta.setOwningPlayer(target);
            if (target.isOnline()) {
                meta.setDisplayName(ColorUtil.translate("&e" + getOwnerDisplayName(target)));
            } else {
                meta.setDisplayName(ColorUtil.translate("&7" + getOwnerDisplayName(target)));
            }
            meta.setLore(List.of(
                    ColorUtil.translate("&7Homes: &f" + homeManager.getHomeCount(target.getUniqueId())),
                    ColorUtil.translate("&8"),
                    ColorUtil.translate("&eLeft-Click &7to manage homes")
            ));
            skull.setItemMeta(meta);
        }

        return skull;
    }

    private ItemStack createHomeListItem(Home home, OfflinePlayer target) {
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.translate("&7Owner: &f" + target.getName()));
        lore.add(ColorUtil.translate("&7Location: &f" + LocationUtil.format(home.getLocation())));
        lore.add(ColorUtil.translate("&8"));
        lore.add(ColorUtil.translate("&eLeft-Click &7Teleport"));
        lore.add(ColorUtil.translate("&eRight-Click &7Manage"));

        return new ItemBuilder(home.getMaterial())
                .name(ColorUtil.translate("&6" + home.getDisplayName()))
                .lore(lore)
                .build();
    }

    private void setPageControls(int currentPage, int maxPage, Runnable previousAction, Runnable nextAction) {
        if (currentPage > 0) {
            setItem(45, new ItemBuilder(Material.ARROW)
                    .name(ColorUtil.translate("&ePrevious Page"))
                    .lore(ColorUtil.translate("&7Go to page " + currentPage))
                    .build());
            setClickHandler(45, event -> previousAction.run());
        }

        setItem(47, new ItemBuilder(Material.PAPER)
                .name(ColorUtil.translate("&6Page &e" + (currentPage + 1) + "&6/&e" + (maxPage + 1)))
                .lore(ColorUtil.translate("&7Navigation controls"))
                .build());

        if (currentPage < maxPage) {
            setItem(53, new ItemBuilder(Material.ARROW)
                    .name(ColorUtil.translate("&eNext Page"))
                    .lore(ColorUtil.translate("&7Go to page " + (currentPage + 2)))
                    .build());
            setClickHandler(53, event -> nextAction.run());
        }
    }

    private void renameHome(Home home) {
        admin.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                admin,
                MessagesUtil.promptChangeName,
                input -> {
                    if (homeManager.isHomeNameCorrectSize(input)) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeNameInvalid)));
                    } else if (homeManager.exists(selectedTarget, input)) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeNameExists)));
                    } else {
                        String oldName = home.getDisplayName();
                        home.setDisplayName(input);
                        homeManager.update(home);
                        admin.sendMessage(ChatFormat.success(
                                PlaceholderUtil.replace(MessagesUtil.homeNameChanged, "%old%", oldName, "%new%", input)
                        ));
                    }
                    reopenCurrentHome();
                },
                () -> admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.nameInputCancelled)))
        );
    }

    private void changeHomeMaterial(Home home) {
        admin.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                admin,
                MessagesUtil.promptChangeMaterial,
                input -> {
                    Material newMaterial = Material.matchMaterial(input.toUpperCase());
                    if (newMaterial == null) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeMaterialInvalid)));
                    } else {
                        Material oldMaterial = home.getMaterial();
                        home.setMaterial(newMaterial);
                        homeManager.update(home);
                        admin.sendMessage(ChatFormat.success(
                                PlaceholderUtil.replace(
                                        MessagesUtil.homeMaterialUpdated,
                                        "%old%", oldMaterial.name(),
                                        "%new%", newMaterial.name()
                                )
                        ));
                    }
                    reopenCurrentHome();
                },
                () -> admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.materialInputCancelled)))
        );
    }

    private void changeHomeLore(Home home) {
        admin.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                admin,
                MessagesUtil.promptChangeLore,
                input -> {
                    if (input == null || input.trim().isEmpty()) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeLoreEmpty)));
                        reopenCurrentHome();
                        return;
                    }

                    List<String> lore = Arrays.stream(input.split("\\|"))
                            .map(String::trim)
                            .filter(line -> !line.isEmpty())
                            .toList();

                    if (lore.isEmpty()) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeLoreMinLines)));
                        reopenCurrentHome();
                        return;
                    }

                    home.setLore(lore);
                    homeManager.update(home);
                    admin.sendMessage(ChatFormat.success(
                            PlaceholderUtil.replace(MessagesUtil.homeLoreUpdated, "%count%", String.valueOf(lore.size()))
                    ));
                    reopenCurrentHome();
                },
                () -> admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.loreInputCancelled)))
        );
    }

    private void moveHomeToAdmin(Home home) {
        Location oldLocation = home.getLocation();
        home.setLocation(admin.getLocation());
        homeManager.update(home);
        admin.sendMessage(ChatFormat.success(
                PlaceholderUtil.replace(
                        MessagesUtil.homeLocationUpdated,
                        "%old%", LocationUtil.format(oldLocation),
                        "%new%", LocationUtil.format(admin.getLocation())
                )
        ));
        reopenCurrentHome();
    }

    private void deleteHome(Home home) {
        admin.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                admin,
                PlaceholderUtil.replace(MessagesUtil.promptDeleteHome, "%confirm%", MessagesUtil.promptDeleteConfirmValue),
                input -> {
                    if (!input.equalsIgnoreCase(MessagesUtil.promptDeleteConfirmValue)) {
                        admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeDeletionCancelled)));
                        reopenCurrentHome();
                        return;
                    }

                    String deletedName = home.getDisplayName();
                    homeManager.deleteHome(selectedTarget, deletedName);
                    admin.sendMessage(ChatFormat.success(
                            PlaceholderUtil.replace(MessagesUtil.homeDeleted, "%name%", deletedName)
                    ));
                    openPlayerHomes(selectedTarget, 0);
                },
                () -> admin.sendMessage(ChatFormat.error(PlaceholderUtil.replace(MessagesUtil.homeDeletionCancelled)))
        );
    }

    private void sendHomeInfo(Home home, OfflinePlayer target) {
        admin.sendMessage(ColorUtil.translate("&8&l&m----------------------------------------"));
        admin.sendMessage(ColorUtil.translate("&4&lAdmin Home Information"));
        admin.sendMessage(ColorUtil.translate("&7Owner: &f" + getOwnerDisplayName(target)));
        for (String line : homeManager.getInformationLines(home)) {
            admin.sendMessage(ColorUtil.translate(line));
        }
        admin.sendMessage(ColorUtil.translate("&8&l&m----------------------------------------"));
    }

    private void teleportAdminToHome(Home home) {
        admin.closeInventory();
        admin.teleport(home.getLocation());
        home.setLastUsedAt(System.currentTimeMillis());
        homeManager.update(home);
        admin.sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(MessagesUtil.teleportSuccessLocation, "%location%", LocationUtil.format(home.getLocation()))
        ));
    }

    private void reopenCurrentHome() {
        new AdminGUI(homeManager, admin, ViewMode.MANAGE_HOME, selectedTarget, selectedHomeId, page).open(admin);
    }

    private void openPlayerList(int targetPage) {
        new AdminGUI(homeManager, admin, ViewMode.PLAYER_LIST, null, null, targetPage).open(admin);
    }

    private void openPlayerHomes(UUID targetId, int targetPage) {
        new AdminGUI(homeManager, admin, ViewMode.PLAYER_HOMES, targetId, null, targetPage).open(admin);
    }

    private void openManageHome(UUID targetId, String homeId, int sourcePage) {
        new AdminGUI(homeManager, admin, ViewMode.MANAGE_HOME, targetId, homeId, sourcePage).open(admin);
    }

    private Home getHomeById(UUID owner, String homeId) {
        Map<String, Home> homes = homeManager.getHomes(owner);
        if (homes == null) {
            return null;
        }
        return homes.get(homeId);
    }

    private String getOwnerDisplayName(UUID ownerId) {
        return getOwnerDisplayName(Bukkit.getOfflinePlayer(ownerId));
    }

    private String getOwnerDisplayName(OfflinePlayer owner) {
        if (owner == null) {
            return "Unknown";
        }
        String name = owner.getName();
        return name != null ? name : owner.getUniqueId().toString();
    }

    private int getMaxPage(int totalEntries) {
        if (totalEntries <= 0) {
            return 0;
        }
        return (totalEntries - 1) / PAGE_SIZE;
    }

    private enum ViewMode {
        PLAYER_LIST,
        PLAYER_HOMES,
        MANAGE_HOME
    }
}

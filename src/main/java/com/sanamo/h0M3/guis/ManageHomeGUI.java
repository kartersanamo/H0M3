package com.sanamo.h0M3.guis;

import com.sanamo.h0M3.H0M3;
import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.chat.ColorUtil;
import com.sanamo.h0M3.api.gui.GUI;
import com.sanamo.h0M3.api.item.ItemBuilder;
import com.sanamo.h0M3.api.util.ConfigUtil;
import com.sanamo.h0M3.api.util.EffectUtil;
import com.sanamo.h0M3.api.util.LocationUtil;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;
import com.sanamo.h0M3.managers.HomeManager;
import com.sanamo.h0M3.models.Home;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ManageHomeGUI extends GUI {

    private final HomeManager homeManager;
    private final Home home;
    private final Player player;

    public ManageHomeGUI(HomeManager homeManager, Home home, Player player) {
        super("manage_home", ColorUtil.translate(MessagesUtil.manageHomeTitle), ConfigUtil.manageHomeGuiSize);
        this.homeManager = homeManager;
        this.home = home;
        this.player = player;
        build();
    }

    private void build() {

        // If home is null, show an error message
        if (home == null) {
            ItemStack nullItem = new ItemBuilder(Material.BARRIER)
                    .name(ColorUtil.translate(MessagesUtil.manageHomeErrorName))
                    .lore(List.of(
                            ColorUtil.translate(MessagesUtil.manageHomeErrorLine1),
                            ColorUtil.translate(MessagesUtil.manageHomeErrorLine2),
                            ColorUtil.translate(MessagesUtil.manageHomeErrorLine3),
                            ColorUtil.translate(MessagesUtil.manageHomeErrorLine4)
                    ))
                    .build();
            setItem(13, nullItem);
        }
        assert home != null;

        // Change name item
        ItemStack changeNameItem = new ItemBuilder(Material.NAME_TAG)
                .name(ColorUtil.translate(MessagesUtil.manageHomeChangeNameTitle))
                .lore(List.of(
                        ColorUtil.translate(MessagesUtil.manageHomeChangeNameLine1),
                        ColorUtil.translate(MessagesUtil.manageHomeChangeNameLine2),
                        ColorUtil.translate(MessagesUtil.blankLine),
                        Objects.requireNonNull(ColorUtil.translate(
                                PlaceholderUtil.replace(
                                        MessagesUtil.manageHomeCurrent,
                                        "%value%", home.getDisplayName()
                                )
                        ))
                ))
                .build();
        setItem(11, changeNameItem);
        setClickHandler(11, event -> changeName());

        // Change material item
        ItemStack changeMaterialItem = new ItemBuilder(home.getMaterial())
                .name(ColorUtil.translate(MessagesUtil.manageHomeChangeMaterialTitle))
                .lore(List.of(
                        ColorUtil.translate(MessagesUtil.manageHomeChangeMaterialLine1),
                        ColorUtil.translate(MessagesUtil.manageHomeChangeMaterialLine2),
                        ColorUtil.translate(MessagesUtil.blankLine),
                        Objects.requireNonNull(ColorUtil.translate(
                                PlaceholderUtil.replace(
                                        MessagesUtil.manageHomeCurrent,
                                        "%value%", home.getMaterial().toString()
                                )
                        ))
                ))
                .build();
        setItem(12, changeMaterialItem);
        setClickHandler(12, event -> changeMaterial());

        // Change lore item
        List<String> lore = new ArrayList<>();

        lore.add(ColorUtil.translate(MessagesUtil.manageHomeChangeLoreLine1));
        lore.add(ColorUtil.translate(MessagesUtil.manageHomeChangeLoreLine2));
        lore.add(ColorUtil.translate(MessagesUtil.blankLine));
        lore.add(ColorUtil.translate(MessagesUtil.manageHomeCurrentLabel));

        List<String> currentLore = home.getLore();

        if (currentLore == null || currentLore.isEmpty()) {
            lore.add(ColorUtil.translate(MessagesUtil.manageHomeLoreNone));
        } else {
            for (String line : currentLore) {
                lore.add(ColorUtil.translate(
                        PlaceholderUtil.replace(MessagesUtil.manageHomeLoreEntry, "%line%", line)
                ));
            }
        }

        ItemStack changeLoreItem = new ItemBuilder(Material.PAPER)
                .name(ColorUtil.translate(MessagesUtil.manageHomeChangeLoreTitle))
                .lore(lore)
                .build();

        setItem(13, changeLoreItem);
        setClickHandler(13, event -> changeLore());

        // Change location item
        ItemStack changeLocationItem = new ItemBuilder(Material.MAP)
                .name(ColorUtil.translate(MessagesUtil.manageHomeChangeLocationTitle))
                .lore(List.of(
                        ColorUtil.translate(MessagesUtil.manageHomeChangeLocationLine1),
                        ColorUtil.translate(MessagesUtil.manageHomeChangeLocationLine2),
                        ColorUtil.translate(MessagesUtil.blankLine),
                        Objects.requireNonNull(ColorUtil.translate(
                                PlaceholderUtil.replace(
                                        MessagesUtil.manageHomeCurrent,
                                        "%value%", LocationUtil.format(home.getLocation())
                                )
                        ))
                ))
                .build();
        setItem(14, changeLocationItem);
        setClickHandler(14, event -> changeLocation());

        // Delete home item
        ItemStack deleteHomeItem = new ItemBuilder(Material.BARRIER)
                .name(ColorUtil.translate(MessagesUtil.manageHomeDeleteTitle))
                .lore(List.of(
                        ColorUtil.translate(MessagesUtil.manageHomeDeleteLine1),
                        ColorUtil.translate(MessagesUtil.manageHomeDeleteLine2),
                        ColorUtil.translate(MessagesUtil.manageHomeDeleteLine3),
                        ColorUtil.translate(MessagesUtil.manageHomeDeleteLine4)
                ))
                .build();
        setItem(15, deleteHomeItem);
        setClickHandler(15, event -> deleteHome());

        // Back button item
        ItemStack backButtonItem = new ItemBuilder(Material.ARROW)
                .name(ColorUtil.translate(MessagesUtil.manageHomeBackTitle))
                .lore(List.of(
                        ColorUtil.translate(MessagesUtil.manageHomeBackLine1),
                        ColorUtil.translate(MessagesUtil.manageHomeBackLine2)
                ))
                .build();
        setItem(18, backButtonItem);
        setClickHandler(18, event -> backButton());

        // Home information
        List<String> lines = homeManager.getInformationLines(home);
        ItemStack homeInformationItem = new ItemBuilder(Material.BOOK)
                .name(ColorUtil.translate(MessagesUtil.manageHomeInfoTitle))
                .lore(lines)
                .build();
        setItem(22, homeInformationItem);
        setClickHandler(22, event -> homeInformation());
    }

    private void changeName() {
        player.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                player,
                MessagesUtil.promptChangeName,
                input -> {
                    String oldName = home.getDisplayName();
                    if (homeManager.isHomeNameCorrectSize(input)) {
                        player.sendMessage(ChatFormat.error(
                                PlaceholderUtil.replace(MessagesUtil.homeNameInvalid)
                        ));
                    } else {
                        this.home.setDisplayName(input);
                        homeManager.update(home);
                        player.sendMessage(ChatFormat.info(
                                PlaceholderUtil.replace(
                                        MessagesUtil.homeNameChanged,
                                        "%old%", oldName,
                                        "%new%", input
                                )
                        ));
                        EffectUtil.play(
                                player,
                                ConfigUtil.renameHomeSound,
                                ConfigUtil.renameHomeSoundVolume,
                                ConfigUtil.renameHomeSoundPitch,
                                ConfigUtil.renameHomeParticle,
                                ConfigUtil.renameHomeParticleCount,
                                ConfigUtil.renameHomeParticleRadius
                        );
                    }
                    ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
                    manageHomeGUI.open(player);
                },
                () -> player.sendMessage(ChatFormat.error(
                        PlaceholderUtil.replace(MessagesUtil.nameInputCancelled)
                ))
        );
    }

    private void changeMaterial() {
        player.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                player,
                MessagesUtil.promptChangeMaterial,
                input -> {
                    Material newMaterial = Material.getMaterial(input);
                    Material oldMaterial = home.getMaterial();
                    if (newMaterial == null) {
                        player.sendMessage(ChatFormat.error(
                                PlaceholderUtil.replace(MessagesUtil.homeMaterialInvalid)
                        ));
                    } else {
                        this.home.setMaterial(newMaterial);
                        homeManager.update(home);
                        player.sendMessage(ChatFormat.info(
                                PlaceholderUtil.replace(
                                        MessagesUtil.homeMaterialUpdated,
                                        "%old%", oldMaterial.name(),
                                        "%new%", newMaterial.name()
                                )
                        ));
                        EffectUtil.play(
                                player,
                                ConfigUtil.editMaterialSound,
                                ConfigUtil.editMaterialSoundVolume,
                                ConfigUtil.editMaterialSoundPitch,
                                ConfigUtil.editMaterialParticle,
                                ConfigUtil.editMaterialParticleCount,
                                ConfigUtil.editMaterialParticleRadius
                        );
                    }
                    ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
                    manageHomeGUI.open(player);
                },
                () -> player.sendMessage(ChatFormat.error(
                        PlaceholderUtil.replace(MessagesUtil.materialInputCancelled)
                ))
        );
    }

    private void changeLore() {
        player.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                player,
                MessagesUtil.promptChangeLore,
                input -> {

                    if (input == null || input.trim().isEmpty()) {
                        player.sendMessage(ChatFormat.error(
                                PlaceholderUtil.replace(MessagesUtil.homeLoreEmpty)
                        ));
                        return;
                    }

                    List<String> lore = Arrays.stream(input.split("\\|"))
                            .map(String::trim)
                            .filter(line -> !line.isEmpty())
                            .toList();

                    if (lore.isEmpty()) {
                        player.sendMessage(ChatFormat.error(
                                PlaceholderUtil.replace(MessagesUtil.homeLoreMinLines)
                        ));
                        return;
                    }

                    this.home.setLore(lore);
                    homeManager.update(home);
                    player.sendMessage(ChatFormat.info(
                            PlaceholderUtil.replace(
                                    MessagesUtil.homeLoreUpdated,
                                    "%count%", String.valueOf(lore.size())
                            )
                    ));
                    EffectUtil.play(
                            player,
                            ConfigUtil.editLoreSound,
                            ConfigUtil.editLoreSoundVolume,
                            ConfigUtil.editLoreSoundPitch,
                            ConfigUtil.editLoreParticle,
                            ConfigUtil.editLoreParticleCount,
                            ConfigUtil.editLoreParticleRadius
                    );

                    ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
                    manageHomeGUI.open(player);
                },
                () -> player.sendMessage(ChatFormat.error(
                        PlaceholderUtil.replace(MessagesUtil.loreInputCancelled)
                ))
        );

    }

    private void changeLocation() {
        this.home.setLocation(player.getLocation());
        Location oldLocation = home.getLocation();
        homeManager.update(home);
        ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
        manageHomeGUI.open(player);
        player.sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(
                        MessagesUtil.homeLocationUpdated,
                        "%old%", LocationUtil.format(oldLocation),
                        "%new%", LocationUtil.format(player.getLocation())
                )
        ));
        EffectUtil.play(
                player,
                ConfigUtil.editLocationSound,
                ConfigUtil.editLocationSoundVolume,
                ConfigUtil.editLocationSoundPitch,
                ConfigUtil.editLocationParticle,
                ConfigUtil.editLocationParticleCount,
                ConfigUtil.editLocationParticleRadius
        );
    }

    private void deleteHome() {
        player.closeInventory();
        H0M3.getInstance().getChatInputManager().awaitInput(
                player,
                PlaceholderUtil.replace(
                        MessagesUtil.promptDeleteHome,
                        "%confirm%", MessagesUtil.promptDeleteConfirmValue
                ),
                input -> {
                    if (input.equalsIgnoreCase(MessagesUtil.promptDeleteConfirmValue)) {
                        String name = home.getDisplayName();
                        homeManager.deleteHome(player.getUniqueId(), name);
                        player.sendMessage(ChatFormat.info(
                                PlaceholderUtil.replace(MessagesUtil.homeDeleted, "%name%", name)
                        ));
                        EffectUtil.play(
                                player,
                                ConfigUtil.deleteHomeSound,
                                ConfigUtil.deleteHomeSoundVolume,
                                ConfigUtil.deleteHomeSoundPitch,
                                ConfigUtil.deleteHomeParticle,
                                ConfigUtil.deleteHomeParticleCount,
                                ConfigUtil.deleteHomeParticleRadius
                        );
                        HomesGUI homesGUI = new HomesGUI(homeManager, player);
                        homesGUI.open(player);
                    } else {
                        player.sendMessage(ChatFormat.error(
                                PlaceholderUtil.replace(MessagesUtil.homeDeletionCancelled)
                        ));
                        ManageHomeGUI manageHomeGUI = new ManageHomeGUI(homeManager, home, player);
                        manageHomeGUI.open(player);
                    }
                }, () -> player.sendMessage(ChatFormat.error(
                        PlaceholderUtil.replace(MessagesUtil.homeDeletionCancelled)
                ))
        );
    }

    private void backButton() {
        player.closeInventory();
        HomesGUI homesGUI = new HomesGUI(homeManager, player);
        homesGUI.open(player);
    }

    private void homeInformation() {
        homeManager.sendInfo(home, player);
    }
}

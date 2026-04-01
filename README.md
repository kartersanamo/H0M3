# H0M3 - v1.0.0

A comprehensive and feature-rich Spigot/Paper plugin for managing player homes in Minecraft. Players can create multiple customizable homes, teleport between them, and manage all their homes through an intuitive GUI interface.

## 📋 Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Commands](#commands)
- [Permissions](#permissions)
- [Configuration](#configuration)
- [Customization](#customization)
- [File Storage](#file-storage)
- [Troubleshooting](#troubleshooting)
- [Changelog](#changelog)
- [Support](#support)

## Features

### Home Management
- **Create Homes**: Players can set multiple homes (default: 10 max per player)
- **Teleport to Homes**: Quick teleportation to any saved home with optional delay and effects
- **Delete Homes**: Remove unwanted homes from your collection
- **Rename Homes**: Change home display names (3-16 characters)
- **Move Homes**: Update a home's location to your current position
- **Home Info**: View detailed information about any home including creation time and last usage

### Home Customization
- **Custom Materials**: Assign different block materials to represent homes (displayed in GUI)
- **Custom Lore**: Add descriptive text to homes for better organization
- **Home Metadata**: Automatically tracks creation timestamp and last used timestamp

### User Interface
- **Homes GUI**: Browse all your homes in an interactive inventory interface
- **Home Management GUI**: Manage individual homes with easy-to-use buttons:
  - Change display name
  - Change material
  - Change lore (description)
  - Change location
  - Delete home
  - View home information
- **Interactive Chat Input**: Use chat prompts for entering names, materials, and lore

### Teleportation Features
- **Optional Teleport Delay**: Configurable countdown timer with visual effects
- **Particle Effects**: Customizable particles during teleportation delay and on arrival
- **Sound Effects**: Audio feedback for all actions (setting homes, deleting, teleporting, etc.)
- **Teleport Protection**: Prevents teleportation if player moves during countdown

### Admin Features
- **Plugin Reload**: Reload messages and configuration with `/h0m3 reload`
- **Comprehensive Logging**: Detailed server logs for debugging and monitoring

## Requirements

- **Minecraft Version**: 1.21+ (API version 1.21)
- **Java**: Java 21+
- **Server Software**: Spigot or Paper (or compatible fork)

## Installation

1. **Download** the compiled JAR file (`H0M3-1.0.0.jar`)
2. **Place** the JAR file in your server's `plugins` directory
3. **Restart** your server or reload plugins using `/reload confirm`
4. **Configure** the plugin using the generated `config.yml` and `messages.yml` files

## Commands

### General Commands

| Command | Usage | Description | Permission |
|---------|-------|-------------|-----------|
| `/sethome <name>` | `/sethome Home1` | Set a new home at your current location | `h0m3.sethome` |
| `/home <name>` | `/home Home1` | Teleport to a saved home | `h0m3.home` |
| `/homes` | `/homes` | Open the homes GUI to manage all homes | `h0m3.homes` |
| `/deletehome <name>` | `/deletehome Home1` | Delete a home | `h0m3.deletehome` |
| `/renamehome <name> <new_name>` | `/renamehome Home1 NewHome` | Rename a home | `h0m3.renamehome` |
| `/movehome <home>` | `/movehome Home1` | Move a home to your current location | `h0m3.movehome` |
| `/edithome <home>` | `/edithome Home1` | Open the home management GUI | `h0m3.edithome` |
| `/homeinfo <home>` | `/homeinfo Home1` | View information about a home | `h0m3.homeinfo` |

### Admin Commands

| Command | Usage | Description | Permission |
|---------|-------|-------------|-----------|
| `/h0m3 reload` | `/h0m3 reload` | Reload configuration and messages | `h0m3.reload` |

### Command Aliases

- `/deletehome` → `/delhome`
- `/renamehome` → `/changename`

## Permissions

```yaml
h0m3.sethome          # Allow player to set homes
h0m3.home             # Allow player to teleport to homes
h0m3.homes            # Allow player to open homes GUI
h0m3.deletehome       # Allow player to delete homes
h0m3.renamehome       # Allow player to rename homes
h0m3.movehome         # Allow player to move homes
h0m3.edithome         # Allow player to edit homes
h0m3.homeinfo         # Allow player to view home information
h0m3.reload           # Allow admin to reload configuration
```

## Configuration

### Home Settings (`config.yml`)

```yaml
home:
  max-number: 10                    # Maximum homes per player
  name:
    min-length: 3                   # Minimum home name length
    max-length: 16                  # Maximum home name length
  default-material: "CHEST"         # Default material for homes in GUI
```

### GUI Settings

```yaml
gui:
  manage-home:
    size: 27                        # Manage home GUI size (inventory rows × 9)
  homes:
    min-size: 9                     # Minimum homes GUI size
    max-rows: 6                     # Maximum rows in homes GUI
```

### Teleportation Effects

```yaml
teleport:
  delay:
    enabled: false                  # Enable teleport countdown
    seconds: 3                      # Countdown duration
    particle: "DRIPPING_LAVA"       # Particle during countdown
    particle-count: 40              # Number of particles
    particle-radius: 1.0            # Particle spawn radius
    sound: "BLOCK_NOTE_BLOCK_BELL"  # Sound during countdown
    sound-volume: 1.0               # Sound volume
    sound-pitch: 1.0                # Sound pitch
  complete:
    particle: "HAPPY_VILLAGER"      # Particle on arrival
    particle-count: 40
    particle-radius: 1.0
    sound: "ENTITY_EXPERIENCE_ORB_PICKUP"
    sound-volume: 1.0
    sound-pitch: 1.0
```

### Action Effects

```yaml
effects:
  set-home:
    sound: "ENTITY_EXPERIENCE_ORB_PICKUP"
    sound-volume: 1.0
    sound-pitch: 1.0
    particle: "NONE"
    particle-count: 40
    particle-radius: 1.0
  # Similar configuration for: delete-home, rename-home, move-home, 
  # edit-material, edit-lore, edit-location
```

## Customization

### Messages (`messages.yml`)

All in-game messages are fully customizable in the `messages.yml` file. You can modify:

- Command responses and error messages
- GUI titles and button descriptions
- Chat input prompts
- Teleportation messages
- Home information displays
- Color codes and formatting

Example customizations:
```yaml
chat-prefix-default: "&8&l[&4&lH0M3&8&l]"      # Default prefix
home-set: "Set your home %name% at %location%" # Home created message
home-limit-reached: "You have reached the maximum number of homes (%max%)"
```

### Color Codes

Use Minecraft's standard color codes in messages:
- `&0` - Black
- `&1-9` - Colors
- `&a` - Green
- `&c` - Red
- `&e` - Yellow
- `&l` - Bold
- `&m` - Strikethrough
- `&o` - Italic

## File Storage

Homes are automatically saved per-player and loaded when they join the server:
- Each player's homes are stored individually
- Automatic loading on player join
- Automatic unloading on player quit
- Data persistence across server restarts

## Troubleshooting

### No homes appearing in GUI
- Ensure homes have been created with `/sethome`
- Check that the player has sufficient permissions
- Review server logs for any errors

### Teleport delay not working
- Enable the teleport delay in `config.yml`: `teleport.delay.enabled: true`
- Adjust `seconds` and effect settings as needed

### Messages not updating
- Run `/h0m3 reload` to reload configuration
- Ensure `messages.yml` exists in the plugin folder
- Check for YAML syntax errors in configuration files

### Command not found
- Verify plugin is enabled: Check server logs on startup
- Ensure JAR file is in the `plugins` folder
- Confirm you're using a compatible server version (1.21+)

## Changelog

### Version 1.0.0
- Initial release
- Complete home management system
- Interactive GUI interface
- Customizable effects and messages
- Multi-home support per player
- Home metadata tracking (creation time, last used)
- Teleportation delay and effects system
- Comprehensive permission system

## Support

For issues, questions, or feature requests, please report them through the appropriate channels.

---

**Author**: KarterSanamo  
**Version**: 1.0.0  
**Tested on**: Minecraft 1.21+  
**License**: All rights reserved

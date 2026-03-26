
# Mob XP

This mod allows setting a custom amount of xp that mobs will drop when killed. This includes being able to change the amount of xp the Ender Dragon drops on the first kill and subsequent kills.

Vanilla Minecraft had different xp amounts for some baby mob variants (e.g. Zombie babies), and chickens that had jockeys. So this mod also allows setting custom amounts for any baby mob variant and chickens with jockeys if wanting to do so.

**GUI**

Starting with v1.1.3 there is an ingame GUI that can be used to modify the custom xp amounts and set the various flags. The default key for opening the GUI is the Keypad 5 when loaded in a world.

![Mob XP Editor](https://cdn.modrinth.com/data/cached_images/229443bb46adb90f2ba797bf2c4036bfcae3d240_0.webp)


## Configuration (v1.1.3 and above)

Starting with **v1.1.3**, all custom mob XP settings are stored as **NBT data on a per-world basis**.  
This means each world maintains its own independent XP configuration.

### File Location

The configuration file is named:

`mob_xp_data.dat`

and is stored in the world’s `data` directory.

**Singleplayer worlds**

`.minecraft/saves/<WorldName>/data/mob_xp_data.dat`

**Dedicated / Integrated servers**

`<WorldName>/data/mob_xp_data.dat`

---

## NBT Data Structure

Each entry represents custom XP settings for a specific mob.

| Field | Description |
|------|------------|
| `id` | Minecraft entity ID (e.g. `minecraft:ender_dragon`) |
| `primaryXP` | Base amount of XP dropped when the mob is killed |
| `secondaryXP` | Alternate XP value used for special conditions (e.g. chicken jockeys, non-first Ender Dragon kills) |
| `babyXP` | XP dropped when killing a baby variant of the mob |
| `enabled` | If `true`, custom XP values are used; if `false`, vanilla XP behavior is applied |
| `random` | If `true`, XP dropped will be a random value between `0` and the configured XP amount |
| `usePrimaryXPForBaby` | If `true`, baby mobs use `primaryXP` instead of `babyXP` |

**Notes**
- If `usePrimaryXPForBaby` is enabled, `babyXP` is ignored.
- If `enabled` is `false`, all other settings for that mob are ignored and vanilla XP is used.
- Any negative number entered for a xp amount will cause it not to be used and vanilla XP will be applied
---

## Commands

### View XP Configuration

`/mobxp <entity_id>`

Displays the current custom XP settings for the specified mob.

---

### Modify XP Values

**Set primary XP**

`/mobxp <entity_id> primaryXP <amount>`


**Set secondary XP**

`/mobxp <entity_id> secondaryXP <amount>`


**Set baby mob XP**

`/mobxp <entity_id> babyXP <amount>`


---

### Toggle Flags

**Enable or disable custom XP**

`/mobxp <entity_id> enabled <true|false>`

**Enable random XP drops**

`/mobxp <entity_id> random <true|false>`

**Use adult XP for baby mobs**

`/mobxp <entity_id> usePrimaryXPForBaby <true|false>`

---

## Configuration Pre 1.1.3

On first run config file will be generated in config/mobxp.json.

- Find the mob identifier for the mob
- Set the "experiencePoints" to the xp you want for that mob
- Enable it by setting "enabled" to true
- Set "random" to true if you want the xp amount to randomize between 1 and "experiencePoints"

## Example config

- Sets Ender Dragon xp drops to 15,000 for both first kill and all other kills
- Sets Villager xp drops to 1,000

```
{
  "dragonXP": 15000,
  "firstDragonXP": 15000,

  "xp": {
    "minecraft:villager": {
      "enabled": true,
      "experiencePoints": 1000,
      "random": false
    }
  }
}
```

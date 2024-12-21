package ovh.csmp.cSMPSacks

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerExpChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.block.Action
import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.event.EventPriority
import org.bukkit.inventory.ItemStack

class listener : Listener {

    companion object {
        val instance = listener()
    }

    //exp update helper func
    fun expUpdate(player: Player) {
        val exp: Double
        if (player.level < 100) {
            exp = player.level.toDouble()
            data.playerData["${player.uniqueId}.ManaRegen"] = player.level.toDouble() / 100.0 * 4.0
        } else {
            exp = 100.0
            data.playerData["${player.uniqueId}.ManaRegen"] = 4.0
        }
        data.playerData["${player.uniqueId}.Mana"] = ((data.playerData["${player.uniqueId}.BaseMana"] as? Number)?.toDouble() ?: 0.0) + exp
        val mana = (data.playerData["${player.uniqueId}.Mana"] as? Number)?.toDouble() ?: 0.0
        val currMana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble() ?: 0.0
        if (currMana > mana) {
            data.playerData["${player.uniqueId}.CurrentMana"] = mana
        }
    }

    //pvp disabler
    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val attacker = event.damager
        val victim = event.entity

        if (attacker is Player && victim is Player) {
            if (!data.pvpBool) {
                event.isCancelled = true
            }
        }
    }

    //event for xp change
    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerExpChange(event: PlayerExpChangeEvent) {
        val player = event.player
        expUpdate(player)
    }

    //event for respawn
    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        expUpdate(player)
        data.playerData["${player.uniqueId}.CurrentMana"] = 0.0
    }

    //interaction
    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val itemInHand: ItemStack? = event.item

        if (itemInHand != null && itemInHand.type != Material.AIR) {
            if (itemInHand.hasItemMeta() && itemInHand.itemMeta?.hasCustomModelData() == true) {
                event.isCancelled = true
            }
        }

        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            if (itemInHand != null && itemInHand.itemMeta.displayName == items.leather_tooling_table.returnName()) {
                tableGUI.instance.mainGUI(player)
            }
        }
    }

    //setup
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!data.playerData.containsKey("${player.uniqueId}.Sack")) {
            data.playerData["${player.uniqueId}.Sack"] = "None"
        }
        if (!data.playerData.containsKey("${player.uniqueId}.Slots")) {
            data.playerData["${player.uniqueId}.Slots"] = 0
        }
        if (!data.playerData.containsKey("${player.uniqueId}.ManaRegen")) {
            data.playerData["${player.uniqueId}.ManaRegen"] = 0.0
        }
        if (!data.playerData.containsKey("${player.uniqueId}.BaseMana")) {
            data.playerData["${player.uniqueId}.BaseMana"] = 0.0
        }

        data.playerData["${player.uniqueId}.CurrentMana"] = 0.0

        if (!data.playerData.containsKey("${player.uniqueId}.Mana")) {
            data.playerData["${player.uniqueId}.Mana"] = ((data.playerData["${player.uniqueId}.BaseMana"] as? Number)?.toDouble() ?: 0.0)
        }
        if (!data.playerData.containsKey("${player.uniqueId}.EquippedAbilities")) {
            data.playerData["${player.uniqueId}.EquippedAbilities"] = arrayOf("None", "None", "None", "None")
        }
        if (!data.playerData.containsKey("${player.uniqueId}.Abilities")) {
            data.playerData["${player.uniqueId}.Abilities"] = arrayListOf("None")
        }
        expUpdate(player)
    }
}
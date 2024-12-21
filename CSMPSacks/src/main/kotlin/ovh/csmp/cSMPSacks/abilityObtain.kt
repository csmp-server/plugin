package ovh.csmp.cSMPSacks

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.random.Random

class abilityObtain: Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val itemInHand = player.inventory.itemInMainHand

        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            itemInHand.itemMeta?.let { itemMeta ->
                when (itemMeta.customModelData) {
                    items.magic_wand.modelID -> {
                        event.isCancelled = true
                        bugs.instance.removeItem(player, itemInHand)
                        giveTier1(player)
                    }
                    items.ability_shard.modelID -> {
                        event.isCancelled = true
                        bugs.instance.removeItem(player, itemInHand)
                        giveTier2(player)
                    }
                    else -> {
                        return
                    }
                }
            }
        }
    }

    //t1
    private fun giveTier1(player: Player) {
        val n: Int = Random.nextInt(1, 100)
        if (n > 10) {
            player.sendMessage("§4You did not get any abilities :(")
            return
        }

        val abilities: ArrayList<String> = data.playerData["${player.uniqueId}.Abilities"] as ArrayList<String>

        if (abilities[0] == "None") {
            val ability = tier1.list.random()
            abilities[0] = ability.id
            data.playerData["${player.uniqueId}.Abilities"] = abilities
            player.sendMessage("§aYou got ${ability.name}")
        } else {
            val ability = tier1.list.random()
            abilities.add(ability.id)
            data.playerData["${player.uniqueId}.Abilities"] = abilities
            player.sendMessage("§aYou got ${ability.name}")
        }
    }

    //t2
    private fun giveTier2(player: Player) {
        val n: Int = Random.nextInt(1, 100)
        if (n > 10) {
            player.sendMessage("§4You did not get any abilities :(")
            return
        }

        val abilities: ArrayList<String> = data.playerData["${player.uniqueId}.Abilities"] as ArrayList<String>

        if (abilities[0] == "None") {
            val ability = tier2.list.random()
            abilities[0] = ability.id
            data.playerData["${player.uniqueId}.Abilities"] = abilities
            player.sendMessage("§aYou got ${ability.name}")
        } else {
            val ability = tier2.list.random()
            abilities.add(ability.id)
            data.playerData["${player.uniqueId}.Abilities"] = abilities
            player.sendMessage("§aYou got ${ability.name}")
        }
    }
}
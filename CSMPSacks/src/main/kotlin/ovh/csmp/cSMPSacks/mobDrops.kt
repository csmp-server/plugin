package ovh.csmp.cSMPSacks

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.random.Random

class mobDrops: Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDeath(event: EntityDeathEvent) {
        if (event.entity.killer !is Player) {
            return
        }
        when (event.entityType) {

            EntityType.WITCH -> {
                witchLootTable(event)
            }
            EntityType.EVOKER -> {
                evokerLootTable(event)
            }
            EntityType.WITHER -> {
                witherLootTable(event)
            }
            else -> {
                return
            }

        }
    }

    //witch
    private fun witchLootTable(event: EntityDeathEvent) {
        val n = Random.nextInt(1,100)
        if (n < 91) {
            event.drops.add(items.magic_wand.returnItem())
        }
    }

    //evoker
    private fun evokerLootTable(event: EntityDeathEvent) {
        val n = Random.nextInt(1, 100)
        if (n in 51..55) {
            event.drops.add(items.illusion_cloak.returnItem())
        }
    }

    //wither
    private fun witherLootTable(event: EntityDeathEvent) {
        val n = Random.nextInt(1, 100)
        if (n in 41..65) {
            event.drops.add(items.ability_shard.returnItem())
        }
    }

}
package ovh.csmp.cSMPSacks

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.random.Random

class mobDrops: Listener {

    @EventHandler
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
            else -> {
                return
            }

        }
    }

    //witch
    private fun witchLootTable(event: EntityDeathEvent) {
        val n = Random.nextInt(1,10)
        if (n == 4) {
            event.drops.add(items.magic_wand.returnItem())
        }
    }

    //evoker
    private fun evokerLootTable(event: EntityDeathEvent) {
        val n = Random.nextInt(1, 50)
        if (n == 36) {
            event.drops.add(items.illusion_cloak.returnItem())
        }
    }

}
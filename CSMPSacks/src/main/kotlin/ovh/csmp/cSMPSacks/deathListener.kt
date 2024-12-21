package ovh.csmp.cSMPSacks

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class deathListener: Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.player
        val mkiller: Player? = deathLogs.magicLogs[victim]
        val rkiller: Player? = deathLogs.rawLogs[victim]

        when {
            mkiller != null -> {
                event.deathMessage = "${victim.name} was slain by ${mkiller.name} using magic"
                deathLogs.magicLogs.remove(victim)
            }
            rkiller != null -> {
                event.deathMessage = "${victim.name} was slain by ${rkiller.name} using raw strength"
                deathLogs.rawLogs.remove(victim)
            }
        }

    }

}
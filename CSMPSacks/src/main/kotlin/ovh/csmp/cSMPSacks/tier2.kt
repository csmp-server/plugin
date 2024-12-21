package ovh.csmp.cSMPSacks

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import ovh.csmp.cSMPSacks.tier1.mend
import ovh.csmp.cSMPSacks.tier1.shield
import kotlin.random.Random

object tier2 {

    val restoration = ability("Restoration", "restoration", "2", 115.0)
    val guard = ability("Guard", "guard", "2", 115.0)
    val blink = ability("Blink", "blink", "2", 115.0)

    val list = arrayOf(restoration, guard, blink)

    fun restoration(player: Player) {
        if (restoration.useAbility(player)) {
            player.health = (player.health + 10.0).coerceAtMost(player.maxHealth)
        }
    }

    fun guard(player: Player) {
        if (guard.useAbility(player)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 400, 2))
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 400, 0))
        }
    }

    fun blink(player: Player) {
        if (blink.useAbility(player)) {
            val nearbyPlayers = player.world.getNearbyEntities(player.location, 5.0, 5.0, 5.0).filterIsInstance<Player>().filter { it != player }
            if (nearbyPlayers.isEmpty()) {
                player.sendMessage("§4No players found within a 5 block radius!")
                return
            }

            val target: Player = nearbyPlayers.random()
            player.teleport(target.location)
            target.sendMessage("§4Teleported to ${target.name}!")
        }
    }

}
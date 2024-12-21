package ovh.csmp.cSMPSacks

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.floor

object tier1 {

    val mend = ability("Mend", "mend", "1", 75.0)
    val grip = ability("Grip", "grip", "1", null)
    val shield = ability("Shield", "shield", "1", 75.0)

    val list = arrayOf(mend, grip, shield)

    fun mend(player: Player) {
        if (mend.useAbility(player)) {
            player.health = (player.health + 6.0).coerceAtMost(player.maxHealth)
        }
    }

    fun grip(player: Player) {
        if (grip.useAbility(player)) {
            val maxmana = data.playerData["${player.uniqueId}.Mana"] as Double
            val damage = floor(maxmana / 100.0)

            for (target in Bukkit.getOnlinePlayers()) {
                if (target != player && target.location.distance(player.location) <= 3.0) {
                    target.health = (target.health - damage).coerceAtLeast(0.0)
                    if (target.health == 0.0) {
                        deathLogs.magicLogs[target] = player
                    }
                    target.sendMessage("§4${player.name} hit you with grip for ${damage}❤!")
                    player.sendMessage("§4You dealt ${3}❤ to ${target.name}!")
                }
            }
        }
    }

    fun shield(player: Player) {
        if (shield.useAbility(player)) {
            player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, 400, 1))
        }
    }

}
package ovh.csmp.cSMPSacks

import org.bukkit.entity.Player

class ability(
    val name: String,
    val id: String,
    val tier: String,
    var manaCost: Double?
) {
    fun useAbility(player: Player): Boolean {
        when {
            manaCost == null -> {
                var playermana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
                val maxmana = (data.playerData["${player.uniqueId}.Mana"] as? Number)?.toDouble()?: 0.0
                if (playermana != maxmana) {
                    player.sendMessage("§4You do not have enough mana!")
                    return false
                } else {
                    playermana = 0.0
                    data.playerData["${player.uniqueId}.CurrentMana"] = playermana
                    player.sendMessage("§bYou have used ability §e${name}§b (-${maxmana} ✎")
                    return true
                }
            }
            manaCost!! >= 1 -> {
                var playermana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
                if (manaCost!! > playermana) {
                    player.sendMessage("§4You do not have enough mana!")
                    return false
                } else {
                    playermana -= manaCost!!
                    data.playerData["${player.uniqueId}.CurrentMana"] = playermana
                    player.sendMessage("§bYou have used ability §e${name}§b (-${manaCost} ✎")
                    return true
                }
            }
            manaCost!! < 1 -> {
                var playermana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
                val maxmana = (data.playerData["${player.uniqueId}.Mana"] as? Number)?.toDouble()?: 0.0
                manaCost = maxmana * manaCost!!

                if (manaCost!! > playermana) {
                    player.sendMessage("§4You do not have enough mana!")
                    return false
                } else {
                    manaCost = manaCost!! - manaCost!!
                    data.playerData["${player.uniqueId}.CurrentMana"] = playermana
                    player.sendMessage("§bYou have used ability §e${name}§b (-${manaCost} ✎)")
                    return true
                }

            }
            else -> {
                player.sendMessage("§4An unexpected error occured, this is NOT your issue, please contact admins asap!")
                return false
            }
        }
    }
}
package ovh.csmp.cSMPSacks

import org.bukkit.entity.Player

class ability(
    val name: String,
    val id: String,
    val tier: String,
    val manaCost: Double?
) {
    fun useAbility(player: Player) {
        if (manaCost != null) {
            var playermana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
            if (manaCost > playermana) {
                player.sendMessage("§4You do not have enough mana!")
            } else {
                playermana -= manaCost
                data.playerData["${player.uniqueId}.CurrentMana"] = playermana
                player.sendMessage("§bYou have used ability §e${name}§b (-${manaCost} ✎")
            }
        } else {
            var playermana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
            val maxmana = (data.playerData["${player.uniqueId}.Mana"] as? Number)?.toDouble()?: 0.0
            if (playermana != maxmana) {
                player.sendMessage("§4You do not have enough mana!")
            } else {
                playermana = 0.0
                data.playerData["${player.uniqueId}.CurrentMana"] = playermana
                player.sendMessage("§bYou have used ability §e${name}§b (-${manaCost} ✎")
            }
        }
    }
}
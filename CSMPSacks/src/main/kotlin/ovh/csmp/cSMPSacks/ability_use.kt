package ovh.csmp.cSMPSacks

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ability_use: Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.item == null || event.item?.type == Material.AIR || !event.item!!.hasItemMeta() || !event.item!!.itemMeta.hasCustomModelData() || event.item!!.itemMeta.customModelData !in 2000..2004) {
            return
        }

        val act = event.action

        when (act) {
            Action.RIGHT_CLICK_AIR -> sackRightClick(event.player)
            Action.RIGHT_CLICK_BLOCK -> sackRightClick(event.player)

            Action.LEFT_CLICK_AIR -> sackLeftClick(event.player)
            Action.LEFT_CLICK_BLOCK -> sackLeftClick(event.player)
            else -> return
        }

    }

    private fun useAbility(player: Player, i: Int) {
        val eqa = data.playerData["${player.uniqueId}.EquippedAbilities"] as Array<String>
        val slots = data.playerData["${player.uniqueId}.Slots"] as Int

        if ((i + 1) > slots) {
            player.sendMessage("§4Your sack is not powerful enough to support that slot!")
            return
        }

        if (eqa[i] != "None") {
            when (eqa[i]) {
                "mend" -> tier1.mend(player)
                "grip" -> tier1.grip(player)
                "shield" -> tier1.shield(player)
                "restoration" -> tier2.restoration(player)
                "guard" -> tier2.guard(player)
                "blink" -> tier2.blink(player)
                else -> {
                    player.sendMessage("§4An unexpected error occured, this is NOT your issue, please contact admins asap!")
                    return
                }
            }
        } else {
            player.sendMessage("§4You dont have an ability equipped for that slot!")
            return
        }
    }

    private fun sackRightClick(player: Player) {
        if (player.isSneaking) {
            sackShiftRightClick(player)
            return
        }

        useAbility(player, 0)

    }

    private fun sackLeftClick(player: Player) {
        if (player.isSneaking) {
            sackShiftLeftClick(player)
            return
        }

        useAbility(player, 1)

    }

    private fun sackShiftRightClick(player: Player) {

        useAbility(player, 2)

    }

    private fun sackShiftLeftClick(player: Player) {

        useAbility(player, 3)

    }
}
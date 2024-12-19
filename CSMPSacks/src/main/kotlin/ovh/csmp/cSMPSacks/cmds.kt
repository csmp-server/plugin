package ovh.csmp.cSMPSacks

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Bukkit
import java.lang.NumberFormatException

class cmds(private val plugin: JavaPlugin) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return when (command.name.lowercase()) {
            "itemgive" -> itemCommand(sender, args)
            "ability" -> abilityCommand(sender, args)
            else -> false
        }
    }

    private fun itemCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§4Only players may execute this!")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§4Invalid usage! /itemgive <item_id> <count>")
            return false
        }

        val itemid = args[0].lowercase()
        var n: Int
        try {
            n = args[1].toInt()
        } catch (e: ArrayIndexOutOfBoundsException) {
            n = 1
        } catch (e: NumberFormatException) {
            n = 1
        }
        return when (itemid) {
            "leather_tooling_table" -> {
                items.leather_tooling_table.give(sender, n)
                true
            }

            "full_grain_leather" -> {
                items.full_grain_leather.give(sender, n)
                true
            }

            "refined_leather" -> {
                items.refined_leather.give(sender, n)
                true
            }

            "magic_wand" -> {
                items.magic_wand.give(sender, n)
                true
            }

            "illusion_cloak" -> {
                items.illusion_cloak.give(sender, n)
                true
            }

            "ability_shard" -> {
                items.ability_shard.give(sender, n)
                true
            }

            "travellers_sack" -> {
                sacks.travellers_sack.give(sender)
                true
            }

            "illusion_sack" -> {
                sacks.illusion_sack.give(sender)
                true
            }

            else -> {
                sender.sendMessage("§4Not a valid item!")
                false
            }
        }
    }

    private fun abilityCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§4Only players may execute this!")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§4Invalid usage! /ability <action> <ability_id> <slot>")
            return false
        }

        val action = args[0].lowercase()
        if (action == "view") {
            return true
        } else {
            return false
        }
    }
}

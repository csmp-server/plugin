package ovh.csmp.cSMPSacks

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import kotlin.NumberFormatException

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
            sender.sendMessage("§4Invalid usage! /itemgive <item_id> [count]")
            return true
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
            sender.sendMessage("§4Invalid usage! /ability <action> [parameters]")
            return true
        }

        return when (args[0].lowercase()) {
            "view" -> {
                val abilities: ArrayList<*> = data.playerData["${sender.uniqueId}.Abilities"] as ArrayList<*>
                val rabilities = abilities.joinToString(", ")

                sender.sendMessage("§aYour owned abilities: $rabilities")

                sender.sendMessage("§a---------------------------------")

                val eqa: Array<*> = data.playerData["${sender.uniqueId}.EquippedAbilities"] as Array<*>
                for (i in 0..3) {
                    sender.sendMessage("§a Slot ${i + 1}: ${eqa[i].toString()}")
                }
                true
            }
            else -> {
                if (args[0].lowercase() == "equip" && args.size == 3) {
                    val abilities: ArrayList<String> = data.playerData["${sender.uniqueId}.Abilities"] as ArrayList<String>
                    val eqa: Array<String> = data.playerData["${sender.uniqueId}.EquippedAbilities"] as Array<String>

                    var slot: Int

                    try {
                        slot = args[2].toInt()
                    } catch (e: NumberFormatException) {
                        sender.sendMessage("§4Invalid usage! /ability <action> [parameters]")
                        return true
                    }

                    if (abilities.contains(args[1]) && slot in 1..4) {
                        slot -= 1 //convert to index
                        abilities.remove(args[1])
                        if (eqa[slot] == "None") {
                            eqa[slot] = args[1]
                        } else {
                            val temp = eqa[slot]
                            abilities.add(temp)
                            eqa[slot] = args[1]
                        }

                        if (abilities.isEmpty()) {
                            abilities.add("None")
                        }

                        data.playerData["${sender.uniqueId}.Abilities"] = abilities
                        data.playerData["${sender.uniqueId}.EquippedAbilities"] = eqa
                        sender.sendMessage("§aEquipped ability!")
                        return true
                    } else {
                        sender.sendMessage("§4You don't own that ability or that ability doesn't exist!")
                        return true
                    }
                } else if (args[0] == "unequip" && args.size == 2){
                    val abilities: ArrayList<String> = data.playerData["${sender.uniqueId}.Abilities"] as ArrayList<String>
                    val eqa: Array<String> = data.playerData["${sender.uniqueId}.EquippedAbilities"] as Array<String>
                    var slot: Int

                    try {
                        slot = args[1].toInt()
                    } catch (e: NumberFormatException) {
                        sender.sendMessage("§4Invalid usage! /ability <action> [parameters]")
                        return true
                    }

                    slot -= 1 //convert to index
                    val temp = eqa[slot]
                    eqa[slot] = "None"
                    abilities.add(temp)

                    if (abilities.contains("None")) {
                        abilities.remove("None")
                    }

                    data.playerData["${sender.uniqueId}.Abilities"] = abilities
                    data.playerData["${sender.uniqueId}.EquippedAbilities"] = eqa

                    return true
                } else {
                    sender.sendMessage("§4Invalid usage! /ability <action> [parameters]")
                    return true
                }
            }
        }
    }
}

package ovh.csmp.cSMPSacks
import org.bukkit.Material

object items {
    val leather_tooling_table = item("Leather Tooling Table", "leather_tooling_table", 1000, "COMMON", Material.CRAFTING_TABLE, listOf("A leather worker's wet dream,", "right click to craft anything with it"), true)
    val full_grain_leather = item("Full-grain Leather", "full_grain_leather", 1001, "COMMON", Material.LEATHER, listOf("Refined leather from a wild cow", "Used to craft sacks"), true)
    val refined_leather = item("Refined Leather", "refined_leather", 1002, "RARE", Material.RABBIT_HIDE, listOf("A piece of leather that only", "a few masters can acquire", "Used to craft the best of the", "best sacks"), true)
    val magic_wand = item("Magic Wand", "magic_wand", 1003, "COMMON", Material.STICK, listOf("Harry Potter wand from wish.com", "Drop and have a 10% chance to", "get a Tier 1 Ability"), true)
    val illusion_cloak = item("Illusion Cloak", "illusion_cloak", 1004, "UNCOMMON", Material.HONEYCOMB, listOf("Superman NOT on steroids", "Used to craft Illusion Sack"), true)
    val ability_shard = item("Ability Shard", "ability_shard", 1005, "UNCOMMON", Material.AMETHYST_SHARD, listOf("Once a dead man held this,", "and he got a cool lil ability", "Drop to have a 10% chance to", "get a Tier 2 Ability"), true)
}




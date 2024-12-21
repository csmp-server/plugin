package ovh.csmp.cSMPSacks

import org.bukkit.Material

enum class tierclass(var color: String, var item: Material) {
    TIER_1("§f", Material.SKULL_POTTERY_SHERD),
    TIER_2("§a", Material.GHAST_TEAR),
    TIER_3("§3", Material.PRISMARINE_SHARD),
    TIER_4("§e", Material.AMETHYST_SHARD),
    TIER_5("§d", Material.ECHO_SHARD)
}

package ovh.csmp.cSMPSacks

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class sack(val name: String,
           val id: String,
           val rarity: String,
           val modelID: Int,
           val maxmana: Int,
           val slots: Int
) {

    fun give(player: Player) {
        val color: String = rarityclass.valueOf(this.rarity.uppercase()).color
        val item = ItemStack(Material.KNOWLEDGE_BOOK)
        val meta: ItemMeta = item.itemMeta ?: return
        val lore = listOf("", "§7Slots: §a${slots}", "§7Mana: §b${maxmana}", "", "§7Right click in inventory to set abilites", "for this sack!", "", "$color§l${rarity.uppercase()} SACK", "§7ID: $id")
        meta.setDisplayName("${color}${name}")
        meta.lore = lore
        meta.setCustomModelData(modelID)
        meta.isUnbreakable = true
        item.itemMeta = meta
        player.inventory.addItem(item)
    }
}
package ovh.csmp.cSMPSacks

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.ItemFlag

class item(
    val name: String,
    val id: String,
    val modelID: Int?,
    val rarity: String,
    val material: Material,
    var desc: List<String>,
    val encht: Boolean
) {
    fun give(player: Player, n: Int) {
        val color: String = rarityclass.valueOf(this.rarity.uppercase()).color
        val item = ItemStack(material, n)
        val meta: ItemMeta = item.itemMeta ?: return
        desc = desc.map { "§7$it" }
        var lore = listOf("", "", "$color§l${rarity.uppercase()} ITEM", "§7ID: $id")
        lore = lore.take(1) + desc + lore.drop(1)
        meta.setDisplayName("${color}${name}")
        meta.lore = lore
        meta.setCustomModelData(modelID)
        meta.isUnbreakable = true

        if (encht) {
            meta.addEnchant(Enchantment.MENDING, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        player.inventory.addItem(item)
    }
    fun returnItem(): ItemStack? {
        val color: String = rarityclass.valueOf(this.rarity.uppercase()).color
        val item = ItemStack(material)
        val meta: ItemMeta = item.itemMeta ?: return null
        desc = desc.map { "§7$it" }
        var lore = listOf("", "", "$color§l${rarity.uppercase()} ITEM", "§7ID: $id")
        lore = lore.take(1) + desc + lore.drop(1)
        meta.setDisplayName("${color}${name}")
        meta.lore = lore
        meta.setCustomModelData(modelID)
        meta.isUnbreakable = true

        if (encht) {
            meta.addEnchant(Enchantment.MENDING, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta

        return item
    }

    fun returnName(): String {
        val color: String = rarityclass.valueOf(this.rarity.uppercase()).color
        return "${color}${name}"
    }


}
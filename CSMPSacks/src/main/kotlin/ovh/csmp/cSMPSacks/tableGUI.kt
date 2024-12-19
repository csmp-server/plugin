package ovh.csmp.cSMPSacks

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class tableGUI: Listener {

    companion object {
        val instance = tableGUI()
    }

    fun mainGUI(player: Player) {
        val gui: Inventory = Bukkit.createInventory(null, 27, items.leather_tooling_table.name)
        player.openInventory(gui)

        val leather = ItemStack(Material.LEATHER, 1)
        val leatherMeta = leather.itemMeta
        leatherMeta?.setDisplayName("Refine Leather")
        leather.itemMeta = leatherMeta

        val chest = ItemStack(Material.CHEST, 1)
        val chestMeta = chest.itemMeta
        chestMeta?.setDisplayName("Craft Sacks")
        chest.itemMeta = chestMeta

        gui.setItem(11, leather)
        gui.setItem(15, chest)
    }
}
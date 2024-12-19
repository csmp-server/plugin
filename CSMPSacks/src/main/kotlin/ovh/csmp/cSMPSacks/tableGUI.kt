package ovh.csmp.cSMPSacks

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.entity.Player
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.ItemMeta

class tableGUI: Listener {

    companion object {
        val instance = tableGUI()
    }

    //count item
    private fun getItemCount(player: Player, i: ItemStack): Int {
        var n: Int = 0
        for (item in player.inventory.contents) {
            if (item != null && item.isSimilar(i)) {
                n += item.amount
            }
        }
        return n
    }

    //gui interaction
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        when (event.view.title) {
            items.leather_tooling_table.returnName() -> {
                event.isCancelled = true
                val player = event.whoClicked as Player
                when (event.slot) {
                    11 -> {
                        refineGUI(player)
                    }
                    15 -> {
                        sacksGUI(player)
                    }
                    else -> {
                        return
                    }
                }
            }
            "§6Refine Leather" -> {
                event.isCancelled = true
                val player = event.whoClicked as Player
                when (event.slot) {
                    11 -> {
                        buyFgLeather(player)
                    }
                    15 -> {
                        buyRLeather(player)
                    }
                    else -> {
                        return
                    }
                }
            }
            else -> {
                return
            }
        }
    }

    //main gui
    fun mainGUI(player: Player) {
        val gui: Inventory = Bukkit.createInventory(null, 27, items.leather_tooling_table.returnName())
        player.openInventory(gui)

        val leather = ItemStack(Material.LEATHER, 1)
        val leatherMeta = leather.itemMeta
        leatherMeta.setDisplayName("§6Refine Leather")
        leather.itemMeta = leatherMeta

        val chest = ItemStack(Material.CHEST, 1)
        val chestMeta = chest.itemMeta
        chestMeta.setDisplayName("§6Craft Sacks")
        chest.itemMeta = chestMeta

        gui.setItem(11, leather)
        gui.setItem(15, chest)
    }

    //refine leather gui
    private fun refineGUI(player: Player) {
        val rgui: Inventory = Bukkit.createInventory(null, 27, "§6Refine Leather")

        val fgleather: ItemStack = items.full_grain_leather.returnItem()
        val rleather: ItemStack = items.refined_leather.returnItem()
        val fgmeta = fgleather.itemMeta
        val rmeta = rleather.itemMeta

        fgmeta?.lore = listOf("§b16x Leather")
        rmeta?.lore = listOf("§b16x Full-grain Leather")

        fgleather.itemMeta = fgmeta
        rleather.itemMeta = rmeta

        rgui.setItem(11, fgleather)
        rgui.setItem(15, rleather)

        player.openInventory(rgui)
    }

    //bug fg leather
    private fun buyFgLeather(player: Player) {
        val count: Int = getItemCount(player, ItemStack(Material.LEATHER))
        if (count < 16) {
            player.sendMessage("§4You dont have enought items for this craft")
        } else {
            player.inventory.removeItem(ItemStack(Material.LEATHER, 16))
            items.full_grain_leather.give(player)
            player.sendMessage("§aYou have bought Full-grain Leather")
        }
    }

    private fun buyRLeather(player: Player) {
        val count: Int = getItemCount(player, items.full_grain_leather.returnItem())
        if (count < 16) {
            player.sendMessage("§4You dont have enought items for this craft")
        } else {
            player.inventory.removeItem(items.full_grain_leather.returnItem(16))
            items.refined_leather.give(player)
            player.sendMessage("§aYou have bought Refined Leather")
        }
    }

    //sacks gui
    private fun sacksGUI(player: Player) {
        val sgui: Inventory = Bukkit.createInventory(null, 27, "§6Craft Sacks")

        val travellers: ItemStack = sacks.travellers_sack.returnSack()
        val illusion: ItemStack = sacks.illusion_sack.returnSack()
        val tmeta: ItemMeta = travellers.itemMeta
        val imeta: ItemMeta = illusion.itemMeta

        tmeta.lore = listOf("§b16x Full-grain Leather", "§b1x Magic Wand")
        imeta.lore = listOf("§b64x Full-grain Leather", "§bIllusion Cloak")

        travellers.itemMeta = tmeta
        illusion.itemMeta = imeta

        sgui.setItem(10, travellers)
        sgui.setItem(11, illusion)

        player.openInventory(sgui)
    }
}
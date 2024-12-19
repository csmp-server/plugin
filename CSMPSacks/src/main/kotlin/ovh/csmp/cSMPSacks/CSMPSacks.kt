package ovh.csmp.cSMPSacks

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import kotlin.math.ceil

//main class
class CSMPSacks : JavaPlugin() {

    private fun saveData(fileName: String, dataMap: MutableMap<String, Any>) {
        val file = File(fileName)
        file.printWriter().use { out ->
            for ((key, value) in dataMap) {
                out.println("$key: $value")
            }
        }
    }

    private fun loadData(fileName: String): MutableMap<String, Any> {
        val dataMap = mutableMapOf<String, Any>()
        val file = File(fileName)

        if (file.exists()) {
            file.forEachLine { line ->
                val (key, value) = line.split(":", limit = 2)
                dataMap[key] = value
            }
        }

        return dataMap
    }

    //data loading
    private fun configLoad() {
        val configFile = File(dataFolder, "config.yml")
        val dataFile = File(dataFolder, "data.yml")
        if (!configFile.exists()) {
            saveDefaultConfig()
            logger.info("Config file not found, created one")
        } else {
            logger.info("Config file found")
        }
    }

    //pvptimer main exec
    private fun pvpMain() {
        val grace = config.getInt("timer.grace")
        val pvp = config.getInt("timer.pvp")
        val warn = config.getInt("timer.warningtime")
        val warnmsg: String = config.getString("timer.messages.warning") ?: "<null>"
        val enablemsg: String = config.getString("timer.messages.enable") ?: "<null>"
        val disablemsg: String = config.getString("timer.messages.disable") ?: "<null>"

        startGrace(grace, pvp, warn, warnmsg, enablemsg, disablemsg)
    }

    //grace period loop
    private fun startGrace(grace: Int, pvp: Int, warn: Int, warnmsg: String, enablemsg: String, disablemsg: String) {
        var remainingGrace: Int = grace
        data.pvpBool = false

        object : BukkitRunnable() {
            override fun run() {
                if (remainingGrace > 0) {
                    if (remainingGrace == warn) {
                        Bukkit.broadcastMessage(String.format(warnmsg, warn))
                    }
                    remainingGrace--
                } else {
                    Bukkit.broadcastMessage(enablemsg)
                    startPVP(pvp, disablemsg)
                    cancel()
                }
            }
        }.runTaskTimer(this, 0L, 1200L)
    }

    //pvp period loop
    private fun startPVP(pvp: Int, disablemsg: String) {
        var remainingPVP: Int = pvp
        data.pvpBool = true

        object: BukkitRunnable() {
            override fun run() {
                if (remainingPVP > 0) {
                    remainingPVP--
                } else {
                    Bukkit.broadcastMessage(disablemsg)
                    startGrace(config.getInt("timer.grace"), config.getInt("timer.pvp"), config.getInt("timer.warning"), config.getString("timer.messages.warning") ?: "<null>", config.getString("timer.messages.enable") ?: "<null>", disablemsg)
                    cancel()
                }
            }
        }.runTaskTimer(this, 0L, 1200L)
    }

    private fun manaRegenLoop() {
        object: BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    val manaRegenRate = ceil(((data.playerData["${player.uniqueId}.ManaRegen"] as? Number)?.toDouble()?: 0.0) / 2.0)
                    val maxMana = (data.playerData["${player.uniqueId}.Mana"] as? Number)?.toDouble()?: 0.0
                    var mana = (data.playerData["${player.uniqueId}.CurrentMana"] as? Number)?.toDouble()?: 0.0
                    mana += 1.0 * (1.0 + manaRegenRate)
                    if (mana > maxMana) {
                        mana = maxMana
                    }
                    data.playerData["${player.uniqueId}.CurrentMana"] = mana
                    val actionmsg = "§b✎: ${mana.toInt()}/${maxMana.toInt()}"
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(actionmsg))
                }
            }
        }.runTaskTimer(this, 0L, 10L)
    }

    //load tasks
    override fun onEnable() {
        // Plugin startup logic
        configLoad()

        //player data
        logger.info("Loading player data")
        data.playerData = loadData("plugins/data.txt")
        logger.info("Loaded player data")

        //commands
        logger.info("Loading commands")
        this.getCommand("itemgive")?.setExecutor(cmds(this))
        logger.info("Registered commands: ${Bukkit.getCommandMap().knownCommands.keys}")

        //event listener
        server.pluginManager.registerEvents(listener(), this)
        server.pluginManager.registerEvents(tableGUI(), this)
        logger.info("Registered all event listeners")

        //pvp loop
        object: BukkitRunnable() {
            override fun run() {
                pvpMain()
            }
        }.runTaskAsynchronously(this)
        logger.info("Running pvp loop in the backround")

        //mana regen loop
        object: BukkitRunnable() {
            override fun run() {
                manaRegenLoop()
            }
        }.runTaskAsynchronously(this)
        logger.info("Running mana regeneration loop in the backround")

        logger.info("CSMPSacks successfully loaded")
    }

    //disable task
    override fun onDisable() {
        // Plugin shutdown logic
        saveData("plugins/data.txt", data.playerData)
        logger.info("Saved player data to JSON")
        logger.info("Disabled CSMPSacks")
    }
}
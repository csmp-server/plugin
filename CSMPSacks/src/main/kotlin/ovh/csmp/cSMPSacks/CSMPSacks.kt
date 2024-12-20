package ovh.csmp.cSMPSacks

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import kotlin.math.ceil
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

//main class
class CSMPSacks : JavaPlugin() {

    //load data
    private fun loadData(): MutableMap<String, Any> {
        val dir = File(this.dataFolder, "data")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val playerFile = File(this.dataFolder, "data/playerData.yml")
        val dataMap = mutableMapOf<String, Any>()

        if (playerFile.exists()) {
            val playerConfig: FileConfiguration = YamlConfiguration.loadConfiguration(playerFile)

            for (key in playerConfig.getKeys(false)) {
                dataMap[key] = playerConfig.get(key) ?: continue
            }
        }

        return dataMap
    }

    //save data
    private fun saveData(dataMap: Map<String, Any>) {
        val dir = File(this.dataFolder, "data")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val playerFile = File(this.dataFolder, "data/playerData.yml")
        val playerConfig: FileConfiguration = YamlConfiguration.loadConfiguration(playerFile)

        for ((key, value) in dataMap) {
            playerConfig.set(key, value)
        }

        playerConfig.save(playerFile)
    }


    //config load
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
        data.playerData = loadData()
        logger.info("Loaded player data")

        //commands
        logger.info("Loading commands")
        this.getCommand("itemgive")?.setExecutor(cmds(this))
        logger.info("Registered commands: ${Bukkit.getCommandMap().knownCommands.keys}")

        //event listener
        server.pluginManager.registerEvents(listener(), this)
        server.pluginManager.registerEvents(tableGUI(), this)
        server.pluginManager.registerEvents(mobDrops(), this)
        server.pluginManager.registerEvents(abilityObtain(), this)
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
        saveData(data.playerData)
        logger.info("Saved player data")
        logger.info("Disabled CSMPSacks")
    }
}
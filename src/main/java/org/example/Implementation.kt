package org.example

import com.google.gson.Gson
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.AccessMode
import java.nio.file.DirectoryStream
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileAttribute
import java.util.UUID
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.HostAccess
import org.graalvm.polyglot.HostAccess.Export
import org.graalvm.polyglot.PolyglotAccess
import org.graalvm.polyglot.Value
import org.graalvm.polyglot.io.FileSystem

/**
 * Mediator service for communicating between the Cobblemon Minecraft mod and Cobblemon showdown service via
 * GraalVM. This directly invokes JavaScript functions provided within the showdown service.
 *
 * @see {@code cobbled-exports/cobbled-index.js} within cobbledmon-showdown repository
 * @see <a href="https://www.graalvm.org/">
 * @since February 27, 2023
 * @author Hiroku, landonjw
 */
class Implementation{

    @Transient
    lateinit var context: Context
    @Transient
    lateinit var sendBattleMessageFunction: Value

    @Transient
    val gson = Gson()

     fun openConnection() {
        createContext()
        boot()
    }

    private fun createContext() {
        val wd = Paths.get("./showdown")
        val access = HostAccess.newBuilder(HostAccess.EXPLICIT)
            .allowIterableAccess(true)
            .allowArrayAccess(true)
            .allowListAccess(true)
            .allowMapAccess(true)
            .build()
        context = Context.newBuilder("js")
            .allowIO(true)
            .fileSystem(object : FileSystem
            {
                val default = FileSystem.newDefaultFileSystem()
                override fun parsePath(uri: URI) = default.parsePath(uri)
                override fun parsePath(path: String) = default.parsePath(path)
                override fun createDirectory(dir: Path, vararg attrs: FileAttribute<*>) = default.createDirectory(dir, *attrs)
                override fun delete(path: Path) = default.delete(path)
                override fun newByteChannel(path: Path, options: MutableSet<out OpenOption>, vararg attrs: FileAttribute<*>) = default.newByteChannel(path, options, *attrs)
                override fun newDirectoryStream(dir: Path, filter: DirectoryStream.Filter<in Path>) = default.newDirectoryStream(dir, filter)
                override fun toAbsolutePath(path: Path) = default.toAbsolutePath(path)
                override fun toRealPath(path: Path, vararg linkOptions: LinkOption) = default.toRealPath(path, *linkOptions)
                override fun readAttributes(path: Path, attributes: String, vararg options: LinkOption) = default.readAttributes(path, attributes, *options)
                override fun checkAccess(path: Path, modes: MutableSet<out AccessMode>, vararg linkOptions: LinkOption) {
                    if (!path.toRealPath(LinkOption.NOFOLLOW_LINKS).startsWith(wd.toRealPath(LinkOption.NOFOLLOW_LINKS))) {

                        throw IOException("Someone has put hacked JS files into datapacks because file access is being attempted outside of controlled folders.")
                    }
                }
            }
            )
            .allowExperimentalOptions(true)
            .allowPolyglotAccess(PolyglotAccess.ALL)
            .allowHostAccess(access)
            .allowCreateThread(true)
            .option("engine.WarnInterpreterOnly", "false")
            .option("js.commonjs-require", "true")
            .option("js.commonjs-require-cwd", "showdown")
            .option(
                "js.commonjs-core-modules-replacements",
                "buffer:buffer/,crypto:crypto-browserify,path:path-browserify"
            )
            .allowHostClassLoading(true)
            .allowNativeAccess(true)
            .allowCreateProcess(true)
            .build()

        context.eval("js", """
            globalThis.process = {
                cwd: function() {
                    return '';
                }
            }
        """.trimIndent())
    }

     fun closeConnection() {
        context.close()
    }

    private fun boot() {
        context.eval("js", File("showdown/index.js").readText())
        sendBattleMessageFunction = context.getBindings("js").getMember("sendBattleMessage")
    }


    fun startBattle(battleId: UUID, messages: Array<String>) {
        val startBattleFunction = context.getBindings("js").getMember("startBattle")
        startBattleFunction.execute(this, battleId.toString(), messages)
    }

     fun send(battleId: UUID, messages: Array<String>) {
        sendToShowdown(battleId, messages)
    }
    /*
         fun getAbilityIds(): JsonArray {
            val getCobbledAbilityIdsFn = context.getBindings("js").getMember("getCobbledAbilityIds")
            val arrayResult = getCobbledAbilityIdsFn.execute().asString()
            return gson.fromJson(arrayResult, JsonArray::class.java)
        }

         fun getMoves(): JsonArray {
            val getCobbledMovesFn = context.getBindings("js").getMember("getCobbledMoves")
            val arrayResult = getCobbledMovesFn.execute().asString()
            return gson.fromJson(arrayResult, JsonArray::class.java)
        }

         fun getItemIds(): JsonArray {
            val getCobbledItemIdsFn = context.getBindings("js").getMember("getCobbledItemIds")
            val arrayResult = getCobbledItemIdsFn.execute().asString()
            return gson.fromJson(arrayResult, JsonArray::class.java)
        }

         fun registerSpecies() {
            val receiveSpeciesDataFn = this.context.getBindings("js").getMember("receiveSpeciesData")
            val jsArray = this.context.eval("js", "new Array();")
            var index = 0L
            PokemonSpecies.species.forEach { species ->
                jsArray.setArrayElement(index++, this.gson.toJson(PokemonSpecies.ShowdownSpecies(species, null)))
                species.forms.forEach { form ->
                    if (form != species.standardForm) {
                        jsArray.setArrayElement(index++, this.gson.toJson(PokemonSpecies.ShowdownSpecies(species, form)))
                    }
                }
            }
            receiveSpeciesDataFn.execute(jsArray)
        }

         fun indicateSpeciesInitialized() {
            val afterCobbledSpeciesInitFn = this.context.getBindings("js").getMember("afterCobbledSpeciesInit")
            afterCobbledSpeciesInitFn.execute()
        }

         fun registerBagItems() {
            val receiveBagItemDataFn = this.context.getBindings("js").getMember("receiveBagItemData")
            for ((itemId, js) in BagItems.bagItemsScripts) {
                receiveBagItemDataFn.execute(itemId, js.replace("\n", " "))
            }
        }*/

   fun sendToShowdown(battleId: UUID, messages: Array<String>) {
        sendBattleMessageFunction.execute(this,battleId.toString(), messages)
    }

  /*  fun interpretMessage(battleId: UUID, message: String) {
        // Check key map and use function if matching
        if (message.startsWith("{\"winner\":\"")) {
            // The post-win message is something we don't care about just yet. It's basically a summary of what happened in the battle.
            // Check /docs/example-post-win-message.json for its format.
            return
        }

        val battle = BattleRegistry.getBattle(battleId)

        if (battle == null) {
            LOGGER.info("No battle could be found with the id: $battleId")
            return
        }

        runOnServer {
            battle.showdownMessages.add(message)
            interpret(battle, message)
        }
    }

    fun interpret(battle: PokemonBattle, rawMessage: String) {
        battle.log()
        battle.log(rawMessage)
        battle.log()
        try {
            val lines = rawMessage.split("\n").toMutableList()
            if (lines[0] == "update") {
                lines.removeAt(0)
                while (lines.isNotEmpty()) {
                    val line = lines.removeAt(0)

                    // Split blocks have a public and private message below
                    if (line.startsWith("|split|")) {
                        val showdownId = line.split("|split|")[1]
                        val targetActor = battle.getActor(showdownId)

                        if (targetActor == null) {
                            battle.log("No actor could be found with the showdown id: $showdownId")
                            return
                        }

                        val privateMessage = lines[0]
                        val publicMessage = lines[1]

                        for (instruction in splitUpdateInstructions.entries) {
                            if (lines[0].startsWith(instruction.key)) {
                                instruction.value(battle, targetActor, BattleMessage(publicMessage), BattleMessage(privateMessage))
                                break
                            }
                        }

                        lines.removeFirst()
                        lines.removeFirst()
                    } else {
                        if (line != "|") {
                            val instruction = updateInstructions.entries.find { line.startsWith(it.key) }?.value
                            if (instruction != null) {
                                instruction(battle, BattleMessage(line), lines)
                            } else {
                                battle.dispatch {
                                    battle.broadcastChatMessage(line.text())
                                    GO
                                }
                            }
                        }
                    }
                }
            } else if (lines[0] == "sideupdate") {
                val showdownId = lines[1]
                val targetActor = battle.getActor(showdownId)
                val line = lines[2]

                if (targetActor == null) {
                    battle.log("No actor could be found with the showdown id: $showdownId")
                    return
                }

                for (instruction in sideUpdateInstructions.entries) {
                    if (line.startsWith(instruction.key)) {
                        instruction.value(battle, targetActor, BattleMessage(line))
                    }
                }
            }
        } catch (e: Exception) {
            println(e);
        }
    }*/


    @Export
    fun sendFromShowdown(battleId: String, message: String) {
   //     println("IMPLEMENTATION MESSAGE :"+message);
   //     println("IMPLEMENTATION MESSAGE ENDED");
      ShowdownService.updatePokemonHealth(battleId,message);
      ShowdownService.updatePokemonPP(battleId,message);



    }

  /*  @Export
    fun log(message: String) {
        Cobblemon.LOGGER.info(message)
    }*/
}
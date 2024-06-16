package net.aspw.client.features.command.impl

import net.aspw.client.Client
import net.aspw.client.features.command.Command
import net.aspw.client.utils.render.ColorUtils.stripColor
import net.aspw.client.utils.render.ColorUtils.translateAlternateColorCodes
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.EnumChatFormatting
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

class AddAllCommand : Command("addall", arrayOf("")) {
    override fun execute(arguments: Array<String>) {
        if (arguments.size == 2) {
            val tag = translateAlternateColorCodes(arguments[1])
            val count = AtomicInteger(0)
            val config = Client.fileManager.friendsConfig
            val presistent = arguments[0].contains("")

            mc.thePlayer.sendQueue.playerInfoMap
                .forEach(Consumer<NetworkPlayerInfo> { player: NetworkPlayerInfo ->
                    val team = checkNotNull(player.playerTeam)
                    if (team != null) {
                        if (stripColor(team.colorPrefix)!!
                                .contains(tag)
                            || stripColor(team.colorSuffix)!!
                                .contains(tag)
                        ) {
                            val name = player.gameProfile.name

                            config.addFriend(name, presistent.toString())

                            count.incrementAndGet()
                        }
                    }
                })

            chat("§7Foi adicionado " + EnumChatFormatting.DARK_RED + count.get() + EnumChatFormatting.GRAY + " players.")
            chat(EnumChatFormatting.RED.toString() + "M" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "d" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "b" + EnumChatFormatting.RED + "y" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "s" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "i" + EnumChatFormatting.RED + "p" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "r" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "l" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "k" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "d" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "b" + EnumChatFormatting.RED + "y" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "L" + EnumChatFormatting.RED + "o" + EnumChatFormatting.RED + "p" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "$")

        } else {
            chat("§7Use: -addall <tag>")
        }
    }
}

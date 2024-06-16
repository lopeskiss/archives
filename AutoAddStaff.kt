package net.aspw.client.features.module.impl.exploit

import net.aspw.client.Client
import net.aspw.client.event.EventTarget
import net.aspw.client.event.UpdateEvent
import net.aspw.client.features.module.Module
import net.aspw.client.features.module.ModuleCategory
import net.aspw.client.features.module.ModuleInfo
import net.aspw.client.utils.render.ColorUtils.stripColor
import net.minecraft.util.EnumChatFormatting
import java.util.*

@ModuleInfo(name = "AutoAddStaff", category = ModuleCategory.EXPLOIT)
class AutoAddStaff : Module() {
    private fun isStaff(prefix: String): Boolean {
        return Arrays.asList(*STAFF_PREFIXES).contains(stripColor(prefix))
    }

    @EventTarget
    fun handle(event: UpdateEvent?) {
        val friendManager = Client.fileManager.friendsConfig

        // Loops through every team registered by the server
        for (team in mc.theWorld.scoreboard.teams) {
            // If it's staff
            if (this.isStaff(team.colorPrefix)) {
                // Loops through every member of the team
                for (member in team.membershipCollection) {
                    // If the member isn't a friend
                    if (!friendManager.isFriend(member)) {
                        friendManager.addFriend(member)

                        chat("§6§l Added: " + team.colorPrefix + member)
                        chat(EnumChatFormatting.RED.toString() + "M" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "d" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "b" + EnumChatFormatting.RED + "y" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "s" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "i" + EnumChatFormatting.RED + "p" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "r" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "l" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "a" + EnumChatFormatting.RED + "k" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "d" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "b" + EnumChatFormatting.RED + "y" + EnumChatFormatting.RED + " " + EnumChatFormatting.RED + "L" + EnumChatFormatting.RED + "o" + EnumChatFormatting.RED + "p" + EnumChatFormatting.RED + "e" + EnumChatFormatting.RED + "$")
                    }
                }
            }
        }
    }

    companion object {
        private val STAFF_PREFIXES = arrayOf( // This can and should be made choosable by the user.
            "[Moderador] ",
            "[Supervisor]",
            "[Moderador+] ",
            "[Administrador] ",
            "[Admin] ",
            "[Coordenador] ",
            "[Coord] ",
            "[Gerente] ",
            "[CEO] ",
            "[Dono] ",
            "[Diretor] ",
            "[Master] "
        )
    }
}

package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.Client
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.impl.combat.KillAura
import net.ccbluex.liquidbounce.utils.timer.ArmorUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.item.ItemArmor
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C16PacketClientStatus

@ModuleInfo(name = "ArmorFilter", category = ModuleCategory.EXPLOIT)
class ArmorFilter : Module() {

    private val percentage = FloatValue("percentage", 25f, 0f, 100f, "%")
    private val delay = FloatValue("delay", 100f, 1f, 3000f, "ms")
    private val safe = BoolValue("killaura", true)

    private val timer = ArmorUtils()


    @EventTarget
    fun handle(event: UpdateEvent?) {
        if (timer.delay(delay.value)) {
            timer.reset()

            val killAura = checkNotNull(
                Client.moduleManager.getModule(
                    KillAura::class.java
                )
            )
            val state = killAura.state && safe.get()

            for (slot in 9..44) {
                val item = mc.thePlayer.inventoryContainer.getSlot(slot).stack

                if (item != null && item.item is ItemArmor) {
                    val durability = (item.maxDamage - item.itemDamage).toFloat()
                    val percentage = durability / item.maxDamage

                    if (percentage < this.percentage.get() / 100.0f) {
                        if (state) {
                            killAura.onToggle(true)
                        }

                        val open = mc.currentScreen !is GuiInventory

                        if (open) {
                            mc.thePlayer.sendQueue.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
                        }

                        mc.playerController.windowClick(
                            mc.thePlayer.inventoryContainer.windowId,
                            slot, 1, 4,
                            mc.thePlayer
                        )

                        if (open) {
                            mc.thePlayer.sendQueue.addToSendQueue(C0DPacketCloseWindow())
                        }

                        if (state) {
                            killAura.onToggle(true)
                        }

                        return
                    }
                }
            }
        }
    }

    val suffix: String
        get() = percentage.value.toString() + "%"
}

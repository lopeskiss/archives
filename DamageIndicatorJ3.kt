package net.aspw.client.visual.hud.element.elements

import net.aspw.client.Client
import net.aspw.client.features.module.impl.combat.KillAura
import net.aspw.client.visual.hud.designer.GuiHudDesigner
import net.aspw.client.visual.hud.element.Border
import net.aspw.client.visual.hud.element.ElementInfo
import net.aspw.client.visual.hud.element.Element
import net.aspw.client.visual.font.Fonts
import net.aspw.client.utils.render.RenderUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumChatFormatting
import kotlin.math.abs

@ElementInfo("DamageIndicatorJ3")
class DamageIndicatorJ3 : Element() {

    private var target: EntityPlayer? = null
    private var progress: Float = 0F
    private var easingHealth: Float = 0F
    private var lastTarget: Entity? = null

    override fun drawElement(): Border {

        val kaTarget = (Client.moduleManager[KillAura::class.java] as KillAura).target
        val actualTarget = if (mc.currentScreen is GuiHudDesigner) mc.thePlayer
        else if (kaTarget is EntityPlayer) kaTarget else null

        progress += 0.0025F * RenderUtils.deltaTime * if (actualTarget != null) -1F else 1F

        if (progress < 0F)
            progress = 0F
        else if (progress > 1F)
            progress = 1F

        if (actualTarget == null) {
            if (progress >= 1F && target != null)
                target = null
        } else
            target = actualTarget

        if (target != null) {
            val convertedTarget = target!! as EntityPlayer

            if (convertedTarget != lastTarget || easingHealth < 0 || easingHealth > convertedTarget.maxHealth ||
                abs(easingHealth - convertedTarget.health) < 0.01
            ) {
                easingHealth = convertedTarget.health
            }

            Fonts.minecraftFont.drawStringWithShadow(
                "[" + convertedTarget.name + "]",
                36F,
                3F,
                0xffffff
            )
            Fonts.minecraftFont.drawStringWithShadow(
                "" + convertedTarget.health.toInt() + " §4\u2764" + EnumChatFormatting.RESET + "/" + convertedTarget.maxHealth.toInt() + " §4\u2764",
                38F,
                12F,
                0xffffff
            )
        }

        lastTarget = target
        return getTBorder()
    }

    private fun getTBorder(): Border = Border(0F, 0F, 90F, 36F)
}

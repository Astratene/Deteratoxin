package org.teamvoided.deteratoxin.config

import me.fzzyhmstrs.fzzy_config.annotations.NonSync
import me.fzzyhmstrs.fzzy_config.config.Config
import org.teamvoided.deteratoxin.Deteratoxin.MODID
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.util.isDev

class DeteratoxinConfig : Config(id(MODID)) {

    var drinkingDeteratoxinKillsYou = false

    var enableDurability = true

    @NonSync
    var tooltipRendersEffects = TooltipType.NEVER

    fun canRenderTooltip(isCreative: Boolean): Boolean {
        return when (tooltipRendersEffects) {
            TooltipType.NEVER -> isDev()
            TooltipType.IN_CREATIVE -> isCreative
            TooltipType.ALWAYS -> true
        }
    }

    enum class TooltipType { NEVER, IN_CREATIVE, ALWAYS }

}
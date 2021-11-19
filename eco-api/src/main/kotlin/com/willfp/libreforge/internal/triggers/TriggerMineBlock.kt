package com.willfp.libreforge.internal.triggers

import com.willfp.eco.core.integrations.antigrief.AntigriefManager
import com.willfp.eco.core.integrations.mcmmo.McmmoManager
import com.willfp.eco.util.NumberUtils
import com.willfp.libreforge.api.events.EffectActivateEvent
import com.willfp.libreforge.api.getHolders
import com.willfp.libreforge.api.triggers.Trigger
import com.willfp.libreforge.api.triggers.TriggerData
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class TriggerMineBlock : Trigger("mine_block") {
    @EventHandler(ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        if (McmmoManager.isFake(event)) {
            return
        }

        val player = event.player
        val block = event.block
        if (!AntigriefManager.canBreakBlock(player, block)) {
            return
        }

        for (holder in player.getHolders()) {
            for ((effect, config, filter, triggers) in holder.effects) {
                if (NumberUtils.randFloat(0.0, 100.0) > (config.getDoubleOrNull("chance") ?: 100.0)) {
                    continue
                }

                if (!triggers.contains(this)) {
                    continue
                }

                if (!filter.matches(block)) {
                    continue
                }

                val aEvent = EffectActivateEvent(player, holder, effect)
                this.plugin.server.pluginManager.callEvent(aEvent)
                if (!aEvent.isCancelled) {
                    effect.handle(
                        TriggerData(
                            player = player,
                            block = block
                        ), config
                    )
                }
            }
        }
    }
}
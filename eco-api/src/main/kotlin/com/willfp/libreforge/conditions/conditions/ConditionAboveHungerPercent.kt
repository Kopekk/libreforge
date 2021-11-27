package com.willfp.libreforge.conditions.conditions

import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.libreforge.conditions.Condition
import com.willfp.libreforge.updateEffects
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.FoodLevelChangeEvent


class ConditionAboveHungerPercent : Condition("above_hunger_percent") {
    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    fun handle(event: FoodLevelChangeEvent) {
        val player = event.entity

        if (player !is Player) {
            return
        }

        player.updateEffects()
    }

    override fun isConditionMet(player: Player, config: JSONConfig): Boolean {
        return (player.foodLevel / 20) * 100 >= config.getDouble("percent")
    }

    override fun validateConfig(config: JSONConfig): List<com.willfp.libreforge.ConfigViolation> {
        val violations = mutableListOf<com.willfp.libreforge.ConfigViolation>()

        config.getDoubleOrNull("percent")
            ?: violations.add(
                com.willfp.libreforge.ConfigViolation(
                    "percent",
                    "You must specify the hunger percentage!"
                )
            )

        return violations
    }
}
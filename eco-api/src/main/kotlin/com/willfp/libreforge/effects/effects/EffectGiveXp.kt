package com.willfp.libreforge.effects.effects

import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.config.interfaces.JSONConfig
import com.willfp.eco.core.integrations.economy.EconomyManager
import com.willfp.libreforge.ConfigViolation
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.triggers.TriggerData
import com.willfp.libreforge.triggers.TriggerParameter
import com.willfp.libreforge.triggers.Triggers

class EffectGiveXp : Effect(
    "give_xp",
    supportsFilters = true,
    applicableTriggers = Triggers.withParameters(
        TriggerParameter.PLAYER
    )
) {
    override fun handle(data: TriggerData, config: JSONConfig) {
        val player = data.player ?: return

        if (Prerequisite.HAS_PAPER.isMet) {
            player.giveExp(config.getInt("amount"), config.getBool("apply_mending"))
        } else {
            player.giveExp(config.getInt("amount"))
        }
    }

    override fun validateConfig(config: JSONConfig): List<ConfigViolation> {
        val violations = mutableListOf<ConfigViolation>()

        config.getIntOrNull("amount")
            ?: violations.add(
                ConfigViolation(
                    "amount",
                    "You must specify the amount of xp to give!"
                )
            )

        if (Prerequisite.HAS_PAPER.isMet) {
            config.getBoolOrNull("apply_mending")
                ?: violations.add(
                    ConfigViolation(
                        "apply_mending",
                        "You must specify whether or not to apply mending!"
                    )
                )
        }

        return violations
    }
}
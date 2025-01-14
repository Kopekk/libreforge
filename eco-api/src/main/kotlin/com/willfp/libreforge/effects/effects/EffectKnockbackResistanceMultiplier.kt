package com.willfp.libreforge.effects.effects

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.libreforge.ConfigViolation
import com.willfp.libreforge.effects.Effect
import com.willfp.libreforge.effects.Identifiers
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player

class EffectKnockbackResistanceMultiplier : Effect("knockback_resistance_multiplier") {
    override fun handleEnable(
        player: Player,
        config: Config,
        identifiers: Identifiers
    ) {
        val attribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) ?: return
        val uuid = identifiers.uuid
        attribute.removeModifier(AttributeModifier(uuid, this.id, 0.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1))
        attribute.addModifier(
            AttributeModifier(
                uuid,
                this.id,
                config.getDoubleFromExpression("multiplier", player) - 1,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun handleDisable(
        player: Player,
        identifiers: Identifiers
    ) {
        val attribute = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) ?: return
        attribute.removeModifier(
            AttributeModifier(
                identifiers.uuid,
                this.id,
                0.0,
                AttributeModifier.Operation.MULTIPLY_SCALAR_1
            )
        )
    }

    override fun validateConfig(config: Config): List<ConfigViolation> {
        val violations = mutableListOf<ConfigViolation>()

        if (!config.has("multiplier")) violations.add(
            ConfigViolation(
                "multiplier",
                "You must specify the knockback resistance multiplier!"
            )
        )

        return violations
    }
}
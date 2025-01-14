package com.willfp.libreforge.triggers.wrappers

import com.willfp.libreforge.triggers.WrappedCancellableEvent
import org.bukkit.event.entity.EntityDamageEvent

open class WrappedDamageEvent(
    private val event: EntityDamageEvent
) : WrappedCancellableEvent<EntityDamageEvent> {
    var damage: Double
        get() = event.damage
        set(value) {
            event.damage = value
        }

    override var isCancelled: Boolean
        get() {
            return event.isCancelled
        }
        set(value) {
            event.isCancelled = value
        }
}

package org.bigcraft.mobmoney.drop

import com.google.inject.Inject
import com.google.inject.Singleton
import me.wyne.storm.api.StormApi
import org.bigcraft.mobmoney.MobMoney

@Singleton
class StormEffects @Inject constructor() {

    init {
        StormApi.getEffectRegistry().register(MultiplierStormEffect::class.java, MULTIPLIER_EFFECT_KEY)
        MobMoney.logger.info("Registered '{}' Storm effect", MULTIPLIER_EFFECT_KEY)
    }

}

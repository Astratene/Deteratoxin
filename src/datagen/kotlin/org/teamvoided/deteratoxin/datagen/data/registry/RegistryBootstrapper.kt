package org.teamvoided.deteratoxin.datagen.data.registry

import net.minecraft.data.worldgen.BootstrapContext

// this will become a Devin thing later on
interface RegistryBootstrapper<T> {

    fun bootstrap(context: BootstrapContext<T>) = context.init()

    fun BootstrapContext<T>.init()

}
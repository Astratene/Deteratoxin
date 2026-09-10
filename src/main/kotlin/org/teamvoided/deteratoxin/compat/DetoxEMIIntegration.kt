package org.teamvoided.deteratoxin.compat

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.recipe.EmiBrewingRecipe
import net.minecraft.world.item.Items
import org.teamvoided.deteratoxin.Deteratoxin.id
import org.teamvoided.deteratoxin.init.DeteratoxinItems

@EmiEntrypoint
object DetoxEMIIntegration : EmiPlugin {

    override fun register(registry: EmiRegistry) {
        val detox = EmiStack.of(DeteratoxinItems.DETERATOXIN_EXTRACT)
        val lily = EmiStack.of(Items.LILY_OF_THE_VALLEY)
        val potent = EmiStack.of(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT)

        registry.addRecipe(EmiBrewingRecipe(detox, lily, potent, id("/brewing/potent_deteratoxin_extract")))
    }

}
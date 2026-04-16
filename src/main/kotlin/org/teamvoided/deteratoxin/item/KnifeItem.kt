package org.teamvoided.deteratoxin.item


import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.level.Level
import org.spongepowered.asm.util.Locals
import org.teamvoided.deteratoxin.components.KnifeData
import org.teamvoided.deteratoxin.init.DeteratoxinDataComponents


// KnifeItem is this class, and TieredItem is the class that this class *extends*. Extending a class lets you take the properties of that class
// And override different parts. Adding and replacing stuff that has already been done in other classes is the core of
// Functional/class based programming languages like java and kotlin.

// You can ctrl + click 'TieredItem' to go to that class.
class KnifeItem(settings: Properties) : TieredItem(Tiers.NETHERITE, settings) {

    // in order to override a function, you simply put 'override' before it.
    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand
    ): InteractionResultHolder<ItemStack?>? {

        val data = player.getItemInHand(interactionHand) .getOrDefault(DeteratoxinDataComponents.KNIFE_DATA,
            KnifeData.DEFAULT)
        if (data.readied){

        }
        else{
            player.getItemInHand(interactionHand) .set(DeteratoxinDataComponents.KNIFE_DATA, KnifeData(true, 100))
        }


        // TODO
        // Marking something as 'to do' lets you be notified of it while committing. I'm going to mark things as
        // 'to do' when I want you to work on stuff in that section.

        // The use function lets you add/replace something that happens when the item is right-clicked.
        // If you want a 'continuous' use function you add 'use ticks' and use a different function.
        // lmk if that's what you want.

        // What I want you to do in this section is to look at the code tutorial I put in 'SimpleStorageComponent.kt'
        // add an if function that checks if the knife is 'readied', and makes it ready if it isn't.
        // Let me know if you need help!

        // Check 'DeteratoxinComponents.kt' for the component itself. I set that up for you :3


        // This is useful for if you want things in previous classes to still happen.
        return super.use(level, player, interactionHand)
    }
}
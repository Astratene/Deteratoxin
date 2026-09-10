package org.teamvoided.deteratoxin.mixin.brewing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.teamvoided.deteratoxin.init.DeteratoxinItems;

@Mixin(targets = "net/minecraft/world/inventory/BrewingStandMenu$PotionSlot")
public class PotionSlotMixin {

    @ModifyReturnValue(method = "mayPlaceItem", at = @At("RETURN"))
    private static boolean allowDetox(boolean original, ItemStack stack) {
        return original || (stack.is(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT) || stack.is(DeteratoxinItems.DETERATOXIN_EXTRACT));
    }

}

package org.teamvoided.deteratoxin.mixin.brewing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamvoided.deteratoxin.init.DeteratoxinItems;


@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @ModifyReturnValue(method = "isIngredient", at = @At("RETURN"))
    private boolean inputCheck(boolean original, ItemStack stack) {
        return original || stack.is(Items.LILY_OF_THE_VALLEY);
    }

    @Inject(method = "mix", at = @At("HEAD"), cancellable = true)
    private void mixItems(ItemStack ingredient, ItemStack potion, CallbackInfoReturnable<ItemStack> info) {
        if (ingredient.is(Items.LILY_OF_THE_VALLEY) && potion.is(DeteratoxinItems.DETERATOXIN_EXTRACT)) {
            info.setReturnValue(DeteratoxinItems.POTENT_DETERATOXIN_EXTRACT.getDefaultInstance());
        }
    }

    @ModifyReturnValue(method = "hasMix", at = @At("RETURN"))
    private boolean canMix(boolean original, ItemStack potion, ItemStack ingredient) {
        return original || (ingredient.is(Items.LILY_OF_THE_VALLEY) && potion.is(DeteratoxinItems.DETERATOXIN_EXTRACT));
    }

}
package de.gerrygames.blackbarconcealer.mixin;

import de.gerrygames.blackbarconcealer.access.IMixinHttpTexture;
import de.gerrygames.blackbarconcealer.config.BBCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class MixinPlayerSkin {

	@Shadow public abstract ResourceLocation texture();

	@Inject(method = "model", at = @At("RETURN"), cancellable = true)
	public void onGetModel(CallbackInfoReturnable<PlayerSkin.Model> cir) {
		AbstractTexture skinTexture = Minecraft.getInstance().getTextureManager().getTexture(texture());
		if (!(skinTexture instanceof IMixinHttpTexture mixinHttpTexture)) return;

		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (!config.enabled()) return;

		boolean preferWideModel = config.preferWideModel();
		boolean fillPixels = config.fillPixels();

		if (preferWideModel) cir.setReturnValue(PlayerSkin.Model.WIDE);

		if (cir.getReturnValue() == PlayerSkin.Model.WIDE && !fillPixels && !mixinHttpTexture.blackBarConcealer$hasThiccArms()) {
			cir.setReturnValue(PlayerSkin.Model.SLIM);
		}
	}
}

package de.gerrygames.blackbarconcealer.mixin;

import de.gerrygames.blackbarconcealer.config.BBCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class MixinPlayerSkin {

	@Shadow @Final private ResourceLocation texture;

	@Inject(method = "texture", at = @At("RETURN"), cancellable = true)
	public void onGetTexture(CallbackInfoReturnable<ResourceLocation> cir) {
		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (config.enabled() && config.fillPixels() && !hasThiccArms()) {
			cir.setReturnValue(ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), texture.getPath().replace("thin/", "thicc/")));
		}
	}

	@Inject(method = "model", at = @At("RETURN"), cancellable = true)
	public void onGetModel(CallbackInfoReturnable<PlayerSkin.Model> cir) {
		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (!config.enabled()) return;

		boolean preferWideModel = config.preferWideModel();
		boolean fillPixels = config.fillPixels();

		if (preferWideModel) cir.setReturnValue(PlayerSkin.Model.WIDE);

		if (cir.getReturnValue() == PlayerSkin.Model.WIDE && !fillPixels && !hasThiccArms()) {
			cir.setReturnValue(PlayerSkin.Model.SLIM);
		}
	}

	@Unique
	private boolean hasThiccArms() {
		return texture.getPath().startsWith("thicc");
	}
}

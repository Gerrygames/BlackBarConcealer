package de.gerrygames.blackbarconcealer.mixin;

import de.gerrygames.blackbarconcealer.config.BBCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerSkin.class)
public abstract class MixinPlayerSkin {

	@Shadow @Final private ClientAsset.Texture body;

	@Inject(method = "body", at = @At("RETURN"), cancellable = true)
	public void onGetBodyTexture(CallbackInfoReturnable<ClientAsset.Texture> cir) {
		if (!(body instanceof ClientAsset.DownloadedTexture downloadedTexture)) return;

		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (config.enabled() && config.fillPixels() && !hasThiccArms()) {
			Identifier texturePath = body.texturePath().withPath(path -> path.replace("thin/", "thicc/"));
			cir.setReturnValue(new ClientAsset.DownloadedTexture(texturePath, downloadedTexture.url()));
		}
	}

	@Inject(method = "model", at = @At("RETURN"), cancellable = true)
	public void onGetModel(CallbackInfoReturnable<PlayerModelType> cir) {
		if (!(body instanceof ClientAsset.DownloadedTexture)) return;

		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (!config.enabled()) return;

		boolean preferWideModel = config.preferWideModel();
		boolean fillPixels = config.fillPixels();

		if (preferWideModel) cir.setReturnValue(PlayerModelType.WIDE);

		if (cir.getReturnValue() == PlayerModelType.WIDE && !fillPixels && !hasThiccArms()) {
			cir.setReturnValue(PlayerModelType.SLIM);
		}
	}

	@Unique
	private boolean hasThiccArms() {
		return body.texturePath().getPath().startsWith("thicc");
	}
}

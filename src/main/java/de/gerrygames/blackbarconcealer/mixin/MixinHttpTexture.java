package de.gerrygames.blackbarconcealer.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import de.gerrygames.blackbarconcealer.access.IMixinHttpTexture;
import de.gerrygames.blackbarconcealer.config.BBCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Mixin(HttpTexture.class)
public abstract class MixinHttpTexture implements IMixinHttpTexture {
	@Unique private static final Rect2i[] ARM_DIFF = {
			new Rect2i(50, 16, 2, 4),
			new Rect2i(54, 20, 2, 12),
			new Rect2i(42, 48, 2, 4),
			new Rect2i(46, 52, 2, 12),
	};

	@Shadow @Final private static Logger LOGGER;

	@Shadow @Final private boolean processLegacySkin;
	@Shadow @Nullable private CompletableFuture<?> future;

	@Shadow public abstract void load(ResourceManager resourceManager) throws IOException;

	@Unique private boolean hasThiccArms = false;

	@Inject(method = "loadCallback", at = @At("HEAD"))
	public void onLoadCallback(NativeImage skin, CallbackInfo ci) {
		if (!processLegacySkin) return;

		for (Rect2i armDiff : ARM_DIFF) {
			int xo = armDiff.getX();
			int yo = armDiff.getY();
			for (int x = armDiff.getWidth() - 1; x >= 0; x--) {
				for (int y = armDiff.getHeight() - 1; y >= 0; y--) {
					int rgba = skin.getPixelRGBA(xo + x, yo + y);
					if ((rgba & 0xFFFFFF) != 0 && (rgba & 0xFF000000) != 0) {
						hasThiccArms = true;
						return;
					}
				}
			}
		}

		BBCConfig config = AutoConfig.getConfigHolder(BBCConfig.class).get();
		if (config.enabled() && config.fillPixels()) {
			convertAlexToSteve(skin);
		}
	}

	@Unique
	private static void convertAlexToSteve(NativeImage skin) {
		//skin.copyRect(originX, originY, offsetX, offsetY, width, height, flipX, flipY);

		// Right hand - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(48, 16, 2, 0, 2, 4, false, false);
		skin.copyRect(48, 16, 1, 0, 1, 4, false, false);
		skin.copyRect(47, 16, 1, 0, 1, 4, false, false);

		// Left hand - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(40, 48, 2, 0, 2, 4, false, false);
		skin.copyRect(40, 48, 1, 0, 1, 4, false, false);
		skin.copyRect(39, 48, 1, 0, 1, 4, false, false);

		// Right shoulder - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(46, 16, 1, 0, 1, 4, false, false);
		skin.copyRect(45, 16, 1, 0, 1, 4, false, false);

		// Left shoulder - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(38, 48, 1, 0, 1, 4, false, false);
		skin.copyRect(37, 48, 1, 0, 1, 4, false, false);

		// Back side of left arm - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(52, 20, 2, 0, 2, 12, false, false);
		skin.copyRect(52, 20, 1, 0, 1, 12, false, false);
		skin.copyRect(51, 20, 1, 0, 1, 12, false, false);

		// Right side of left arm - copied to steve postion
		skin.copyRect(50, 20, 1, 0, 1, 12, false, false);
		skin.copyRect(49, 20, 1, 0, 1, 12, false, false);
		skin.copyRect(48, 20, 1, 0, 1, 12, false, false);
		skin.copyRect(47, 20, 1, 0, 1, 12, false, false);

		// Front side of left arm - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(46, 20, 1, 0, 1, 12, false, false);
		skin.copyRect(45, 20, 1, 0, 1, 12, false, false);

		// Back side of right arm - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(44, 52, 2, 0, 2, 12, false, false);
		skin.copyRect(44, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(43, 52, 1, 0, 1, 12, false, false);

		// Left side of right arm - copied to steve postion
		skin.copyRect(42, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(41, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(40, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(39, 52, 1, 0, 1, 12, false, false);

		// Front side of right arm - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(38, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(37, 52, 1, 0, 1, 12, false, false);


		// Right glove - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(48, 32, 2, 0, 2, 4, false, false);
		skin.copyRect(48, 32, 1, 0, 1, 4, false, false);
		skin.copyRect(47, 32, 1, 0, 1, 4, false, false);

		// Left glove - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(56, 48, 2, 0, 2, 4, false, false);
		skin.copyRect(56, 48, 1, 0, 1, 4, false, false);
		skin.copyRect(55, 48, 1, 0, 1, 4, false, false);

		// Right shoulder overlay - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(46, 32, 1, 0, 1, 4, false, false);
		skin.copyRect(45, 32, 1, 0, 1, 4, false, false);

		// Left shoulder overlay - copied to steve postion and extended from 3x4 to 4x4
		skin.copyRect(54, 48, 1, 0, 1, 4, false, false);
		skin.copyRect(53, 48, 1, 0, 1, 4, false, false);

		// Back side of left arm sleeve - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(52, 36, 2, 0, 2, 12, false, false);
		skin.copyRect(52, 36, 1, 0, 1, 12, false, false);
		skin.copyRect(51, 36, 1, 0, 1, 12, false, false);

		// Right side of left arm sleeve - copied to steve postion
		skin.copyRect(50, 36, 1, 0, 1, 12, false, false);
		skin.copyRect(49, 36, 1, 0, 1, 12, false, false);
		skin.copyRect(48, 36, 1, 0, 1, 12, false, false);
		skin.copyRect(47, 36, 1, 0, 1, 12, false, false);

		// Front side of left arm sleeve - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(46, 36, 1, 0, 1, 12, false, false);
		skin.copyRect(45, 36, 1, 0, 1, 12, false, false);

		// Back side of right arm sleeve - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(60, 52, 2, 0, 2, 12, false, false);
		skin.copyRect(60, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(59, 52, 1, 0, 1, 12, false, false);

		// Left side of right arm sleeve - copied to steve postion
		skin.copyRect(58, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(57, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(56, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(55, 52, 1, 0, 1, 12, false, false);

		// Front side of right arm sleeve - copied to steve postion and extended from 3x12 to 4x12
		skin.copyRect(54, 52, 1, 0, 1, 12, false, false);
		skin.copyRect(53, 52, 1, 0, 1, 12, false, false);
	}

	@Override
	public boolean blackBarConcealer$hasThiccArms() {
		return hasThiccArms;
	}

	@Override
	public void blackBarConcealer$reload() {
		if (!processLegacySkin || hasThiccArms) return;

		if (future != null) future.cancel(true);
		future = null;

		try {
			load(Minecraft.getInstance().getResourceManager());
		} catch (IOException ex) {
			LOGGER.error("Error reload texture:", ex);
		}
	}
}

package de.gerrygames.blackbarconcealer;

import com.mojang.blaze3d.platform.InputConstants;
import de.gerrygames.blackbarconcealer.config.BBCConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import org.lwjgl.glfw.GLFW;

public class BlackBarConcealer implements ModInitializer {

	@Override
	public void onInitialize() {
		ConfigHolder<BBCConfig> configHolder = AutoConfig.register(BBCConfig.class, GsonConfigSerializer::new);

		KeyMapping toggleKeyMapping = new KeyMapping("key.blackbarconcealer.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, KeyMapping.CATEGORY_MISC);
		KeyBindingHelper.registerKeyBinding(toggleKeyMapping);

		ClientTickEvents.END_CLIENT_TICK.register(e -> {
			while (toggleKeyMapping.consumeClick()) {
				configHolder.setConfig(configHolder.getConfig().toggle());
				configHolder.save();
				Minecraft.getInstance().gui.setOverlayMessage(configHolder.get().enabled() ?
						Component.translatable("text.blackbarconcealer.toggled.on").withColor(CommonColors.GREEN) :
						Component.translatable("text.blackbarconcealer.toggled.off").withColor(CommonColors.SOFT_RED),
						false);
			}
		});
	}
}

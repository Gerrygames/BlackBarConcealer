package de.gerrygames.blackbarconcealer.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "blackbarconcealer")
@Accessors(fluent = true)
@Getter
@With
@AllArgsConstructor
@NoArgsConstructor
public class BBCConfig implements ConfigData {
	private boolean enabled = true;
	private boolean fillPixels = false;
	private boolean preferWideModel = false;

	public BBCConfig toggle() {
		return withEnabled(!enabled);
	}
}

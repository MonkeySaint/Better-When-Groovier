package jamdoggie.musicmod;

import jamdoggie.musicmod.mixins.ConfigHandlerAccessor;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;

import java.util.Properties;

public class ConfigScreen extends Screen {
	public ConfigScreen(Screen parent) {
		super(parent);
	}

	@Override
	public void init() {
		buttons.add(new ButtonElement(0, (this.width - 200) / 2, (this.height - 20) / 2, "Excursions: " + (MusicMod.config.getBoolean("excursions_enabled") ? "Enabled" : "Disabled")));
		buttons.add(new ButtonElement(1, (this.width - 200) / 2, (this.height - 20) / 2 + 20 + 6, "Done"));
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		renderBackground();
		super.render(mx, my, partialTick);
	}

	@Override
	protected void buttonClicked(ButtonElement button) {
		if (button.id == 0) {
			Properties newProps = new Properties();
			boolean newVal = !MusicMod.config.getBoolean("excursions_enabled");
			newProps.setProperty("excursions_enabled", String.valueOf(newVal));
			((ConfigHandlerAccessor) MusicMod.config).setProperties(newProps);
			MusicMod.config.updateConfig();
			button.displayString = "Excursions: " + (newVal ? "Enabled" : "Disabled");
		}
		else if (button.id == 1) {
			mc.displayScreen(parentScreen);
		}
	}
}

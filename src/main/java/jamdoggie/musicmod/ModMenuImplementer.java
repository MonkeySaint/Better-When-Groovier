package jamdoggie.musicmod;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.Screen;
import org.lwjgl.Sys;

import java.util.function.Function;

public class ModMenuImplementer implements ModMenuApi {
	@Override
	public String getModId() {
		return MusicMod.MOD_ID;
	}

	@Override
	public Function<Screen, ? extends Screen> getConfigScreenFactory() {
		return (ConfigScreen::new);
	}
}

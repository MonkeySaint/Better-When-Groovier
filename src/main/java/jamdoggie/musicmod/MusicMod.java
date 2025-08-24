package jamdoggie.musicmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.NamedSoundRepository;
import net.minecraft.client.sound.SoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.io.File;
import java.util.Properties;


public class MusicMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint, ClientModInitializer
{
    public static final String MOD_ID = "musicmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigHandler config;

	static {
		Properties properties = new Properties();
		properties.setProperty("excursions_enabled", "true");
		config = new ConfigHandler(MOD_ID, properties);
		config.updateConfig();
	}

    @Override
    public void onInitialize() {
        LOGGER.info("Better when Groovy initialized.");

    }

	@Override
	public void beforeGameStart() {
		SoundRepository.registerNamespace(MOD_ID);
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void onInitializeClient() {

	}
}

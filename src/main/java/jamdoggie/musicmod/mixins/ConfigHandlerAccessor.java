package jamdoggie.musicmod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import turniplabs.halplibe.util.ConfigHandler;

import java.util.Properties;

@Mixin(ConfigHandler.class)
public interface ConfigHandlerAccessor {
	@Accessor
	public void setProperties(Properties properties);
}

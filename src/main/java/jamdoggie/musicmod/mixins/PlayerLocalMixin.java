package jamdoggie.musicmod.mixins;

import jamdoggie.musicmod.mixininterface.ISoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerLocal.class, remap = false)
public class PlayerLocalMixin {
	@Shadow
	protected Minecraft mc;

	@Inject(method = "setGamemode", at = @At("HEAD"))
	private void setGamemode(Gamemode gamemode, CallbackInfo ci) {
		ISoundManager sndManager = ((ISoundManager)this.mc.sndManager);

		sndManager.setTicksUntilMusic(600);
		sndManager.stopBgMusic();
	}
}

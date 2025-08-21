package jamdoggie.musicmod.mixins;

import jamdoggie.musicmod.mixininterface.ISoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	@Shadow
	public SoundEngine sndManager;

	@Inject(method = "changeWorld(Lnet/minecraft/client/world/WorldClient;Ljava/lang/String;Lnet/minecraft/core/entity/player/Player;)V",
		at = @At("HEAD"))
	private void changeWorld(WorldClient world, String loadingTitle, Player player, CallbackInfo ci) {
		((ISoundManager)sndManager).stopBgMusic();
		((ISoundManager)sndManager).setTicksUntilMusic(600);
	}
}

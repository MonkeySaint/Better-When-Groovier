package jamdoggie.musicmod.mixins;

import jamdoggie.musicmod.mixininterface.ISoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.*;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.season.Seasons;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.SoundSystem;

import java.util.Random;
import java.util.concurrent.locks.Lock;

import static jamdoggie.musicmod.MusicMod.LOGGER;

@Mixin(value = SoundEngine.class, remap = false)
public abstract class SoundEngineMixin implements ISoundManager {
	@Shadow
	public int ticksBeforeMusic;
	@Shadow
	public Minecraft mc;
	@Shadow
	private static boolean loaded;
	@Shadow
	private static @Nullable SoundSystem soundSystem;
	@Shadow
	private @Nullable GameSettings options;
	@Shadow
	@Final
	private Random random;

	@Shadow
	private static Lock lock;

	@Shadow
	protected abstract boolean isLoaded();

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(CallbackInfo ci) {
		ticksBeforeMusic = 0;
	}

	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	private void tick(CallbackInfo ci) {
		ticksBeforeMusic = 0;
		try {
			lock.lock();
			if (!this.isLoaded() || SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options) == 0.0F) {
				ci.cancel();
				return;
			}

			if (soundSystem.playing("BgMusic") || soundSystem.playing("Streaming")) {
				ci.cancel();
				return;
			}

			if (this.ticksBeforeMusic > 0) {
				this.ticksBeforeMusic--;
				ci.cancel();
				return;
			}

			SoundEvent event = null;
			if (mc.currentWorld.dimension == Dimension.NETHER) {
				event = SoundRepository.SOUNDS.getSoundEvent("musicmod:nether");
			}
			else {
				if (mc.thePlayer.gamemode == Gamemode.creative)
					event = SoundRepository.SOUNDS.getSoundEvent("musicmod:creative");
				else if (this.mc.thePlayer != null && !this.mc
					.thePlayer
					.world
					.canBlockSeeTheSky(MathHelper.floor(this.mc.thePlayer.x), MathHelper.floor(this.mc.thePlayer.y), MathHelper.floor(this.mc.thePlayer.z))) {
					event = SoundRepository.SOUNDS.getSoundEvent("ambient.cave");
				}
				else if (random.nextInt(2) == 1) {
					if (mc.currentWorld.seasonManager.getCurrentSeason() == Seasons.OVERWORLD_FALL)
						event = SoundRepository.SOUNDS.getSoundEvent("musicmod:fall");
					else if (mc.currentWorld.seasonManager.getCurrentSeason() == Seasons.OVERWORLD_WINTER) {
						if (!mc.currentWorld.isDaytime() && random.nextInt(2) == 1) {
							event = SoundRepository.SOUNDS.getSoundEvent("musicmod:winter_night");
						} else event = SoundRepository.SOUNDS.getSoundEvent("musicmod:winter");
					}
				}

				else event = SoundRepository.SOUNDS.getRandomSoundFromCategory("music.");
			}

			if (event != null) {
				SoundEntry entry = event.getRandomEntry();
				if (entry != null) {
					this.ticksBeforeMusic = this.random.nextInt(6000) + 6000;
					soundSystem.backgroundMusic("BgMusic", entry.getURL(), entry.name, false);
					soundSystem.setPitch("BgMusic", entry.pitch);
					soundSystem.setVolume("BgMusic", SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options) * entry.volume);
					soundSystem.play("BgMusic");
				}

				ci.cancel();
			}
		} catch (Exception var6) {
			LOGGER.error("Unexpected exception while ticking sound engine!", var6);
			ci.cancel();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void playTitleScreenMusic() {
		if (!loaded || soundSystem == null || SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options) == 0.0f) {
			return;
		}

		if (soundSystem.playing("BgMusic") || soundSystem.playing("Streaming")) {
			return;
		}

		SoundEvent event = SoundRepository.SOUNDS.getSoundEvent("musicmod:title");

		if (event != null) {
			System.out.println("Playing title screen music");
			SoundEntry entry = event.getRandomEntry();
			if (entry != null) {
				this.ticksBeforeMusic = random.nextInt(6000) + 6000;
				soundSystem.backgroundMusic("BgMusic", entry.getURL(), entry.name, false);
				soundSystem.setPitch("BgMusic", entry.pitch);
				soundSystem.setVolume("BgMusic", SoundCategoryHelper.getEffectiveVolume(SoundCategory.MUSIC, this.options) * entry.volume);
				soundSystem.play("BgMusic");
			}
		}
		else {
			System.out.println("No title screen music found");
			System.out.println(SoundRepository.namespaces);
			System.out.println(SoundRepository.allKeys);
		}
	}

	@Override
	public void stopBgMusic() {
		soundSystem.fadeOut("BgMusic", null, 2000);
	}

	@Override
	public void setTicksUntilMusic(int ticks) {
		ticksBeforeMusic = ticks;
	}
}

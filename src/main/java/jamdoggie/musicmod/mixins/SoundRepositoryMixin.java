package jamdoggie.musicmod.mixins;

import jamdoggie.musicmod.MusicMod;
import net.minecraft.client.sound.NamedSoundRepository;
import net.minecraft.client.sound.SoundEvent;
import net.minecraft.client.sound.SoundRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(SoundRepository.class)
public abstract class SoundRepositoryMixin {
	@Inject(method = "getRandomSoundFromCategory", at = @At("HEAD"), cancellable = true, remap = false)
	public void getRandomSoundFromCategory(String category, CallbackInfoReturnable<SoundEvent> cir) {
		List<SoundEvent> eventList = new ArrayList();
		if (!category.equals("music.")) return;
		for (NamedSoundRepository nsr : (SoundRepository) (Object) this) {
			nsr.iterator().forEachRemaining(soundEvent -> {
				if (soundEvent.getEventID().startsWith(category) || soundEvent.getEventID().startsWith(MusicMod.MOD_ID + ":" + category)) {
					eventList.add(soundEvent);
				} else if (MusicMod.config.getBoolean("excursions_enabled")) {
					if (soundEvent.getEventID().equals(MusicMod.MOD_ID + ":" + "excursions") || soundEvent.getEventID().equals("excursions")) {
						eventList.add(soundEvent);
					}
				}
			});
		}

		if (!eventList.isEmpty()) {
			cir.setReturnValue(eventList.get(new Random().nextInt(eventList.size())));
		}
	}
}

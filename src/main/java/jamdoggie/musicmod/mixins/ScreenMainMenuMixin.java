package jamdoggie.musicmod.mixins;

import jamdoggie.musicmod.mixininterface.ISoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenMainMenu;
import net.minecraft.core.lang.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ScreenMainMenu.class, remap = false)
public abstract class ScreenMainMenuMixin extends Screen {
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();
		ISoundManager mixinSndManager = (ISoundManager) mc.sndManager;

		if (mc.currentScreen instanceof ScreenMainMenu) {
			mixinSndManager.playTitleScreenMusic();
		}
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void drawScreen(int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
		I18n i18n = I18n.getInstance();

		String c418Disclaimer = i18n.translateKey("musicmod.titledisclaimer");
		this.drawString(this.font, c418Disclaimer, this.width - this.font.getStringWidth(c418Disclaimer) - 2, this.height - 30, 0xFFFFFF);
	}
}

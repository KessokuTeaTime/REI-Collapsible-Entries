package net.krlite.reicollapsibleentries.mixin;

import net.krlite.reicollapsibleentries.client.gui.screen.ConfirmTagScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow @Nullable protected MinecraftClient client;

    @Inject(
            method = "handleTextClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/ClickEvent;getAction()Lnet/minecraft/text/ClickEvent$Action;",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void handleTagClick(Style style, CallbackInfoReturnable<Boolean> cir) {
        ClickEvent clickEvent = Objects.requireNonNull(style.getClickEvent());
        Identifier tag = Identifier.tryParse(clickEvent.getValue());

        if (clickEvent.getAction() == ClickEvent.Action.OPEN_URL && tag != null) {
            // On a tag
            if (client != null) {
                client.setScreen(new ConfirmTagScreen(tag));
                cir.setReturnValue(true);
            }
        }
    }
}

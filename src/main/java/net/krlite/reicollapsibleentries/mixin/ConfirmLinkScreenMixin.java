package net.krlite.reicollapsibleentries.mixin;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.krlite.reicollapsibleentries.REICollapsibleEntries;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfirmLinkScreen.class)
public class ConfirmLinkScreenMixin {
    @Unique
    private @Nullable Identifier tag = null;

    @Inject(
            method = "<init>(Lit/unimi/dsi/fastutil/booleans/BooleanConsumer;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/lang/String;Lnet/minecraft/text/Text;Z)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmScreen;<init>(Lit/unimi/dsi/fastutil/booleans/BooleanConsumer;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void checkTag(BooleanConsumer booleanConsumer, Text text, Text text2, String string, Text text3, boolean bl, CallbackInfo ci) {
        tag = Identifier.tryParse(string);
    }

    @Redirect(
            method = "<init>(Lit/unimi/dsi/fastutil/booleans/BooleanConsumer;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/lang/String;Lnet/minecraft/text/Text;Z)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmLinkScreen;yesText:Lnet/minecraft/text/Text;"
            )
    )
    private void setYesText(ConfirmLinkScreen instance, Text value) {
        if (tag == null) return;

        boolean exist = REICollapsibleEntries.CONFIG.customTags.contains(tag.toString());
        instance.yesText = exist ? Text.translatable("command.reicollapsibleentries.tags.remove") : Text.translatable("command.reicollapsibleentries.tags.add");
    }

    @Redirect(
            method = "<init>(Lit/unimi/dsi/fastutil/booleans/BooleanConsumer;Lnet/minecraft/text/Text;Lnet/minecraft/text/Text;Ljava/lang/String;Lnet/minecraft/text/Text;Z)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/screen/ConfirmLinkScreen;noText:Lnet/minecraft/text/Text;"
            )
    )
    private void setNoText(ConfirmLinkScreen instance, Text value) {
        if (tag == null) return;

        instance.noText = ScreenTexts.CANCEL;
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
            ),
            index = 1
    )
    private Text drawTagWarning(Text text) {
        if (tag == null) {
            // As is
            return text;
        } else {
            return REICollapsibleEntries.paintIdentifier(tag);
        }
    }

    @Inject(
            method = "open",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void operateTag(String string, Screen screen, boolean bl, CallbackInfo ci) {
        if (Identifier.isValid(string)) {
            // Add or remove tag
            boolean exist = REICollapsibleEntries.CONFIG.customTags.contains(string);

            if (exist) {
                REICollapsibleEntries.CONFIG.customTags.remove(string);
            } else {
                REICollapsibleEntries.CONFIG.customTags.add(string);
            }

            REICollapsibleEntries.CONFIG_HOLDER.save();
            ci.cancel();
        }
    }
}

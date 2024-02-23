package net.krlite.reicollapsibleentries.client.gui.screen;

import net.krlite.reicollapsibleentries.REICollapsibleEntries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ConfirmTagScreen extends ConfirmScreen {
    private static @Nullable Identifier tag = null;

    public ConfirmTagScreen(Identifier tag) {
        super(
                ConfirmTagScreen::confirmTag,
                Text.translatable("command.reicollapsibleentries.tags.title"),
                REICollapsibleEntries.paintIdentifier(tag),
                ConfirmTagScreen.exists(tag) ? Text.translatable("command.reicollapsibleentries.tags.remove") : Text.translatable("command.reicollapsibleentries.tags.add"),
                ConfirmLinkScreen.COPY
        );
        ConfirmTagScreen.tag = tag;
    }

    public static boolean exists(Identifier tag) {
        return REICollapsibleEntries.CONFIG.customTags.contains(tag.toString());
    }

    public static void confirmTag(boolean notCopy) {
        if (ConfirmTagScreen.tag == null) return;

        String string = ConfirmTagScreen.tag.toString();
        if (notCopy) {
            if (exists(ConfirmTagScreen.tag)) {
                REICollapsibleEntries.CONFIG.customTags.remove(string);
            } else {
                REICollapsibleEntries.CONFIG.customTags.add(string);
            }

            REICollapsibleEntries.CONFIG_HOLDER.save();
        } else {
            MinecraftClient.getInstance().keyboard.setClipboard(string);
        }

        MinecraftClient.getInstance().setScreen(null);
    }
}

package band.kessokuteatime.reicollapsibleentries.client.gui.screen;

import band.kessokuteatime.reicollapsibleentries.REICollapsibleEntries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfirmTagScreen extends ConfirmScreen {
    private static @Nullable Identifier tag = null;

    public ConfirmTagScreen(@NotNull Identifier tag) {
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
        return REICollapsibleEntries.CONFIG.get().customTags.contains(tag.toString());
    }

    public static void confirmTag(boolean notCopy) {
        if (ConfirmTagScreen.tag == null) return;

        String string = ConfirmTagScreen.tag.toString();
        if (notCopy) {
            if (exists(ConfirmTagScreen.tag)) {
                REICollapsibleEntries.CONFIG.get().customTags.remove(string);
            } else {
                REICollapsibleEntries.CONFIG.get().customTags.add(string);
            }

            REICollapsibleEntries.CONFIG.save();
        } else {
            MinecraftClient.getInstance().keyboard.setClipboard(string);
        }

        MinecraftClient.getInstance().setScreen(null);
    }
}

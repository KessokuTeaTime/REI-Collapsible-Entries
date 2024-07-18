package band.kessokuteatime.reicollapsibleentries.config.modmenu.widgets;

import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TagListEntry extends TextFieldListEntry<String> {
    protected final Text deleteText;
    protected final Text restoreText;
    protected final Text inspectText;

    protected final Consumer<String> deleteCallback;
    protected final Consumer<String> restoreCallback;
    protected final Consumer<String> inspectCallback;

    protected final ButtonWidget deleteButton;
    protected final ButtonWidget restoreButton;
    protected final ButtonWidget inspectButton;

    protected final Function<String, Boolean> activeSupplier;

    protected TagListEntry(Text fieldName, String original, Text deleteText, Text restoreText, Text inspectText, Supplier<String> defaultValue, Consumer<String> saveConsumer, Consumer<String> deleteConsumer, Consumer<String> restoreConsumer, Consumer<String> inspectConsumer, Function<String, Boolean> activeSupplier) {
        this(fieldName, original, deleteText, restoreText, inspectText, defaultValue, saveConsumer, deleteConsumer, restoreConsumer, inspectConsumer, activeSupplier, false);
    }

    protected TagListEntry(Text fieldName, String original, Text deleteText, Text restoreText, Text inspectText, Supplier<String> defaultValue, Consumer<String> saveConsumer, Consumer<String> deleteConsumer, Consumer<String> restoreConsumer, Consumer<String> inspectConsumer, Function<String, Boolean> activeSupplier, boolean requiresRestart) {
        super(fieldName, original, Text.empty(), defaultValue, null, requiresRestart);
        this.saveCallback = saveConsumer;
        this.deleteText = deleteText;
        this.restoreText = restoreText;
        this.inspectText = inspectText;
        this.deleteCallback = deleteConsumer;
        this.restoreCallback = restoreConsumer;
        this.inspectCallback = inspectConsumer;
        this.activeSupplier = activeSupplier;
        
        this.deleteButton = ButtonWidget.builder(deleteText, (widget) -> deleteConsumer.accept(original))
                .dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(deleteText) + 6, 20)
                .build();
        this.restoreButton = ButtonWidget.builder(restoreText, (widget) -> restoreConsumer.accept(original))
                .dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(restoreText) + 6, 20)
                .build();
        this.inspectButton = ButtonWidget.builder(inspectText, (widget) -> inspectConsumer.accept(original))
                .dimensions(0, 0, MinecraftClient.getInstance().textRenderer.getWidth(inspectText) + 6, 20)
                .build();
        this.widgets = Lists.newArrayList(this.textFieldWidget, this.inspectButton, this.deleteButton, this.restoreButton);
    }

    @Override
    public String getValue() {
        return textFieldWidget.getText();
    }

    @Override
    public void render(DrawContext graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        //super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        Window window = MinecraftClient.getInstance().getWindow();

        boolean isActive = this.activeSupplier.apply(original);
        ButtonWidget actionButton = isActive ? this.deleteButton : this.restoreButton;

        actionButton.setY(y);
        this.inspectButton.setY(y);

        this.textFieldWidget.setEditable(this.isEditable() && isActive);
        this.textFieldWidget.setY(y + 1);

        Text displayedFieldName = isActive ? this.getDisplayedFieldName() : this.getDisplayedFieldName().copy().formatted(Formatting.STRIKETHROUGH, Formatting.ITALIC);

        if (MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), window.getScaledWidth() - x - MinecraftClient.getInstance().textRenderer.getWidth(displayedFieldName), y + 6, this.getPreferredTextColor());
            actionButton.setX(x);
            this.inspectButton.setX(x + actionButton.getWidth());
            this.textFieldWidget.setX(x + actionButton.getWidth() + this.inspectButton.getWidth());
        } else {
            graphics.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayedFieldName.asOrderedText(), x, y + 6, this.getPreferredTextColor());
            actionButton.setX(x + entryWidth - actionButton.getWidth());
            this.inspectButton.setX(x + entryWidth - actionButton.getWidth() - this.inspectButton.getWidth());
            this.textFieldWidget.setX(x + entryWidth - 148);
        }

        setTextFieldWidth(this.textFieldWidget, 148 - actionButton.getWidth() - this.inspectButton.getWidth() - 4);
        actionButton.render(graphics, mouseX, mouseY, delta);
        this.inspectButton.render(graphics, mouseX, mouseY, delta);
        this.textFieldWidget.render(graphics, mouseX, mouseY, delta);
    }
}

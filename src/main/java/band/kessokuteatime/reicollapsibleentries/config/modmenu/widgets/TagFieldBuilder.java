package band.kessokuteatime.reicollapsibleentries.config.modmenu.widgets;

import band.kessokuteatime.reicollapsibleentries.core.ModEntry;
import com.mojang.datafixers.types.Func;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TagFieldBuilder extends AbstractFieldBuilder<String, TagListEntry, TagFieldBuilder> {
    protected Text deleteText;
    protected Text restoreText;
    protected Text inspectText;

    protected Consumer<String> deleteCallback;
    protected Consumer<String> restoreCallback;
    protected Consumer<String> inspectCallback;

    protected Function<String, Boolean> activeSupplier;

    public TagFieldBuilder(Text fieldName, String value, Text deleteText, Text restoreText, Text inspectText, Consumer<String> deleteConsumer, Consumer<String> restoreConsumer, Consumer<String> inspectConsumer, Function<String, Boolean> activeSupplier) {
        super(Text.empty(), fieldName);
        this.value = value;
        this.deleteText = deleteText;
        this.restoreText = restoreText;
        this.inspectText = inspectText;
        this.deleteCallback = deleteConsumer;
        this.restoreCallback = restoreConsumer;
        this.inspectCallback = inspectConsumer;
        this.activeSupplier = activeSupplier;
    }

    public TagFieldBuilder(Text fieldName, String value) {
        this(fieldName, value, ModEntry.THIS.name("config", "text", "delete"), ModEntry.THIS.name("config", "text", "restore"), ModEntry.THIS.name("config", "text", "inspect"), string -> {}, string -> {}, string -> {}, string -> true);
    }

    public TagFieldBuilder setErrorSupplier(Function<String, Optional<Text>> errorSupplier) {
        return super.setErrorSupplier(errorSupplier);
    }

    public TagFieldBuilder requireRestart() {
        return super.requireRestart();
    }

    public TagFieldBuilder setSaveConsumer(Consumer<String> saveConsumer) {
        return super.setSaveConsumer(saveConsumer);
    }

    public TagFieldBuilder setDeleteText(Text deleteText) {
        this.deleteText = deleteText;
        return this;
    }

    public TagFieldBuilder setRestoreText(Text restoreText) {
        this.restoreText = restoreText;
        return this;
    }

    public TagFieldBuilder setInspectText(Text inspectText) {
        this.inspectText = inspectText;
        return this;
    }

    public TagFieldBuilder setDeleteConsumer(Consumer<String> deleteConsumer) {
        this.deleteCallback = deleteConsumer;
        return this;
    }

    public TagFieldBuilder setRestoreConsumer(Consumer<String> restoreConsumer) {
        this.restoreCallback = restoreConsumer;
        return this;
    }

    public TagFieldBuilder setInspectConsumer(Consumer<String> inspectConsumer) {
        this.inspectCallback = inspectConsumer;
        return this;
    }

    public TagFieldBuilder setActiveSupplier(Function<String, Boolean> activeSupplier) {
        this.activeSupplier = activeSupplier;
        return this;
    }

    public TagFieldBuilder setTooltipSupplier(Function<String, Optional<Text[]>> tooltipSupplier) {
        return super.setTooltipSupplier(tooltipSupplier);
    }

    public TagFieldBuilder setTooltipSupplier(Supplier<Optional<Text[]>> tooltipSupplier) {
        return super.setTooltipSupplier(tooltipSupplier);
    }

    public TagFieldBuilder setTooltip(Optional<Text[]> tooltip) {
        return super.setTooltip(tooltip);
    }

    public TagFieldBuilder setTooltip(Text... tooltip) {
        return super.setTooltip(tooltip);
    }

    public @NotNull TagListEntry build() {
        TagListEntry entry = new TagListEntry(this.getFieldNameKey(), this.value, this.deleteText, this.restoreText, this.inspectText, this.defaultValue, this.getSaveConsumer(), this.deleteCallback, this.restoreCallback, this.inspectCallback, this.activeSupplier, this.isRequireRestart());
        entry.setTooltipSupplier(() -> this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> this.errorSupplier.apply(entry.getValue()));
        }

        return this.finishBuilding(entry);
    }
}

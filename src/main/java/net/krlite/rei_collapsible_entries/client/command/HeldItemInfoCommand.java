package net.krlite.rei_collapsible_entries.client.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.krlite.rei_collapsible_entries.REICollapsibleEntries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class HeldItemInfoCommand implements Command<FabricClientCommandSource> {
	@Override
	@SuppressWarnings("all")
	public int run(CommandContext<FabricClientCommandSource> context) {
		ItemStack stack = context.getSource().getPlayer().getMainHandStack();
		Identifier itemId = Registries.ITEM.getId(stack.getItem());

		if (stack.isOf(Items.AIR))
			return 0;

		// Tags
		if (stack.getItem().getRegistryEntry().streamTags().findAny().isPresent())
			context.getSource().sendFeedback(streamTags(stack));

		return SINGLE_SUCCESS;
	}

	@SuppressWarnings("deprecation")
	private Text streamTags(ItemStack stack) {
		return stack.getItem().getRegistryEntry().streamTags()
				.map(tag -> Text.translatable("command." + REICollapsibleEntries.ID + ".held_item_info.tag_prefix")
						.formatted(Formatting.GRAY, Formatting.ITALIC)
						.append(Text.literal(tag.id().toString())
								.styled(style -> style
										.withColor(Formatting.YELLOW)
										.withHoverEvent(new HoverEvent(
												HoverEvent.Action.SHOW_TEXT,
												Text.literal(tag.id().toString()).formatted(Formatting.YELLOW)
										))
										.withClickEvent(new ClickEvent(
												ClickEvent.Action.COPY_TO_CLIPBOARD,
												tag.id().toString())
										))
						))
				.reduce((a, b) -> a.append(" ").append(b)).orElse(Text.empty());
	}
}

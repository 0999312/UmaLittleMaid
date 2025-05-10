package net.tracen.uma_maid.utils;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;

public interface IPreviousItemHandler {
	  /**
	   * Sets a {@link ItemStack} to the given slot index as the previous stack, for comparison purposes
	   * with the current stack.
	   *
	   * @param stack The {@link ItemStack} to assign as the previous stack
	   */
	  void setPreviousStack(@Nonnull ItemStack stack);

	  /**
	   * Gets the {@link ItemStack} assigned as the previous stack
	   *
	   * @return The {@link ItemStack} assigned as the previous stack
	   */
	  ItemStack getPreviousStack();
}

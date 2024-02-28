package io.github.mceventhorizon.eventslootbags.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mceventhorizon.eventslootbags.EventsLootbags;
import io.github.mceventhorizon.eventslootbags.menu.BagOpenerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BagOpenerMenuScreen extends AbstractContainerScreen<BagOpenerMenu> {

  private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(EventsLootbags.MODID, "textures/gui/bag_opener_gui.png");

  public BagOpenerMenuScreen(BagOpenerMenu pMenu,
      Inventory pPlayerInventory,
      Component pTitle) {
    super(pMenu, pPlayerInventory, pTitle);
    this.imageWidth = 176;
    this.imageHeight = 166;
  }

  @Override
  protected void init() {
    super.init();
    this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
  }

  @Override
  protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    RenderSystem.setShaderTexture(0, GUI_TEXTURE);

    pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
    renderProgressArrow(pGuiGraphics);
  }

  private void renderProgressArrow(GuiGraphics guiGraphics) {
    if (menu.isCrafting()) {
      guiGraphics.blit(GUI_TEXTURE, leftPos + 49, topPos + 35, 176, 0, menu.getScaledProgress(), 15);
    }
  }

  @Override
  public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    renderTooltip(pGuiGraphics, pMouseX, pMouseY);
  }
}

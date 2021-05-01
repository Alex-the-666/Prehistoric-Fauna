package superlord.prehistoricfauna.entity.render;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.entity.tile.PaleoscribeContainer;
import superlord.prehistoricfauna.entity.tile.PaleoscribeTileEntity;
import superlord.prehistoricfauna.init.ItemInit;
import superlord.prehistoricfauna.util.EnumPaleoPages;

@OnlyIn(Dist.CLIENT)
public class PaleoscribeScreen extends ContainerScreen<PaleoscribeContainer> {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation(PrehistoricFauna.MODID, "textures/gui/container/paleoscribe.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation(PrehistoricFauna.MODID, "textures/models/paleoscribe_book.png");
    private static final BookModel MODEL_BOOK = new BookModel();
    private final PlayerInventory playerInventory;
    private final Random random = new Random();
    private final PaleoscribeContainer container;
    private final ITextComponent nameable;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private int flapTimer = 0;

    public PaleoscribeScreen(PaleoscribeContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.container = container;
        this.nameable = name;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.nameable.getFormattedText(), 12, 4, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    public void tick() {
        super.tick();
        this.container.onUpdate();
        this.tickBook();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (int k = 0; k < 3; ++k) {
            double l = mouseX - (i + 60);
            double i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(minecraft.player, k)) {
                flapTimer = 5;
                this.minecraft.playerController.sendEnchantPacket(this.container.windowId, k);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderHelper.setupGuiFlatDiffuseLighting();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize);
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        int k = (int) this.minecraft.getMainWindow().getGuiScaleFactor();
        RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
        RenderSystem.translatef(-0.34F, 0.23F, 0.0F);
        RenderSystem.multMatrix(Matrix4f.perspective(90.0D, 1.3333334F, 9.0F, 80.0F));
        RenderSystem.matrixMode(5888);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.push();
        MatrixStack.Entry matrixstack$entry = matrixstack.getLast();
        matrixstack$entry.getMatrix().setIdentity();
        matrixstack$entry.getNormal().setIdentity();
        matrixstack.translate(0.0D, 3.3F, 1984.0D);
        matrixstack.scale(5.0F, 5.0F, 5.0F);
        matrixstack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        matrixstack.rotate(Vector3f.XP.rotationDegrees(20.0F));
        float f1 = MathHelper.lerp(partialTicks, this.oOpen, this.open);
        matrixstack.translate((1.0F - f1) * 0.2F, (1.0F - f1) * 0.1F, (1.0F - f1) * 0.25F);
        float f2 = -(1.0F - f1) * 90.0F - 90.0F;
        matrixstack.rotate(Vector3f.YP.rotationDegrees(f2));
        matrixstack.rotate(Vector3f.XP.rotationDegrees(180.0F));
        float f3 = MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.25F;
        float f4 = MathHelper.lerp(partialTicks, this.oFlip, this.flip) + 0.75F;
        f3 = (f3 - (float) MathHelper.fastFloor(f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float) MathHelper.fastFloor(f4)) * 1.6F - 0.3F;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        RenderSystem.enableRescaleNormal();
        MODEL_BOOK.setBookState(0, f3, f4, f1);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(MODEL_BOOK.getRenderType(ENCHANTMENT_TABLE_BOOK_TEXTURE));
        MODEL_BOOK.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        irendertypebuffer$impl.finish();
        matrixstack.pop();
        RenderSystem.matrixMode(5889);
        RenderSystem.viewport(0, 0, this.minecraft.getMainWindow().getFramebufferWidth(), this.minecraft.getMainWindow().getFramebufferHeight());
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(5888);
        RenderHelper.setupGui3DDiffuseLighting();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < 3; ++i1) {
            int j1 = i + 60;
            int k1 = j1 + 20;
            this.setBlitOffset(0);
            this.minecraft.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int l1 = this.container.getPossiblePages()[i1] == null ? -1 : this.container.getPossiblePages()[i1].ordinal();//enchantment level
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (l1 == -1) {
                this.blit(j1, j + 14 + 19 * i1, 0, 185, 108, 19);
            } else {
                String s = "" + 3;
                FontRenderer fontrenderer = this.font;
                String s1 = "";
                float textScale = 1.0F;
                EnumPaleoPages enchantment = this.container.getPossiblePages()[i1];
                if (enchantment != null) {
                    s1 = I18n.format("paleopedia." + enchantment.toString().toLowerCase());//EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1);
                    if (fontrenderer.getStringWidth(s1) > 80) {
                        textScale = 1.0F - (fontrenderer.getStringWidth(s1) - 80) * 0.01F;
                    }
                }
                int j2 = 6839882;
                if (PrehistoricFauna.PROXY.getReferencedTE() instanceof PaleoscribeTileEntity) {
                    if (container.getSlot(0).getStack().getItem() == ItemInit.PALEOPEDIA.get()) { // Forge: render buttons as disabled when enchantable but enchantability not met on lower levels
                        int k2 = mouseX - (i + 60);
                        int l2 = mouseY - (j + 14 + 19 * i1);
                        int j3 = 0X9F988C;
                        if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                            this.blit(j1, j + 14 + 19 * i1, 0, 204, 108, 19);
                            j2 = 16777088;
                            j3 = 16777088;
                        } else {
                            this.blit(j1, j + 14 + 19 * i1, 0, 166, 108, 19);
                        }

                        this.blit(j1 + 1, j + 15 + 19 * i1, 16 * i1, 223, 16, 16);
                        RenderSystem.pushMatrix();
                        RenderSystem.translatef(width / 2F - 10, height / 2F - 83 + (1.0F - textScale) * 55, 2);
                        RenderSystem.scalef(textScale, textScale, 1);
                        fontrenderer.drawString(s1, 0, 20 + 19 * i1, j2);
                        RenderSystem.popMatrix();
                        fontrenderer = this.minecraft.fontRenderer;
                        fontrenderer.drawStringWithShadow(s, (float) (k1 + 84 - fontrenderer.getStringWidth(s)), (float) (j + 13 + 19 * i1 + 7), j3);
                    } else {
                        this.blit(j1, j + 14 + 19 * i1, 0, 185, 108, 19);
                        this.blit(j1 + 1, j + 15 + 19 * i1, 16 * i1, 239, 16, 16);
                        j2 = 4226832;
                    }
                }
            }
        }
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.minecraft.getRenderPartialTicks();
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        boolean flag = this.minecraft.player.isCreative();
        int i = this.container.getPaleopageAmount();

        for (int j = 0; j < 3; ++j) {
            int k = 1;
            EnumPaleoPages enchantment = this.container.getPossiblePages()[j];
            int i1 = 3;

            if (this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0) {
                List<String> list = Lists.newArrayList();

                if (enchantment == null) {
                    list.add(TextFormatting.RED + I18n.format("container.paleoscribe.no_paleojournal"));
                } else if (!flag) {
                    list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format(enchantment == null ? "" : "paleopedia." + enchantment.name().toLowerCase()));
                    TextFormatting textformatting = i >= i1 ? TextFormatting.GRAY : TextFormatting.RED;
                    list.add(textformatting + "" + I18n.format("container.paleoscribe.costs"));
                    String s = I18n.format("container.paleoscribe.paleopage.many", i1);
                    list.add(textformatting + "" + s);
                }

                this.renderTooltip(list, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook() {
        ItemStack itemstack = this.container.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, this.last)) {
            this.last = itemstack;

            while (true) {
                this.flipT += (float) (this.random.nextInt(4) - this.random.nextInt(4));

                if (this.flip > this.flipT + 1.0F || this.flip < this.flipT - 1.0F) {
                    break;
                }
            }
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i) {
            if (this.container.getPossiblePages()[i] != null) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        if(flapTimer > 0){
            f1 = (ticks + this.minecraft.getRenderPartialTicks()) * 0.5F;
            flapTimer--;
        }
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

}
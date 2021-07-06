package fuzs.visualworkbench.mixin;

import fuzs.visualworkbench.block.IWorkbenchTileEntityProvider;
import fuzs.visualworkbench.element.VisualWorkbenchElement;
import fuzs.visualworkbench.tileentity.WorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"deprecation", "unused"})
@Mixin(CraftingTableBlock.class)
public abstract class CraftingTableBlockMixin extends Block implements ITileEntityProvider, IWorkbenchTileEntityProvider {

    private int hasWorkbenchTileEntity = -1;

    public CraftingTableBlockMixin(Properties p_i48440_1_) {

        super(p_i48440_1_);
    }

    @Override
    public boolean hasWorkbenchTileEntity() {

        if (this.hasWorkbenchTileEntity == -1) {

            try {

                TileEntity tileEntity = this.createTileEntity(null, null);
                this.hasWorkbenchTileEntity = tileEntity instanceof WorkbenchTileEntity ? 1 : 0;
            } catch (NullPointerException ignored) {

                // method must be overridden, most likely due to an own tile entity, so we don't do anything
                this.hasWorkbenchTileEntity = 0;
            }
        }

        if (this.hasWorkbenchTileEntity == 1) {

            try {

                return !this.is(VisualWorkbenchElement.NON_VISUAL_WORKBENCHES_TAG);
            } catch (IllegalStateException ignored) {

                // tag will throw an exception when not fetched yet before a world is loaded
                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {

        return new WorkbenchTileEntity();
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {

        if (!this.hasWorkbenchTileEntity()) {

            super.setPlacedBy(world, pos, state, placer, stack);
            return;
        }

        if (stack.hasCustomHoverName()) {

            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof WorkbenchTileEntity) {

                ((WorkbenchTileEntity) tileentity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

        if (!this.hasWorkbenchTileEntity()) {

            super.onRemove(state, world, pos, newState, isMoving);
            return;
        }

        if (!state.is(newState.getBlock())) {

            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof WorkbenchTileEntity) {

                InventoryHelper.dropContents(world, pos, (WorkbenchTileEntity) tileentity);
            }

            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean triggerEvent(BlockState state, World world, BlockPos pos, int id, int param) {

        if (!this.hasWorkbenchTileEntity()) {

            return super.triggerEvent(state, world, pos, id, param);
        }

        super.triggerEvent(state, world, pos, id, param);
        TileEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit, CallbackInfoReturnable<ActionResultType> callbackInfo) {

        if (this.hasWorkbenchTileEntity()) {

            if (world.isClientSide) {

                callbackInfo.setReturnValue(ActionResultType.SUCCESS);
            } else {

                TileEntity tileentity = world.getBlockEntity(pos);
                if (tileentity instanceof WorkbenchTileEntity) {

                    player.openMenu((WorkbenchTileEntity) tileentity);
                    player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                }

                callbackInfo.setReturnValue(ActionResultType.CONSUME);
            }
        }
    }

    @Inject(method = "getMenuProvider", at = @At("HEAD"), cancellable = true)
    public void getMenuProvider(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<INamedContainerProvider> callbackInfo) {

        if (this.hasWorkbenchTileEntity()) {

            TileEntity tileentity = world.getBlockEntity(pos);
            callbackInfo.setReturnValue(tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null);
        }
    }

}

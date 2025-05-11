package net.tracen.uma_maid.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockCubePerFace;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceItem;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.FaceUVsItem;

import cn.mcmod_mmf.mmlib.utils.MathUtil;
import net.minecraft.core.Direction;

@Mixin(value = BedrockCubePerFace.class, priority = 10)
public class BedrockCubePreFaceMixin {
    @Inject(method = "fillUV", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void fillUVwhenNull(Direction direction, FaceUVsItem faces, float texWidth, float texHeight, CallbackInfo ci) {
        FaceItem face = faces.getFace(direction);
        
        if(face == null) {
        	ci.cancel();
        	return;
        }
        
        if (MathUtil.equalFloat(face.getUvSize()[0], 0.0F) && MathUtil.equalFloat(face.getUvSize()[1], 0.0F)){
        	ci.cancel();
        	return;
        }
    }
}

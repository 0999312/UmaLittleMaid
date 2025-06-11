package net.tracen.uma_maid.client;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.GlWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Mob;

import java.util.List;

public class UmamusumeTLMModel extends BedrockModel<Mob> {
    private final BedrockPart head;
    private final List<BedrockPart> longHairParts;

    public UmamusumeTLMModel(BedrockModelPOJO pojo) {
        super(pojo, BedrockVersion.isLegacyVersion(pojo) ? BedrockVersion.LEGACY : BedrockVersion.NEW);

        this.getModelMap().put("armLeft", this.getModelMap().getOrDefault("left_arm", new BedrockPart()));
        this.getModelMap().put("legLeft", this.getModelMap().getOrDefault("left_leg", new BedrockPart()));
        this.getModelMap().put("legRight", this.getModelMap().getOrDefault("right_leg", new BedrockPart()));
        this.getModelMap().put("armRight", this.getModelMap().getOrDefault("right_arm", new BedrockPart()));

        this.getModelMap().put("left_arm", new BedrockPart());
        this.getModelMap().put("left_leg", new BedrockPart());
        this.getModelMap().put("right_leg", new BedrockPart());
        this.getModelMap().put("right_arm", new BedrockPart());

        this.head = this.getModelMap().getOrDefault("head", new BedrockPart());

        if (this.getModelMap().get("hat") != null) {
            this.head.addChild(this.getModelMap().get("hat"));
            this.shouldRender.remove(this.getModelMap().get("hat"));
        }

        this.modelMap.forEach((key, model) -> modelMapWrapper.put(key, new ModelRendererWrapper(model)));

        this.longHairParts = Lists.newArrayList();
        this.getModelMap().forEach((name, part) -> {
            if (name.startsWith("long_hair_") || "long_hair".equals(name)) {
                this.longHairParts.add(part);
            }
        });

        var backpackPos = new BedrockPart();
        backpackPos.setPos(0, 1, 0);
        this.getModelMap().put("backpackPositioningBone", backpackPos);

        this.setRotationAngle(this.getModelMap().get("armRight"), 0, 0, convertRotation(12.5F));
        this.setRotationAngle(this.getModelMap().get("armLeft"), 0, 0, convertRotation(-12.5F));
    }

    @Override
    public void setupAnim(Mob entityIn, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(entityIn, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        if (this.head.xRot < 0) {
            this.longHairParts.forEach(part -> part.xRot = -this.head.xRot);
        } else {
            this.longHairParts.forEach(part -> part.xRot = 0F);
        }
        if (entityIn.isSleeping()) {
            GlWrapper.translate(0, 0.25F, 0);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.scale(0.85F, 0.85F, 0.85F);
        poseStack.translate(0, 0.25, 0);
        super.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

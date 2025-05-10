package net.tracen.uma_maid.client;

import com.github.tartaricacid.simplebedrockmodel.client.bedrock.model.BedrockPart;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockModelPOJO;
import com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;

import net.minecraft.world.entity.Mob;

public class UmamusumeTLMModel extends BedrockModel<Mob> {
	public UmamusumeTLMModel(BedrockModelPOJO pojo) {
		super(pojo, BedrockVersion.isLegacyVersion(pojo) ? BedrockVersion.LEGACY: BedrockVersion.NEW);
		this.getModelMap().put("armLeft", this.getModelMap().get("left_arm"));
		this.getModelMap().put("legLeft", this.getModelMap().get("left_leg"));
		this.getModelMap().put("legRight", this.getModelMap().get("right_leg"));
		this.getModelMap().put("armRight", this.getModelMap().get("right_arm"));

		
		this.getModelMap().put("left_arm", new BedrockPart());
		this.getModelMap().put("left_leg", new BedrockPart());
		this.getModelMap().put("right_leg", new BedrockPart());
		this.getModelMap().put("right_arm", new BedrockPart());
		if(this.getModelMap().get("hat")!=null) {
			this.getHead().addChild(this.getModelMap().get("hat"));
			this.shouldRender.remove(this.getModelMap().get("hat"));
		}
		this.modelMap.forEach((key, model) -> modelMapWrapper.put(key, new ModelRendererWrapper(model)));
	}
}

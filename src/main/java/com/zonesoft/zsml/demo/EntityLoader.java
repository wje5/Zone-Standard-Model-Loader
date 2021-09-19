package com.zonesoft.zsml.demo;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityLoader {
	public static final DeferredRegister<EntityType<?>> ENTITYS = DeferredRegister.create(ForgeRegistries.ENTITIES,
			"zsml");
	public static final RegistryObject<EntityType<EntityDemo>> demo = ENTITYS.register("demo",
			() -> EntityType.Builder.create(EntityDemo::new, EntityClassification.MISC).size(1, 1).build("demo"));

}

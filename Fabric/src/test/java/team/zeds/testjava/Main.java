package team.zeds.testjava;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        MyMultiblocks.init();
        Registry.register(BuiltInRegistries.ITEM, "my_activator", new ItemActivator(new Item.Properties()));
    }
}

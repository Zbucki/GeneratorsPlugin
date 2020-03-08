package pl.zbucki.generators.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.data.FarmerData;
import pl.zbucki.generators.data.GeneratorData;
import pl.zbucki.generators.data.PlacedGeneratorData;
import pl.zbucki.generators.manager.FarmerManager;
import pl.zbucki.generators.manager.GeneratorManager;

@RequiredArgsConstructor
public class BlockListener implements Listener {

	private final GeneratorManager generatorManager;
	private final FarmerManager farmerManager;

	@EventHandler
	public void onPlaceGenerator(BlockPlaceEvent e) {
		if (e.isCancelled()) {
			return;
		}
		GeneratorData data = generatorManager.getGeneratorType(e.getItemInHand());
		if (data == null) {
			return;
		}
		generatorManager.addGenerator(e.getBlock().getLocation(), data, true);
	}
	
	@EventHandler
	public void onBreakGenerator(BlockBreakEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (e.getBlock().getType() != Material.STONE) {
			return;
		}
		PlacedGeneratorData data = generatorManager.getPlacedGenerator(e.getBlock().getLocation());
		if (data == null) {
			return;
		}
		if (e.getPlayer().getItemInHand() != null
				&& e.getPlayer().getItemInHand().getType() == data.getGenerator().getDestroyItem()) {
			data.drop();
			return;
		}
		data.destroy();
	}

	@EventHandler
	public void onPlaceFarmer(BlockPlaceEvent e) {
		if (e.isCancelled()) {
			return;
		}
		FarmerData data = farmerManager.getFarmerData(e.getItemInHand());
		if (data == null) {
			return;
		}
		data.build(e.getBlock().getLocation());
	}

	

}

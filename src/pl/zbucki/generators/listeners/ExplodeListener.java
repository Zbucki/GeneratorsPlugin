package pl.zbucki.generators.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.data.PlacedGeneratorData;
import pl.zbucki.generators.manager.GeneratorManager;

@RequiredArgsConstructor
public class ExplodeListener implements Listener {

	private final GeneratorManager manager;
	
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		List<Block> blocks = new ArrayList<>(e.blockList());
		for (Block b : blocks) {
			PlacedGeneratorData gen = manager.getPlacedGenerator(b.getLocation());
			if (gen == null) {
				continue;
			}
			e.blockList().remove(b);
			gen.drop();
		}
	}
}

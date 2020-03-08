package pl.zbucki.generators.util.runnable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.data.PlacedGeneratorData;
import pl.zbucki.generators.manager.GeneratorManager;

@RequiredArgsConstructor
public class GeneratorRepairRunnable implements Runnable {

	private final GeneratorManager manager;

	@Override
	public void run() {
		List<PlacedGeneratorData> toRepair = new ArrayList<>(manager.getToRepair());
		for (PlacedGeneratorData to : toRepair) {
			if (to.getLastDestroy() != 0 && to.getLastDestroy() < System.currentTimeMillis()) {
				manager.repairGenerator(to);
				continue;
			}
		}
	}

	public void start() {
		Bukkit.getScheduler().runTaskTimer(GeneratorsPlugin.getInst(), this, 10, 10);
	}

}

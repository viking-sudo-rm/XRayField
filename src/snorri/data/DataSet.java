package snorri.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import snorri.main.XRayField;

//TODO: add "comments" to log file with time stamps?

//when performing tests, create a new dataset and load values from logs/existing data

public class DataSet {
	
	private ArrayList<Double> data;
	
	public DataSet() {
		data = new ArrayList<Double>();
	}
	
	public void addPoint(Double val) {
		data.add(val);
	}
	
	public void load(DataSet other) {
		for (Double val : other.data)
			data.add(val);
	}
	
	public void loadLog(Player player) throws IOException {
		File log = new File(XRayField.getFolder().getAbsolutePath() + "/log/" + player.getUniqueId().toString() + ".csv");
		
		if (log.exists()) {
			
			BufferedReader fh = new BufferedReader(new FileReader(log));
			
			while (fh.ready())
				data.add(Double.parseDouble(fh.readLine()));
			fh.close();
			
		}
		
	}
	
	public void save(Player player) throws IOException {
		
		File logFolder = new File(XRayField.getFolder(), "log");
		logFolder.mkdir();
				
		File log = new File(logFolder, player.getUniqueId().toString() + ".csv");
		if (! log.exists())
			log.createNewFile();
				
		FileWriter fh = new FileWriter(log, true);
		for (Double angle : data)
			fh.write(angle + "\n");
		fh.close();
		
	}

}

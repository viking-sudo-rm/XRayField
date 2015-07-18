package snorri.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.inference.TTest;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import snorri.main.XRayField;

//TODO: add "comments" to log file with time stamps?

//TODO: make this extend an arraylist?

public class DataSet extends ArrayList<Double> {
	
	private static final long serialVersionUID = 1L;
	
	private DataMode mode = DataMode.SESSION;

	public void addPoint(Double val) {
		add(val);
	}
	
	public void load(DataSet other) {
		for (Double val : other)
			add(val);
	}
	
	public void loadLog(OfflinePlayer player) throws IOException {
		File log = new File(XRayField.getFolder().getAbsolutePath() + "/log/" + player.getUniqueId().toString() + ".csv");
		
		if (log.exists()) {
			
			BufferedReader fh = new BufferedReader(new FileReader(log));
			
			String line;
			while (fh.ready()) {
				line = fh.readLine();
				try {
					add(Double.parseDouble(line));
				}
				catch (NumberFormatException e) {
					XRayField.log("Could not parse corrupted line in log file for player " + player.getUniqueId().toString() + ": " + line);
				}
			}
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
		for (Double angle : this)
			fh.write(angle + "\n");
		fh.close();
		
	}
	
	public double mean() {
		double sum = 0;
		for (Double d : this)
			sum += d;
		return sum / size();
	}
	
	public double sd() {
		double mu = mean(), n = size(), sum = 0;
		for (Double d : this)
			sum += (d - mu) * (d - mu);
		return Math.sqrt(sum / (n - 1));
	}
	
	public double tTest(double mu) {
		TTest test = new TTest();
		try {
			double pvalue = 2 * test.tTest(mu, toPrimitiveArray());
			return (mean() > mu) ? pvalue : 1;
		} catch (NumberIsTooSmallException e) {
			return 10d;
		}
	}
	
	public double tTest(DataSet other) {
		TTest test = new TTest();
		try {
			double pvalue = 2 * test.tTest(toPrimitiveArray(), other.toPrimitiveArray());
			return (mean() - other.mean() > 0) ? pvalue : 1;
		} catch (NumberIsTooSmallException e) {
			return 10d;
		}
	}
	
	private double[] toPrimitiveArray() {
		double[] result = new double[size()];
		for (int i = 0; i < size(); i++)
			result[i] = (double) get(i);
		return result;
	}

	public DataMode getMode() {
		return mode;
	}

	public void setMode(DataMode mode) {
		this.mode = mode;
	}

}

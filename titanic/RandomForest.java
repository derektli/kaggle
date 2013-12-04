import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class RandomForest {

	private ArrayList<ArrayList<Integer>> data;
	private Node[] forest;
	private final int numDecisionTree;
	private final double pctFeaturesSelected;

	private ArrayList<Integer> defaultValue;

	public RandomForest(int num, double pct) {
		this.numDecisionTree = num;
		this.pctFeaturesSelected = pct;
		this.forest = new Node[num];
	}

	private void readTrainingFile(String fileName) {
		try {
			data = new ArrayList<ArrayList<Integer>>();
			CSVReader reader = new CSVReader(new FileReader(fileName));
			String [] arr;
			reader.readNext();
			while ((arr = reader.readNext()) != null) {
				ArrayList<Integer> tmp = new ArrayList<Integer>();
				for (int i = 0; i < arr.length; i ++) {
					// not using passengerid name, cabin, ticket as feature
					if (i == 0 || i == 3  || i == 8 || i ==10)
						continue;
					if (i == 4) {
						if (arr[i].equals("female"))
							tmp.add(1);
						else
							tmp.add(0);
						continue;
					}
					if (i == 11) {
						if (arr[i].equals("C"))
							tmp.add(0);
						else if (arr[i].equals("S"))
							tmp.add(1);
						else
							tmp.add(2);
						continue;
					}
					if (!arr[i].equals(""))
						tmp.add((int)Double.parseDouble(arr[i]));
					else
						tmp.add(-1);
					}
					data.add(tmp);
				}
			} catch (IOException ex) {
		}
		this.fillMissingValue();
	}

	private void fillMissingValue() {
		this.defaultValue = new ArrayList<Integer>();
		for (int i = 0; i < data.get(0).size(); i ++) {
			int mean = 0;
			int count = 0;

			for (int j = 0; j < data.size(); j ++) {
				if (data.get(j).get(i) != -1) {
					count ++;
					mean += data.get(j).get(i);
				}
			}
			mean = mean / count;
			this.defaultValue.add(mean);
			for (int j = 0; j < data.size(); j ++) {
				if (data.get(j).get(i) == -1) {
					data.get(j).set(i, mean);
				}
			}
		}
	}

	private void trainRandomForest(String fileName) {
		this.readTrainingFile(fileName);
		ArrayList<Integer> features = new ArrayList<Integer>();
		for (int i = 1; i < data.get(0).size(); i ++) {
			features.add(i);
		}

		for (int i = 0; i < this.numDecisionTree; i++) {
			this.forest[i] = new Node(data, features, true, this.pctFeaturesSelected);
		}
	}


	private void testRandomForest(String fileName) {
    System.out.println("PassengerId,Survived");
    try {
      CSVReader reader = new CSVReader(new FileReader(fileName));
      String [] arr;
      reader.readNext();
      while ((arr = reader.readNext()) != null) {
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp.add(0);
        for (int i = 0; i < arr.length; i ++) {
          // not using passengerid name, cabin, ticket as feature
          if (i == 0 || i == 2  || i == 7 || i == 9)
            continue;
          if (i == 3) {
            if (arr[i].equals("female"))
              tmp.add(1);
            else
              tmp.add(0);
            continue;
          }
          if (i == 10) {
            if (arr[i].equals("C"))
              tmp.add(0);
            else if (arr[i].equals("S"))
              tmp.add(1);
            else
              tmp.add(2);
            continue;
          }
          if (!arr[i].equals(""))
            tmp.add((int)Double.parseDouble(arr[i]));
          else
            tmp.add(this.defaultValue.get(tmp.size()));
        }

        int x = this.classify(tmp);
        if (x < 0)
          x = 0;
        System.out.println(arr[0] + "," + x);
      }
    } catch (IOException ex) {
    }
  }

	private int classify(ArrayList<Integer> data) {
		int nPos = 0;
		int nNeg = 0;
		for (int i = 0; i < this.numDecisionTree; i ++) {
			if (forest[i].classify(data) == 1)
				nPos ++;
			else
				nNeg ++;
		}
		if (nPos > nNeg)
			return 1;
		else
			return -1;
	}

	public static void main(String[] args) {
		RandomForest dt = new RandomForest(100, 0.6);
		dt.trainRandomForest(args[0]);
		dt.testRandomForest(args[1]);
	}
}
package replace;
/**
 * Created by Patrick on 28.11.16.
 */

import replace.model.DataSet;

public class Main {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("You must enter at least 2 parameters!");
            throw new IllegalArgumentException("You must enter at least 2 parameters!");
        } else if (args.length > 3) {
            System.out.println("You must enter at most 3 parameters!");
            throw new IllegalArgumentException("You must enter at most 3 parameters!");
        }

        DataSetReader reader = new DataSetReader(args[0]);
        DataSet dataset = null;
        try {
            dataset = reader.read();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Print current medians: ");
        DataSetAnalyzer medianCalculator = new DataSetAnalyzer(dataset);
        medianCalculator.print();

        DataSet newDataset;
        HandleMissingValues missingValueReplacer;
        if (args.length != 3) {
        	throw new IllegalArgumentException("Replaces must have exactly 3 parameters!");        
        } else {
            if (args[2].equals("ignore")) {
                missingValueReplacer = new HandleMissingValues(dataset);
                newDataset = missingValueReplacer.replace(true, false, false);
            } else if (args[2].equals("replaceByAttributeMean")) {
                missingValueReplacer = new HandleMissingValues(dataset);
                newDataset = missingValueReplacer.replace(false, true, false);
            } else if (args[2].equals("replaceByClassMean")) {
                missingValueReplacer = new HandleMissingValues(dataset);
                newDataset = missingValueReplacer.replace(false, false, true);
            } else {
                System.out
                        .println("Unknown mode "
                                + args[2]
                                + ". Know modes are ignore, replaceByAttributeMean, replaceByClassMean");
                throw new IllegalArgumentException("Unknown mode " + args[2] + ". Know modes are ignore, replaceByAttributeMean, replaceByClassMean");
            }
        }

        System.out.println("Print medians after replacements: ");
        DataSetAnalyzer newMedianCalculator = new DataSetAnalyzer(newDataset);
        newMedianCalculator.print();

        new DataSetWriter(args[1], newDataset).write();
    }

    }

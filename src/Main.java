/**
 * Created by Patrick on 28.11.16.
 */

import replace.bi.data.*;
import replace.bi.DataMiningException;
import replace.bi.DataSetAnalyzer;
import replace.bi.inputOutput.DataSetReader;
import replace.bi.inputOutput.DataSetWriter;
import replace.bi.replacer.*;

public class Main {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Not enough parameters!");
            throw new DataMiningException();
        } else if (args.length > 3) {
            System.out.println("Not enough parameters!");
            throw new DataMiningException();
        }

        DataSetReader reader = new DataSetReader(args[0]);
        DataSet dataset = null;
        try {
            dataset = reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Print current medians: ");
        DataSetAnalyzer medianCalculator = new DataSetAnalyzer(dataset);
        medianCalculator.print();

        handleMissingValues missingValueReplacer;
        if (args.length == 2) {
            missingValueReplacer = new ignoreAttributeWhenMissingValue(dataset);
        } else {
            missingValueReplacer = new replaceWholeClWithClMean(dataset);
            if (args[2].equals("ignore")) {
                missingValueReplacer = new ignoreAttributeWhenMissingValue(
                        dataset);
            } else if (args[2].equals("attributeMean")) {
                missingValueReplacer = new replaceMissingValueWithAttributeMean(
                        dataset);
            } else if (args[2].equals("clazzMean")) {
                missingValueReplacer = new replaceMissingValueWithClMean(
                        dataset);
            } else if (args[2].equals("attribute2attributeMean")) {
                missingValueReplacer = new replaceWholeAttributeWithAttributeMean(
                        dataset);
            } else if (args[2].equals("class2classMean")) {
                missingValueReplacer = new replaceWholeClWithClMean(
                        dataset);
            } else {
                System.out
                        .println("Unknown mode "
                                + args[2]
                                + ". Know modes are ignore, attributeMean, classMean, attribute2attributeMean and class2classMean");
                throw new DataMiningException();
            }
        }

        System.out.println("Print medians after replacements: ");
        DataSet newDataset = missingValueReplacer.replace();

        DataSetAnalyzer newMedianCalculator = new DataSetAnalyzer(newDataset);
        newMedianCalculator.print();

        new DataSetWriter(args[1], newDataset).write();
    }

    }

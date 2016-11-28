package replace.bi;

/**
 * Created by Patrick on 28.11.16.
 */

import java.util.HashMap;
import java.util.Map;
import replace.bi.data.*;

public class CopyOfDataSetAnalyzer {

    private DataSet dataset;
    private Map<Attribute, Double> means = new HashMap<Attribute, Double>();
    private Map<Attribute, Map<Cl, Double>> meansByCl = new HashMap<Attribute, Map<Cl, Double>>();
    private Map<Attribute, Boolean> missingValues = new HashMap<Attribute, Boolean>();
    private Map<Attribute, Map<Cl, Boolean>> missingValuesByCl = new HashMap<Attribute, Map<Cl, Boolean>>();

    public CopyOfDataSetAnalyzer(DataSet dataset) {
        this.dataset = dataset;
        this.calcmeans();
    }

    private void calcmeans() {
        Map<Attribute, Double> sum = new HashMap<Attribute, Double>();
        Map<Attribute, Double> size = new HashMap<Attribute, Double>();
        Map<Attribute, Map<Cl, Double>> sumByCl = new HashMap<Attribute, Map<Cl, Double>>();
        Map<Attribute, Map<Cl, Double>> sizeByCl = new HashMap<Attribute, Map<Cl, Double>>();

        for (Row row : dataset.getRows()) {
            Cl cl = dataset.getClByName(row.getCl());
            for (Attribute attribute : dataset.getAttributes()) {
                // Only attributes that are not class identifiers
                if (attribute.isCl()) {
                    continue;
                }

                Field field = row.getField(attribute);

                // Only fields that are not missing
                if (field.isMissing()) {
                    missingValues.put(attribute, true);
                    missingValuesByCl.get(attribute).put(cl, true);
                    continue;
                }

                double value = field.getValue();

                // init Lists when not existing
                if (!sum.containsKey(attribute)) {
                    sum.put(attribute, 0.0);
                    size.put(attribute, 0.0);
                    sumByCl.put(attribute, new HashMap<Cl, Double>());
                    sizeByCl.put(attribute, new HashMap<Cl, Double>());
                }

                if (!sumByCl.get(attribute).containsKey(cl)) {
                    sumByCl.get(attribute).put(cl, 0.0);
                    sizeByCl.get(attribute).put(cl, 0.0);
                }

                // Sum values
                sum.put(attribute, sum.get(attribute) + value);
                size.put(attribute, size.get(attribute) + 1);
                sumByCl.get(attribute).put(cl,
                        sumByCl.get(attribute).get(cl) + value);
                sizeByCl.get(attribute).put(cl,
                        sizeByCl.get(attribute).get(cl) + 1);
            }
        }

        // calc means
        for (Attribute attribute : dataset.getAttributes()) {
            // Only attributes that are not class identifiers
            if (attribute.isCl()) {
                continue;
            }

            // calc for whole attribute
            double mean = sum.get(attribute) / size.get(attribute);
            means.put(attribute, mean);
            meansByCl.put(attribute, new HashMap<Cl, Double>());

            for (Cl cl : dataset.getCles()) {
                double meanForCl = sumByCl.get(attribute).get(cl)
                        / sizeByCl.get(attribute).get(cl);
                meansByCl.get(attribute).put(cl, meanForCl);
            }
        }
    }

    public void print() {
        int i = 1;
        for (Attribute attribute : dataset.getAttributes()) {
            if (attribute.isCl()) {
                continue;
            }
            System.out
                    .println(fixedLenthString(i + "", 5)
                            + fixedLenthString(attribute.getName(), 25)
                            + "   avg: "
                            + fixedLenthString(roundmean(means.get(attribute))
                            + "", 20)
                            + fixedLenthString(
                            missingValueString(hasMissingValue(attribute)),
                            15));
            int j = 1;
            for (Cl cl : dataset.getCles()) {
                System.out.println("     -"
                        + fixedLenthString(j + "", 2)
                        + fixedLenthString(cl.getName(), 22)
                        + "   avg: "
                        + fixedLenthString(roundmean(meansByCl
                        .get(attribute).get(cl)) + "", 10)
                        + fixedLenthString(
                        missingValueString(hasMissingValue(attribute,
                                cl)), 15));
                j++;
            }
            i++;
        }
    }

    public double getMean(Attribute attribute) {
        return this.means.get(attribute);
    }

    public double getMean(Attribute attribute, Cl cl) {
        return this.meansByCl.get(attribute).get(cl);
    }

    public boolean hasMissingValue(Attribute attribute) {
        return this.missingValues.get(attribute);
    }

    public boolean hasMissingValue(Attribute attribute, Cl cl) {
        return this.missingValuesByCl.get(attribute).get(cl);
    }

    private static double roundmean(double mean) {
        return Math.round(mean * 10000) / ((double) 10000);
    }

    private static String fixedLenthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    private static String missingValueString(boolean hasMissingValue) {
        return (hasMissingValue == true) ? "missing value!" : "";
    }

}

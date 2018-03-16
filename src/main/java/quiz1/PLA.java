package quiz1;

import jdk.management.resource.internal.inst.DatagramDispatcherRMHooks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PLA {
    final static protected int DIMENSION = 5;

    protected double[] ws = new double[DIMENSION];

    public static List<Record> getData(String filename) throws IOException {
        List<Record> ret = null;
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            Stream<Record> recordStream = lines.map(l -> {
                String[] cols = l.split("\\s+");
                double[] xs = new double[DIMENSION];
                xs[0] = 1.0;
                for (int i = 1; i < DIMENSION; i++) {
                    xs[i] = Double.parseDouble(cols[i-1]);
                }
                int y = Integer.parseInt(cols[DIMENSION - 1]);
                return new Record(xs, y);
            });
            ret = recordStream.collect(Collectors.toList());
        }
        return ret;
    }

    public int fit(List<Record> records) {
        boolean noError;
        int updateCnt = 0;
        do {
            noError = true;
            for (Record record : records) {
                double f = 0.0;
                double[] xs = record.getXs();
                for (int i = 0; i < DIMENSION; i++) {
                    f += xs[i] * ws[i];
                }
                if ((record.getY() > 0 && f <= 0) || (record.getY() < 0 && f > 0)) {
                    noError = false;
                    updateCnt ++;
                    for (int i = 0; i < DIMENSION; i++) {
                        ws[i] += 0.5 * xs[i] * record.getY();
                    }
                }
            }
        } while (!noError);
        return updateCnt;
    }

    public double[] getWs() {
        return ws;
    }

    public static void main(String[] args) throws IOException {
        List<Record> records = getData("sample.txt");
        int updateCntSum = 0;
        for (int i = 0; i < 2000; i++) {
            PLA pla = new PLA();
            Collections.shuffle(records, new Random(System.currentTimeMillis()));
            updateCntSum += pla.fit(records);
        }

        System.out.println(updateCntSum / 2000.0);
    }
}

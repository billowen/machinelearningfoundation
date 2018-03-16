package quiz1;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PocketPLA extends PLA {

    private static double getErrorRate(List<Record> records, double[] ws) {
        int errCntOfPocket = 0;
        for (Record record : records) {
            double f = 0.0;
            double[] xs = record.getXs();
            for (int i = 0; i < DIMENSION; i++) {
                f += xs[i] * ws[i];
            }
            if ((record.getY() < 0 && f > 0) || (record.getY() > 0 && f <= 0)) {
                errCntOfPocket += 1;
            }
        }
        return ((double)errCntOfPocket) / records.size();
    }

    public double fit(List<Record> records, int iterator) {
        double errRateOfPocket = getErrorRate(records, ws);
        double[] wts = Arrays.copyOf(ws, DIMENSION);
        boolean noError = true;
        int iterCnt = 0;
        do {
            noError = true;
            for (Record record : records) {
                double f = 0.0;
                double[] xs = record.getXs();
                for (int i = 0; i < DIMENSION; i++) {
                    f += xs[i] * wts[i];
                }
                if ((record.getY() > 0 && f <= 0) || (record.getY() < 0 && f > 0)) {
                    noError = false;
                    for (int i = 0; i < DIMENSION; i++) {
                        wts[i] += xs[i] * record.getY();
                    }
                    double curErrorRatio = getErrorRate(records, wts);
                    if (curErrorRatio < errRateOfPocket) {
                        ws = Arrays.copyOf(wts, DIMENSION);
                    }
                }
            }
            iterCnt += 1;
        } while (!noError && iterCnt < iterator);

        return errRateOfPocket;
    }

    public static void main(String[] args) throws IOException {
        List<Record> train = getData("1_18_train.txt");
        List<Record> test = getData("1_18_test.txt");
        double errRate = 0.0;
        for (int i = 0; i < 2000; i++) {
            PocketPLA pla = new PocketPLA();
            Collections.shuffle(train, new Random(System.currentTimeMillis()));
            pla.fit(train, 100);
            double[] ws = pla.getWs();
            errRate += getErrorRate(test, ws);
            System.out.println(errRate / (i + 1));
        }

        System.out.println(errRate / 2000.0);
    }

}

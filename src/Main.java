import Jama.Matrix;
import com.sun.deploy.util.ArrayUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.lang.StrictMath.abs;

/**
 * Created by deynekalex on 05.10.15.
 */
public class Main {
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> results = new ArrayList();
        int arg_size = 12;
        try {
            Scanner in = new Scanner(new File("students.txt"));
            ArrayList<Double> list = new ArrayList<>();
            while(in.hasNext()) {
                for (int j = 0; j < arg_size; j++) {
                    list.add(Double.valueOf(in.next()));
                }
                results.add(Double.valueOf(in.next()));
                data.add(new ArrayList<>(list));
                list.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int teach_size = (int) (data.size() * 0.8);
        double[][] mas1 = new double[teach_size][arg_size];
        double[][] mas2 = new double[data.size() - teach_size][arg_size];
        double[] res1 = new double[teach_size];
        double[] res2 = new double[data.size()-teach_size];
        for(int i = 0; i < teach_size; i++) {
            res1[i] = results.get(i);
            Double[] boxed1 = data.get(i).toArray(new Double[arg_size]);
            mas1[i] = Stream.of(boxed1).mapToDouble(Double::doubleValue).toArray();
        }
        for(int i = 0; i < data.size()-teach_size; i++){
                res2[i] = results.get(i);
                Double[] boxed2 = data.get(i).toArray(new Double[arg_size]);
                mas2[i] = Stream.of(boxed2).mapToDouble(Double::doubleValue).toArray();
        }
        Matrix x = new Matrix(mas1);
        Matrix y = new Matrix(res1, 1);
        normolize(x);
        Matrix cur = x.transpose().times(x);
        Matrix betta = (cur.inverse()).times(x.transpose()).times(y.transpose());
        Matrix x_test = new Matrix(mas2);
        Matrix y_test = new Matrix(res2, 1);
        normolize(x_test);
        double sum = 0;
        for(int i = 0; i < x_test.getRowDimension(); i++) {
            Matrix t = new Matrix(x_test.getArrayCopy()[i], 1);
            Matrix res = t.times(betta);
            System.out.println(res.get(0,0) + "    " + y_test.get(0,i));
            sum += abs(res.get(0,0) - y_test.get(0,i));
        }
        System.out.println(sum/y_test.getColumnDimension());
    }

    public static void normolize(Matrix x){
        for(int i = 0; i < x.getColumnDimension(); i++){
            //find min, max
            double min = x.get(0,i);//0 элемент i колонки
            double max = x.get(0,i);
            for(int j = 0; j < x.getRowDimension(); j++){
                if (x.get(j,i) < min)
                    min = x.get(j,i);
                if (x.get(j,i) > max)
                    max = x.get(j,i);
            }
            for(int j = 0; j < x.getRowDimension(); j++){
                x.set(j,i,(x.get(j,i) - min)/(max - min));
            }
        }
    }
}

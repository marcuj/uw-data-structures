package seamcarving;

import graphs.Edge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 * @see SeamCarver
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        double[][] table = new double[picture.width()][picture.height()];
        for (int y = 0; y < picture.height(); y += 1) {
            table[0][y] = f.apply(picture, 0, y);
        }
        // set next column values with lowest energy predecessor(left-up/middle/down) + current energy value
        double up = 0;
        double mid = 0;
        double down = 0;
        for (int x = 1; x < picture.width(); x++) {
            // top case (y = 0);
            mid = table[x - 1][0];
            down = table[x - 1][1];
            double least = Math.min(mid, down);
            table[x][0] = least + f.apply(picture, x, 0);
            // bottom case (picture.height() - 1)
            up = table[x - 1][picture.height() - 2];
            mid = table[x - 1][picture.height() - 1];
            least = Math.min(up, mid);
            table[x][picture.height() - 1] = least + f.apply(picture, x, picture.height() - 1);
            // mid case
            for (int y = 1; y < picture.height() - 1; y++) {
                up = table[x - 1][y - 1];
                mid = table[x - 1][y];
                down = table[x - 1][y + 1];
                least = Math.min(Math.min(up, mid), down);
                table[x][y] = least + f.apply(picture, x, y);
            }
        }
        // Find the shortest path
        int lowestY = 0;
        double lowestE = Double.POSITIVE_INFINITY;
        for (int y = 0; y < picture.height(); y++) {
            if (table[picture.width() - 1][y] < lowestE) {
                lowestE = table[picture.width() - 1][y];
                lowestY = y;
            }
        }
        List<Integer> result = new ArrayList<>();
        result.add(lowestY);
        for (int x = picture.width() - 2; x >= 0; x--) {
            mid = table[x][lowestY];
            if (lowestY == 0) {
                down = table[x][lowestY + 1];
                lowestE = Math.min(down, mid);
            } else if (lowestY == picture.height() - 1) {
                up = table[x][lowestY - 1];
                lowestE = Math.min(up, mid);
            } else {
                up = table[x][lowestY - 1];
                down = table[x][lowestY + 1];
                lowestE = Math.min(Math.min(up, down), mid);
            }
            if (lowestE == mid) {
                result.add(lowestY);
            } else if (lowestE == up) {
                lowestY = lowestY - 1;
                result.add(lowestY);
            } else {
                lowestY = lowestY + 1;
                result.add(lowestY);
            }
        }
        Collections.reverse(result);
        return result;
    }
}

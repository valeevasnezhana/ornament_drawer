package ornaments.classes;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class represents a matrix of ornament colors, where each element
 * represents a
 * color choice
 * for a particular element. This class also
 * includes methods to randomize the color choices
 * for each element and ensures that ornament seems really "random"
 */
public class ColorChoiceMatrix {
    private final int repeats;
    private final List<OrnamentColor> colors;
    private final int[][] entry;

    public ColorChoiceMatrix(int repeats, List<OrnamentColor> colors) {
        if (repeats < 1 || colors.isEmpty()) {
            throw new IllegalStateException("repeats amount must be positive " +
                    "integer and colors must be no-empty");
        }
        this.repeats = repeats;
        this.colors = colors;
        entry = new int[repeats - 1][repeats];
        refill();
    }

    /**
     * This method randomizes the values in the 'entry' 2D array by shuffling the numbers and populating the array.
     * It ensures that no two adjacent elements in the array have the same value.
     */
    public void randomize() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            numbers.add(i);
        }

        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < repeats - 1; i++) {
            for (int j = 0; j < repeats; j++) {
                int randomIndex = random.nextInt(numbers.size());
                int randomNumber = numbers.get(randomIndex);

                while ((i > 0 && entry[i - 1][j] == randomNumber) ||
                        (j > 0 && entry[i][j - 1] == randomNumber)) {
                    randomIndex = random.nextInt(numbers.size());
                    randomNumber = numbers.get(randomIndex);
                }

                entry[i][j] = randomNumber;
            }
        }
    }


    /**
     * @return true if matrix contains pair repeats like this:
     * COLOR1 ... COLOR1
     * COLOR2 ... COLOR2
     */
    public boolean checkVerticalOnHorizontalPairsManyRepeats() {
        for (int row = 1; row < entry.length; row++) {
            List<int[]> levelPairs = new ArrayList<>();
            for (int j = 0; j < entry[0].length; j++) {
                levelPairs.add(new int[]{entry[row - 1][j], entry[row][j]});
            }
            if (checkEqualPairs(levelPairs)) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return true if matrix contains pair repeats like this:
     * COLOR1
     * COLOR2
     * COLOR1
     * COLOR2
     * OR
     * COLOR1
     * COLOR2
     * colorX
     * colorY
     * COLOR1
     * COLOR2
     * etc.
     */
    public boolean checkOneVerticalPairsManyRepeats() {
        int colIndex = 0;

        while (colIndex < entry[0].length) {
            List<int[]> levelPairs = new ArrayList<>();
            for (int i = 1; i < entry.length; i += 2) {
                levelPairs.add(
                        new int[]{entry[i - 1][colIndex], entry[i][colIndex]});
            }
            if (checkEqualPairs(levelPairs)) {
                return true;
            }
            levelPairs = new ArrayList<>();
            for (int i = entry.length - 1; i > 0; i -= 2) {
                levelPairs.add(
                        new int[]{entry[i - 1][colIndex], entry[i][colIndex]});
            }
            if (checkEqualPairs(levelPairs)) {
                return true;
            }
            colIndex++;
        }
        return false;
    }


    /**
     * @return true if matrix contains pair repeats like this:
     * COLOR1 COLOR2 COLOR1 COLOR2
     * OR COLOR1 COLOR2 colorX colorY COLOR1 COLOR2 etc.
     */

    public boolean checkHorizontalPairsManyRepeats() {
        for (int[] ints : entry) {
            List<int[]> levelPairs = new ArrayList<>();
            for (int j = 1; j < entry[0].length; j += 2) {
                levelPairs.add(new int[]{ints[j - 1], ints[j]});
            }
            if (checkEqualPairs(levelPairs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are any repeated colors in the color matrix, with
     * specific conditions
     *
     * @return true if there are repeated colors according to conditions, false otherwise
     * @see ColorChoiceMatrix#checkHorizontalPairsManyRepeats()
     * @see ColorChoiceMatrix#checkOneVerticalPairsManyRepeats()
     * @see ColorChoiceMatrix#checkVerticalOnHorizontalPairsManyRepeats()
     */
    public boolean checkManyColorsRepeats() {
        if (repeats > 8 || colors.size() < 5) {
            return false;
        }
        return checkVerticalOnHorizontalPairsManyRepeats() ||
                checkOneVerticalPairsManyRepeats() ||
                checkHorizontalPairsManyRepeats();
    }


    /**
     * Refills the color matrix until there are no repeated colors according to
     * specific conditions.
     */
    public void refill() {
        randomize();
        boolean manyColorRepeats = checkManyColorsRepeats();
        while (manyColorRepeats) {
            randomize();
            manyColorRepeats = checkManyColorsRepeats();
        }
    }

    /**
     * Checks if there are any equal pairs in the given list of integer arrays.
     *
     * @param pairs the list of integer arrays containing pairs
     * @return true if there are equal pairs, false otherwise
     */

    private boolean checkEqualPairs(List<int[]> pairs) {
        Set<String> pairSet = new HashSet<>();
        for (int[] pair : pairs) {
            String pairString = pair[0] + "," + pair[1];
            if (!pairSet.add(pairString)) {
                return true;
            }
        }
        return false;
    }

    public int[][] getEntry() {
        return entry;
    }

    /**
     * @return String identifier based on the number of repeats and values of colors
     * If the size of colors list is less than or equal to 5 and the number of repeats is less than or equal to 10,
     * then the identifier is calculated using the pairs stored in the 'entry' array and the size of colors list.
     * Otherwise, a random UUID is used as the tail of identifier
     */
    public String calculateIdentifier() {
        StringJoiner joiner = new StringJoiner("");
        joiner.add("" + repeats);
        for (OrnamentColor ornamentColor: colors) {
            joiner.add(Integer.toHexString(ornamentColor.color().getRGB()));
        }
        joiner.add("_");
        if (colors.size() <= 5 && repeats <= 10) {
            for (int[] ints : entry) {
                for (int j = 1; j < ints.length; j += 2) {
                    joiner.add("" + (char) ('a' + colors.size() * ints[j - 1] +
                            ints[j]));
                }
                if (entry[0].length % 2 == 1) {
                    joiner.add("" + (char) ('a' + ints[ints.length - 1]));
                }
            }
        } else {
            joiner.add(UUID.randomUUID().toString());
        }
        return joiner.toString();
    }
}

package ornaments.classes;


import java.util.*;

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

    public void randomize() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            numbers.add(i);
        }

        Random random = new Random();
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

    public boolean checkManyColorsRepeats() {
        if (repeats > 8 || colors.size() < 5) {
            return false;
        }
        return checkVerticalOnHorizontalPairsManyRepeats() ||
                checkOneVerticalPairsManyRepeats() ||
                checkHorizontalPairsManyRepeats();
    }

    public void refill() {
        randomize();
        boolean manyColorRepeats = checkManyColorsRepeats();
        while (manyColorRepeats) {
            randomize();
            manyColorRepeats = checkManyColorsRepeats();
        }
    }

    private boolean checkEqualPairs(List<int[]> pairs) {
        for (int i = 0; i < pairs.size() - 1; i++) {
            for (int j = i + 1; j < pairs.size(); j++) {
                if (pairs.get(i)[0] == pairs.get(j)[0] &&
                        pairs.get(i)[1] == pairs.get(j)[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getEntry() {
        return entry;
    }

    public String calculateIdentifier() {
        StringJoiner joiner = new StringJoiner("");
        joiner.add("" + repeats + colors.size());
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

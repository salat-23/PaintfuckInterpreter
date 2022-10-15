public class Main {

    private static final String FIBONACCI =
            """
            *
            [[s*en]
            sw[w]enn
            [[e]w*ssss*nnnnw[w]ess[e]*[w]enn]
            ssss[*nnnn*essss]
            nnnnw[w]e
            ss]
            """;

    public static void main(String[] args) {

        System.out.println(Paintfuck.interpreter(FIBONACCI, 9999, 100, 15));

    }
}

class Paintfuck {

    private final boolean[][] grid;
    private int xPos, yPos;
    private final int xMax, yMax;

    private Paintfuck(int width, int height) {
        grid = new boolean[width][height];
        xMax = width - 1;
        yMax = height - 1;
        xPos = 0;
        yPos = 0;
    }

    private void north() {
        yPos--;
        if (yPos < 0) yPos = yMax;
    }

    private void south() {
        yPos++;
        if (yPos > yMax) yPos = 0;
    }

    private void west() {
        xPos--;
        if (xPos < 0) xPos = xMax;
    }

    private void east() {
        xPos++;
        if (xPos > xMax) xPos = 0;
    }

    private void flip() {
        grid[xPos][yPos] = !grid[xPos][yPos];
    }

    private boolean getCurrent() {
        return grid[xPos][yPos];
    }

    private String convert() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y <= yMax; y++) {

            for (int x = 0; x <= xMax; x++) {
              /*  if (x == xPos && y == yPos) {
                    stringBuilder.append(grid[x][y] ? "+" : "-");
                } else*/
                    stringBuilder.append(grid[x][y] ? "1" : "0");
            }
            if (y != yMax) {
                stringBuilder.append("\r\n");
            }
        }
        return stringBuilder.toString();
    }

    public static String interpreter(String code, int iterations, int width, int height) {
        Paintfuck paintfuck = new Paintfuck(width, height);
        int currentIteration = 0;
        int currentPos = 0;
        char[] program = code.toCharArray();

        while (true) {
            if (currentIteration >= iterations) return paintfuck.convert();
            char c = program[currentPos];
            switch (c) {
                case 'n' -> {
                    paintfuck.north();
                    currentIteration++;
                }
                case 's' -> {
                    paintfuck.south();
                    currentIteration++;
                }
                case 'w' -> {
                    paintfuck.west();
                    currentIteration++;
                }
                case 'e' -> {
                    paintfuck.east();
                    currentIteration++;
                }
                case '*' -> {
                    paintfuck.flip();
                    currentIteration++;
                }
                case '[' -> {
                    if (!paintfuck.getCurrent()) {
                        int abstractPos = currentPos;
                        int stack = 0;
                        while (true) {
                            abstractPos++;
                            if (program[abstractPos] == ']') {
                                if (stack == 0) {
                                    currentPos = abstractPos;
                                    break;
                                }
                                stack--;
                            } else if (program[abstractPos] == '[') {
                                stack++;
                            }
                            if (abstractPos > program.length - 1) abstractPos = 0;
                        }
                    }
                    currentIteration++;
                }
                case ']' -> {
                    if (paintfuck.getCurrent()) {
                        int abstractPos = currentPos;
                        int stack = 0;
                        while (true) {
                            abstractPos--;
                            if (program[abstractPos] == '[') {
                                if (stack == 0) {
                                    currentPos = abstractPos;
                                    break;
                                }
                                stack--;
                            } else if (program[abstractPos] == ']') {
                                stack++;
                            }
                        }
                        currentIteration++;
                    }
                }
            }
            currentPos++;
            if (currentPos > code.length() - 1) return paintfuck.convert();
        }
    }
}
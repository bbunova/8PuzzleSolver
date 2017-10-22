package slidingblocks;

import java.util.Scanner;

/**
 * @author Beatris Bunova <bibunova@gmail.com>
 */
public class State {

    public static final int MOVEMENT_LEFT = 1;
    public static final int MOVEMENT_RIGHT = 2;
    public static final int MOVEMENT_UP = 3;
    public static final int MOVEMENT_DOWN = 4;

    public static int[] goalXCoordinates;
    public static int[] goalYCoordinates;

    private final int[][] state;
    private final int zeroPositionX;
    private final int zeroPositionY;
    private final int distance;
    private final String path;
    private final int parentMovement;

    private static int numberOfBlocksPerDimension;

    public State(int[][] state, int zeroPositionX, int zeroPositionY, int distance, String path, int parentMovement) {
        this.state = state;
        this.zeroPositionX = zeroPositionX;
        this.zeroPositionY = zeroPositionY;
        this.distance = distance;
        this.path = path;
        this.parentMovement = parentMovement;
    }

    public static State getInitialState(int numberOfBlocksPerDimension) throws Exception {
        State.numberOfBlocksPerDimension = numberOfBlocksPerDimension;
        State.goalXCoordinates = new int[numberOfBlocksPerDimension * numberOfBlocksPerDimension];
        State.goalYCoordinates = new int[numberOfBlocksPerDimension * numberOfBlocksPerDimension];

        int[][] state = new int[numberOfBlocksPerDimension][numberOfBlocksPerDimension];
        int zeroPositionX = -1;
        int zeroPositionY = -1;

        int goalBlock = 0;

        Scanner input = new Scanner(System.in);

        System.out.println("Enter blocks: ");
        for (int x = 0; x < numberOfBlocksPerDimension; x++) {
            for (int y = 0; y < numberOfBlocksPerDimension; y++) {
                int block = input.nextInt();
                if (block == 0) {
                    zeroPositionX = x;
                    zeroPositionY = y;
                }

                state[x][y] = block;

                State.goalXCoordinates[goalBlock] = x;
                State.goalYCoordinates[goalBlock] = y;
                goalBlock++;
            }
        }

        if ((zeroPositionX == -1) || (zeroPositionY == -1)) {
            throw new Exception("One of the block must be zero");
        }

        return new State(state, zeroPositionX, zeroPositionY, 0, "", 0);
    }

    public boolean isSolvable() {
        int invertions = 0;

        int[] stateInOneDimention = new int[numberOfBlocksPerDimension * numberOfBlocksPerDimension - 1];
        int index = 0;
        for (int x = 0; x < numberOfBlocksPerDimension; x++) {
            for (int y = 0; y < numberOfBlocksPerDimension; y++) {
                if (this.state[x][y] == 0) {
                    continue;
                }

                stateInOneDimention[index++] = this.state[x][y];
            }
        }

        for (int i = 0; i < stateInOneDimention.length; i++) {
            for (int j = i + 1; j < stateInOneDimention.length; j++) {
                if (stateInOneDimention[j] > stateInOneDimention[i]) {
                    invertions++;
                }
            }
        }

        if (invertions % 2 == 0) {
            return true;
        }

        return false;
    }

    public boolean isGoal() {
        int block = 0;

        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            for (int y = 0; y < State.numberOfBlocksPerDimension; y++) {
                if (this.state[x][y] != block) {
                    return false;
                }
                block++;
            }
        }

        return true;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getManhattanDistance() {
        int manhattanDistance = 0;

        int block;
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            for (int y = 0; y < State.numberOfBlocksPerDimension; y++) {
                block = this.state[x][y];
                manhattanDistance += Math.abs(x - goalXCoordinates[block]) + Math.abs(y - goalYCoordinates[block]);
            }
        }

        return manhattanDistance;
    }

    public void printState() {
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            for (int y = 0; y < State.numberOfBlocksPerDimension; y++) {
                System.out.print(this.state[x][y] + "  ");
            }
            System.out.println("\n");
        }
    }

    public void printPath() {
        System.out.println(this.path);
    }

    public boolean canMoveLeft() {
        if (this.parentMovement == MOVEMENT_RIGHT) {
            return false;
        }

        if (this.zeroPositionY == State.numberOfBlocksPerDimension - 1) {
            return false;
        }

        return true;
    }

    public boolean canMoveRight() {
        if (this.parentMovement == MOVEMENT_LEFT) {
            return false;
        }

        if (this.zeroPositionY == 0) {
            return false;
        }

        return true;
    }

    public boolean canMoveDown() {
        if (this.parentMovement == MOVEMENT_UP) {
            return false;
        }

        if (this.zeroPositionX == 0) {
            return false;
        }

        return true;
    }

    public boolean canMoveUp() {
        if (this.parentMovement == MOVEMENT_DOWN) {
            return false;
        }

        if (this.zeroPositionX == State.numberOfBlocksPerDimension - 1) {
            return false;
        }

        return true;
    }

    public State moveLeft() {
        int[][] stateCopy = new int[State.numberOfBlocksPerDimension][State.numberOfBlocksPerDimension];
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            System.arraycopy(this.state[x], 0, stateCopy[x], 0, State.numberOfBlocksPerDimension);
        }

        stateCopy[this.zeroPositionX][this.zeroPositionY] = stateCopy[this.zeroPositionX][this.zeroPositionY + 1];
        stateCopy[this.zeroPositionX][this.zeroPositionY + 1] = 0;

        return new State(stateCopy, this.zeroPositionX, this.zeroPositionY + 1, this.distance + 1, this.path + "left\n", MOVEMENT_LEFT);
    }

    public State moveRight() {
        int[][] stateCopy = new int[State.numberOfBlocksPerDimension][State.numberOfBlocksPerDimension];
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            System.arraycopy(this.state[x], 0, stateCopy[x], 0, State.numberOfBlocksPerDimension);
        }

        stateCopy[this.zeroPositionX][this.zeroPositionY] = stateCopy[this.zeroPositionX][this.zeroPositionY - 1];
        stateCopy[this.zeroPositionX][this.zeroPositionY - 1] = 0;

        return new State(stateCopy, this.zeroPositionX, this.zeroPositionY - 1, this.distance + 1, this.path + "right\n", MOVEMENT_RIGHT);
    }

    public State moveDown() {
        int[][] stateCopy = new int[State.numberOfBlocksPerDimension][State.numberOfBlocksPerDimension];
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            System.arraycopy(this.state[x], 0, stateCopy[x], 0, State.numberOfBlocksPerDimension);
        }

        stateCopy[this.zeroPositionX][this.zeroPositionY] = stateCopy[this.zeroPositionX - 1][this.zeroPositionY];
        stateCopy[this.zeroPositionX - 1][this.zeroPositionY] = 0;

        return new State(stateCopy, this.zeroPositionX - 1, this.zeroPositionY, this.distance + 1, this.path + "down\n", MOVEMENT_DOWN);
    }

    public State moveUp() {
        int[][] stateCopy = new int[State.numberOfBlocksPerDimension][State.numberOfBlocksPerDimension];
        for (int x = 0; x < State.numberOfBlocksPerDimension; x++) {
            System.arraycopy(this.state[x], 0, stateCopy[x], 0, State.numberOfBlocksPerDimension);
        }

        stateCopy[this.zeroPositionX][this.zeroPositionY] = stateCopy[this.zeroPositionX + 1][this.zeroPositionY];
        stateCopy[this.zeroPositionX + 1][this.zeroPositionY] = 0;

        return new State(stateCopy, this.zeroPositionX + 1, this.zeroPositionY, this.distance + 1, this.path + "up\n", MOVEMENT_UP);
    }
}

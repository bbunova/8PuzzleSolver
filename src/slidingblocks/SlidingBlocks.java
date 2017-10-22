package slidingblocks;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author Beatris Bunova <bibunova@gmail.com>
 */
public class SlidingBlocks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter number of blocks: ");
        int numberOfBlocks = input.nextInt();
        System.out.println();

        if (Math.sqrt(numberOfBlocks + 1) != (int) Math.sqrt(numberOfBlocks + 1)) {
            System.err.println("Inappropriate number of blocks!");
            return;
        }

        int numberOfBlocksBySide = (int) Math.sqrt(numberOfBlocks + 1);

        State initialState;
        try {
            initialState = State.getInitialState(numberOfBlocksBySide);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return;
        }

        initialState.printState();
        if (!initialState.isSolvable()) {
            System.err.println("Puzzle is not solvable.");
            return;
        }
        
        System.out.println("Puzzle is solvable.");

        SlidingBlocks sb = new SlidingBlocks();
        sb.AStar(initialState);
    }

    public void AStar(State initialState) {
        StateComparator comparator = new StateComparator();
        PriorityQueue<State> queue = new PriorityQueue<>(comparator);
        queue.add(initialState);

        while (!queue.isEmpty()) {
            State state = queue.poll();

            if (state.isGoal()) {
                System.out.println(state.getDistance());
                state.printPath();
                return;
            }

            if (state.canMoveLeft()) {
                queue.add(state.moveLeft());
            }
            if (state.canMoveRight()) {
                queue.add(state.moveRight());
            }
            if (state.canMoveUp()) {
                queue.add(state.moveUp());
            }
            if (state.canMoveDown()) {
                queue.add(state.moveDown());
            }
        }

        System.out.println("Solution was not found for this puzzle.");
    }

}

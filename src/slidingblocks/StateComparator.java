package slidingblocks;

import java.util.Comparator;

/**
 * @author Beatris Bunova <bibunova@gmail.com>
 */
public class StateComparator implements Comparator<State> {

    @Override
    public int compare(State state1, State state2) {
        int state1Evaluation = state1.getDistance() + state1.getManhattanDistance();
        int state2Evaluation = state2.getDistance() + state2.getManhattanDistance();

        if (state1Evaluation == state2Evaluation) {
            return 0;
        }

        if (state1Evaluation > state2Evaluation) {
            return 1;
        }

        return -1;
    }
}

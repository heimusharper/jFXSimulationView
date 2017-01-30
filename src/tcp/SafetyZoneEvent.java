package tcp;

/**
 * Created by boris on 16.01.17.
 */
public class SafetyZoneEvent {
    private double numOfPeople;

    public SafetyZoneEvent(double numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public double getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(double numOfPeople) {
        this.numOfPeople = numOfPeople;
    }
}

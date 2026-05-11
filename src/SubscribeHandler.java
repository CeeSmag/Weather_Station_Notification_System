import java.util.List;
import java.util.Map;

public record SubscribeHandler(Map<DataType, List<Observer>> mapObservers, Observer observer, boolean isSubscribe) {

    private void process(DataType dataType) {
        if (isSubscribe) { // if true will be checked if in the list if not will be added to specific observer-list
            // this will print the exact Observer the want to subscribe.
            System.out.println("*** " + observer.getClass().getSimpleName() + " subscribed to " + dataType + " ***");
            if (mapObservers.containsKey(dataType) && !mapObservers.get(dataType).contains(observer)) {
                mapObservers.get(dataType).add(observer);
            }
        } else {
            // this will print the exact Observer the want to unsubscribe.
            System.out.println("\n*** " + observer.getClass().getSimpleName() + " unsubscribed from " + dataType + " ***\n");
            if (mapObservers.containsKey(dataType)) {
                mapObservers.get(dataType).remove(observer);
            }
        }
    }

    public void condition() {
        process(DataType.CONDITION_TYPE);
    }
    public void temperature() {
        process(DataType.TEMPERATURE_TYPE);
    }
    public void humidity() {
        process(DataType.HUMIDITY_TYPE);
    }
    public void pressure() {
        process(DataType.PRESSURE_TYPE);
    }
    public void all() {
        process(DataType.CONDITION_TYPE);
        process(DataType.TEMPERATURE_TYPE);
        process(DataType.HUMIDITY_TYPE);
        process(DataType.PRESSURE_TYPE);
    }
}
import java.util.List;
import java.util.Map;

public record SubscribeHandler(Map<DataType, List<Observer>> mapObservers, Observer observer, boolean isSubscribe) {

    private void process(DataType dataType) {
        if (isSubscribe) {
            System.out.println("*** " + observer.getClass().getSimpleName() + " subscribed to " + dataType + " ***");
            if (mapObservers.containsKey(dataType) && !mapObservers.get(dataType).contains(observer)) {
                mapObservers.get(dataType).add(observer);
            }
        } else {
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
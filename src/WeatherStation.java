import java.util.*;

public class WeatherStation implements Subject {

    private String condition;
    private float temperature;
    private float humidity;
    private float pressure;

    private volatile boolean isOnline;
    private Thread onlineThread;

    private WeatherAPI weatherAPI;
    private final Map<DataType, List<Observer>> mapObservers = new HashMap<>(); // each DataType is a key for each Observer list

    public WeatherStation(WeatherAPI weatherAPI) {
        this.weatherAPI = weatherAPI;
        // we created ArrayList for each data type (e.g. temp, humidity,...) for any observer to subscribe to.
        mapObservers.put(DataType.CONDITION_TYPE, new ArrayList<>());
        mapObservers.put(DataType.TEMPERATURE_TYPE, new ArrayList<>());
        mapObservers.put(DataType.HUMIDITY_TYPE, new ArrayList<>());
        mapObservers.put(DataType.PRESSURE_TYPE, new ArrayList<>());
    }
    // this set a new weather API for fetching data
    public void setWeatherAPI(WeatherAPI newWeatherAPI) {
        this.weatherAPI = newWeatherAPI;
    }

    // this creates a new object SubscribeHandler and passes (Observers HashMap) and
    // the observer that want to subscribe. the bool (true) means a new subscriber.
    @Override
    public SubscribeHandler subscribeObserver(Observer observer) {
        return new SubscribeHandler(this.mapObservers, observer, true);
    }

    // this creates a new object SubscribeHandler and passes (Observers HashMap) and
    // the observer that want to unsubscribe. the bool (false) means the observer want to unsubscribe.
    @Override
    public SubscribeHandler unsubscribeObserver(Observer observer) {
        return new SubscribeHandler(this.mapObservers, observer, false);
    }

    // this method in (setStationOnline) is just a caller to other methods.
    private void caller() {
        System.out.println("=====================================");
        weatherAPI.fetchData();
        dataChecker();
    }

    private void dataChecker() { // checks each dataType, if different will update WeatherStation instance variables
        if (!Objects.equals(condition, weatherAPI.condition())) {// Object.equal() we this because we are comparing two Strings.
            this.condition = weatherAPI.condition();
            notifyObservers(DataType.CONDITION_TYPE);
        }
        if (temperature != weatherAPI.temperature()) {
            this.temperature = weatherAPI.temperature();
            notifyObservers(DataType.TEMPERATURE_TYPE);
        }
        if (humidity != weatherAPI.humidity()) {
            this.humidity = weatherAPI.humidity();
            notifyObservers(DataType.HUMIDITY_TYPE);
        }
        if (pressure != weatherAPI.pressure()) {
            this.pressure = weatherAPI.pressure();
            notifyObservers(DataType.PRESSURE_TYPE);
        }
    }

    // notify a specific observer-list based on the dataType
    @Override
    public void notifyObservers(DataType dataType) {
        Object data; // the Object is a Generic Container, and it can hold any data a (String or float or..).
        switch (dataType) {
            case CONDITION_TYPE:
                data = condition;
                break;
            case TEMPERATURE_TYPE:
                data = temperature;
                break;
            case HUMIDITY_TYPE:
                data = humidity;
                break;
            case PRESSURE_TYPE:
                data = pressure;
                break;
            default:
                data = null;
                break;
        }

        for (Observer observer : mapObservers.get(dataType)) {
            observer.update(dataType, data);
        }
    }

    // this method will turn the WeatherStation on and will keep it on the loop until off() method is called
    private void setStationOnline(boolean online, int RefreshRateInMilSec) {
        if (online) {
            if (!isOnline) {
                isOnline = true;
                onlineThread = new Thread(() -> {
                    while (isOnline && !Thread.currentThread().isInterrupted()) {
                        caller();
                        try {
                            Thread.sleep(RefreshRateInMilSec);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
                onlineThread.start();
                System.out.println("*** Weather Station Is Online ***");
            }
        } else {
            this.isOnline = false;
            if (onlineThread != null) {
                onlineThread.interrupt();
            }
            System.out.println("*** Weather Station Going Offline... ***");
        }
    }

    public void on(int RefreshRateInMilSec) {
        setStationOnline(true, RefreshRateInMilSec);
    }

    public void off() {
        setStationOnline(false, 1000);
    }
}
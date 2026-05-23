import java.util.*;

public class WeatherStation implements Subject {

    private String condition;
    private float temperature;
    private float humidity;
    private float pressure;

    private volatile boolean isOnline;
    private Thread onlineThread;

    private WeatherAPI weatherAPI;
    private final Map<DataType, List<Observer>> mapObservers = new HashMap<>();

    public WeatherStation(WeatherAPI weatherAPI) {
        this.weatherAPI = weatherAPI;

        mapObservers.put(DataType.CONDITION_TYPE, new ArrayList<>());
        mapObservers.put(DataType.TEMPERATURE_TYPE, new ArrayList<>());
        mapObservers.put(DataType.HUMIDITY_TYPE, new ArrayList<>());
        mapObservers.put(DataType.PRESSURE_TYPE, new ArrayList<>());
    }

    public void setWeatherAPI(WeatherAPI newWeatherAPI) {
        this.weatherAPI = newWeatherAPI;
    }

    @Override
    public SubscribeHandler subscribeObserver(Observer observer) {
        return new SubscribeHandler(this.mapObservers, observer, true);
    }


    @Override
    public SubscribeHandler unsubscribeObserver(Observer observer) {
        return new SubscribeHandler(this.mapObservers, observer, false);
    }

    private void caller() {
        System.out.println("=====================================");
        weatherAPI.fetchData();
        dataChecker();
    }

    private void dataChecker() {
        if (!Objects.equals(condition, weatherAPI.condition())) {
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


    @Override
    public void notifyObservers(DataType dataType) {
        Object data;
        switch (dataType) {
            case CONDITION_TYPE -> data = condition;
            case TEMPERATURE_TYPE -> data = temperature;
            case HUMIDITY_TYPE -> data = humidity;
            case PRESSURE_TYPE -> data = pressure;
            default -> data = null;
        }

        for (Observer observer : mapObservers.get(dataType)) {
            observer.update(dataType, data);
        }
    }

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
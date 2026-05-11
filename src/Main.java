public class Main {
    public static void main(String[] args) {
        WeatherAPI dataSource = new APIClient(); // Real world weather data.
        WeatherStation weatherStation = new WeatherStation(dataSource);
        weatherStation.setWeatherAPI(new APITester()); // Random weather data for testing.

        PhoneDisplay phoneDisplay = new PhoneDisplay(weatherStation);
        WebDisplay webDisplay = new WebDisplay(weatherStation);

        weatherStation.on(5000); // the number is the refresh rate to fetch new data
        sleep(6000);
        weatherStation.unsubscribeObserver(webDisplay).temperature(); // [unsubscribeObserver().temperature()] this called method chaining
        sleep(6000);
        weatherStation.unsubscribeObserver(phoneDisplay).humidity();
        sleep(6000);
        weatherStation.subscribeObserver(webDisplay).temperature();
        sleep(8000);
        weatherStation.off();

    }

    public static void sleep(int milSec) {
        try {
            Thread.sleep(milSec);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("*** error sleep main ***");
        }
    }

}
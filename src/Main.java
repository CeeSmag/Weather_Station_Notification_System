public class Main {
    public static void main(String[] args) {
        WeatherAPI dataSource = new APIClient();
        WeatherStation weatherStation = new WeatherStation(dataSource);

        PhoneDisplay phoneDisplay = new PhoneDisplay(weatherStation);
        WebDisplay webDisplay = new WebDisplay(weatherStation);

        weatherStation.on(5000);
        sleep(6000);
        weatherStation.unsubscribeObserver(webDisplay).temperature();
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
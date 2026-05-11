public class WebDisplay implements Observer {

    private String condition;
    private float temperature;
    private float humidity;
    private float pressure;

    private int updateType;

    public WebDisplay(Subject subject) {
        subject.subscribeObserver(this).all(); // this here will subscribe the Observer to all notification.
    }

    @Override
    public void update(DataType dataType, Object data) {

        switch (dataType) {
            case CONDITION_TYPE:
                this.condition = (String) data;
                updateType = 1;
                break;
            case TEMPERATURE_TYPE:
                this.temperature = (Float) data;
                updateType = 2;
                break;
            case HUMIDITY_TYPE:
                this.humidity = (Float) data;
                updateType = 3;
                break;
            case PRESSURE_TYPE:
                this.pressure = (Float) data;
                updateType = 4;
                break;
        }
        display();
    }

    public void display() {

        if (updateType == 1) {
            System.out.println("Web Display received notification -> Condition: " + condition);
        } else if (updateType == 2) {
            System.out.println("Web Display received notification -> Temperature: " + temperature + " °C");
        } else if (updateType == 3) {
            System.out.println("Web Display received notification -> Humidity: " + humidity + "%");
        } else if (updateType == 4) {
            System.out.println("Web Display received notification -> Pressure: " + pressure + " hPa");
        }
    }
}
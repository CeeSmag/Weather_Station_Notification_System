public interface Subject {
    SubscribeHandler subscribeObserver(Observer o); // this is a method chain calling.
    SubscribeHandler unsubscribeObserver(Observer o);
    void notifyObservers(DataType dataType); // the dataType is an enum reference type.
}
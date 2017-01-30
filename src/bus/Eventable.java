package bus;

/**
 * Created by boris on 16.01.17.
 */
public interface Eventable {

    default void registeredOnBus(Object object) {
        EBus.register(object);
    }
}

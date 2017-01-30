/*******************************************************************************
 * Copyright (C) 2016 Chirkov Boris <b.v.chirkov@udsu.ru>
 *
 * Project website:       http://eesystem.ru
 * Organization website:  http://rintd.ru
 *
 * --------------------- DO NOT REMOVE THIS NOTICE -----------------------------
 * EBus is part of jSimulationMoving.
 *
 * jSimulationMoving is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jSimulationMoving is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSimulationMoving. If not, see <http://www.gnu.org/licenses/>.
 * -----------------------------------------------------------------------------
 *
 * This code is in BETA; some features are incomplete and the code
 * could be written better.
 ******************************************************************************/

package bus;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Шина событиый {@link EventBus}
 * <p>
 * Initialization-on-demand holder idiom
 * <p>
 * Created by boris on 15.12.16.
 */
public class EBus {

    private static final Logger log = LoggerFactory.getLogger(EBus.class);

    /**
     * Регистрирует класс (вешает слушателя на шину), в котором есть
     * методы-обработчики событий
     *
     * @param object объект (класса)
     */
    public static void register(Object object) {
        log.debug("Register {} on the EVENT BUS", object.getClass().getSimpleName());
        EBus.getInstance().register(object);
    }

    /**
     * Снимает с регистрации класс с методами-обработчиками событий
     *
     * @param object объект (класса)
     */
    public static void unregister(Object object) {
        log.debug("Unregistered {} on the EVENT BUS", object.getClass().getSimpleName());
        EBus.getInstance().unregister(object);
    }

    /**
     * Инициализация события. Сообщение о событии на шину
     *
     * @param obj сообщение
     */
    public static void post(Object obj) {
        log.debug("Post obj type of {} class", obj.getClass().getSimpleName());
        EBus.getInstance().post(obj);
    }

    /**
     * @return Экземпляр шины {@link EventBus}
     */
    private static EventBus getInstance() {
        return EBusHelper.BUS_INSTANCE;
    }

    private static class EBusHelper {
        private static final EventBus BUS_INSTANCE;

        static {
            BUS_INSTANCE = new EventBus(EBusHelper.class.getName());
            LoggerFactory.getLogger(EBusHelper.class).info("Create instance bus events");
        }
    }

}

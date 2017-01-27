/******************************************************************************
 Copyright (C) 2016 Galiullin Marat, Chirkov Boris <b.v.chirkov@udsu.ru>

 Project website:       http://eesystem.ru
 Organization website:  http://rintd.ru

 --------------------- DO NOT REMOVE THIS NOTICE ------------------------------
 This file is part of SimulationMoving.

 SimulationMoving is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 SimulationMoving is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with SimulationMoving. If not, see <http://www.gnu.org/licenses/>.
 ------------------------------------------------------------------------------

 This code is in BETA; some features are incomplete and the code
 could be written better.
 *****************************************************************************/

package json.geometry;

import java.util.ArrayList;

/**
 * Класс используется для десериализации стандарта BECS.json
 *
 * @author mag
 */
public abstract class BIM<R extends Room, T extends Transition> {

    /**
     * Общее название здания
     */
    private String name;

    /**
     * Адресные данные
     */
    private Address address;

    /**
     * Массив помещений в здании
     */
    private ArrayList<R> rooms;

    /**
     * Массив переходов/дверей/проемов в здании
     */
    private ArrayList<T> transitions;

    // --------------------------------------------------
    @Override
    public String toString() {
        return "Name:\t" + name + "\n" + address + "\n" + "Number of " +
                "rooms:\t" +  rooms.size() + "\n" + "Number of " +
                "transitions:\t"  + transitions.size() + "\n";
    }

    /**
     * @return Имя здания
     */
    public String getName() {
        return name;
    }

    /**
     * Позволяет изменять имя здания
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name == null ? "" : name;
    }

    /**
     * @return Адресные данные здания
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Позволяет изменть адресные данные здания
     *
     * @param address - объект класса {@link Address}
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /**
     * @return Списко помещений здания
     */
    public ArrayList<R> getRooms() {
        return rooms;
    }

    /**
     * @param i - номер помещения
     * @return Помещение под номером i
     */
    public R getRoom(final int i) {
        return rooms.get(i);
    }

    /**
     * @return Список проемов
     */
    public ArrayList<T> getTransitions() {
        return transitions;
    }

    /**
     * @param i - номер проема
     * @return Проем с номером i
     */
    public T getTransitions(final int i) {
        return transitions.get(i);
    }

}
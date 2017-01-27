/******************************************************************************
 Copyright (C) 2016 Galiullin Marat, Boris Chirkov <b.v.chirkov@udsu.ru>

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

/**
 * Класс указания адреса здания
 * Created by boris on 17.12.16.
 */
public class Address {

    /**
     * Улица, дом.
     */
    private String street;
    /**
     * Город.
     */
    private String city;
    /**
     * Дополнительная информация.
     */
    private String addInfo;

    /**
     * @return Улица, на которой расположено здание
     */
    public String getStreet() {
        return street;
    }

    /**
     * Позволяет задать улицу, на которой разположено здание
     *
     * @param street - название улицы
     */
    public void setStreet(final String street) {
        this.street = street == null ? "" : street;
    }

    /**
     * @return Название города, где расположено здание
     */
    public String getCity() {
        return city;
    }

    /**
     * Позволяет изменить название города, где расположено здание
     *
     * @param city - название города
     */
    public void setCity(final String city) {
        this.city = city == null ? "" : city;
    }

    /**
     * @return Дополнительная информация о здании
     */
    public String getAddInfo() {
        return addInfo;
    }

    /**
     * Позволяет изменить дополнительную информацию о здании
     *
     * @param addInfo - доплнительная информация
     */
    public void setAddInfo(final String addInfo) {
        this.addInfo = addInfo == null ? "" : addInfo;
    }

    @Override
    public String toString() {
        return "Address {" + "street='" + street + '\'' + ", city='" + city +
                '\'' + ", addInfo='" + addInfo + '\'' + '}';
    }
}

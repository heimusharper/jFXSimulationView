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
 * Класс, описывающий зону внутри помещения
 *
 * @param <L> Класс, расширенный {@link Light}
 * @param <S> Класс, расширенный {@link Sensor}
 * @param <P> Класс, расширенный {@link Speaker}
 *
 * @author mag
 */
public abstract class Zone<L extends Light, S extends Sensor, P extends Speaker> {
    public static final int FLOOR   = 0; // Обыное помещение
    public static final int STAIRS  = 1; // Лестничная площадка
    public static final int UNKNOWN = -1; // Неопределенный тип
    /**
     * Идетнификатор зоны в формате UUID. <br>
     * <hr>
     * UUID представляет собой 16-байтный (128-битный) номер. <br>
     *
     * @Example В шестнадцатеричной системе счисления UUID выглядит как:<br>
     * 550e8400-e29b-41d4-a716-446655440000
     */
    private String       id;
    /**
     * Высота потолка зоны в метрах
     */
    private double       ceilingHeight;
    /**
     * Тип зоны по пожарной нагрузке. Цифровой идентификатор вида пространства.
     * <ol>
     * <li>Жилые помещения гостиниц, общежитий и т. д.</li>
     * <li>Столовая, буфет, зал ресторана</li>
     * <li>Зал театра, кинотеатра, клуба, цирка</li>
     * <li>Гардероб</li>
     * <li>Хранилища библиотек, архивы</li>
     * <li>Музеи, выставки</li>
     * <li>Подсобные и бытовые помещения, лестничная клетка</li>
     * <li>Административные помещения, учебные классы школ, ВУЗов, кабинеты поликлиник</li>
     * <li>Магазины</li>
     * <li>Зал вокзала</li>
     * <li>Стоянки легковых автомобилей</li>
     * <li>Стоянки легковых автомобилей (с двухуровневым хранением)</li>
     * <li>Стадионы</li>
     * <li>Спортзалы</li>
     * <li>Торговый зал гипермаркета</li>
     * </ol>
     */
    private int          fireType;
    /**
     * Количество людей в зоне
     */
    private double       numOfPeople;
    /**
     * Примечание. Дополнительная информация.
     */
    private String       note;
    /**
     * Тип зоны
     */
    private String       type;
    /**
     * Геометрия зоны.<br>
     * [кольца][точки][x,y,z]
     */
    private double[][][] xyz;
    /**
     * Светофоры и указатели, расположенные в данной зоне
     */
    private ArrayList<L> lights;
    /**
     * Сенсоры, расположенные в зоне
     */
    private ArrayList<S> sensors;
    /**
     * Аудио оповещатели, расположенные в зоне
     */
    private ArrayList<P> speakers;

    protected Zone() {
    }

    @Override public String toString() {
        return "Zone: {" + "id='" + id + '\'' + ", ceilingHeight="
                + ceilingHeight + ", fireType=" + fireType + ", numOfPeople="
                + numOfPeople + ", note='" + note + '\'' + ", type='" + type
                + '\'' + ", lights=" + lights + ", sensors=" + sensors + ", "
                + "speakers=" + speakers + '}';
    }

    /**
     * Метод возвращает тип зоны, который задается на этапе ввода здания
     *
     * @return 0, если зона имеет тип FLOOR <br> 1, если зона имеет тим STAIRS
     * <br> -1, если тип зоны не определен (UNKNOWN)
     */
    public int getType() {
        switch (type) {
        case "FLOOR":
            return FLOOR;
        case "STAIRS":
            return STAIRS;
        default:
            return UNKNOWN;
        }
    }

    /**
     * Позволяет установить тип зоны.
     *
     * @param type тип зоны: {@link Zone#FLOOR} или {@link Zone#STAIRS}
     */
    public void setType(final int type) {
        switch (type) {
        case FLOOR:
            this.type = "FLOOR";
            break;
        case STAIRS:
            this.type = "STAIRS";
            break;
        default:
            this.type = "UNKNOWN";
            break;
        }
    }

    /**
     * @return Идентификатор зоны в формате UUID.
     */
    public String getId() {
        return id;
    }

    /**
     * Позволяет изментить идентификатор зоны
     *
     * @param id - идентификатор в формете UUID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Расстояние от пола до потолка
     */
    public double getCeilingHeight() {
        return ceilingHeight;
    }

    /**
     * Позволяет изменить расстояние от пола до потолка
     *
     * @param ceilingHeight - расстояние, м.
     */
    public void setCeilingHeight(double ceilingHeight) {
        this.ceilingHeight = ceilingHeight;
    }

    /**
     * @return Тип пожарной нагрузки
     */
    public int getFireType() {
        return fireType;
    }

    /**
     * Позволяет изменить тип пожарной нагрузки в зоне
     *
     * @param fireType - номер из фиксированного списка
     */
    public void setFireType(int fireType) {
        this.fireType = fireType;
    }

    /**
     * @return Количество людей в зоне
     */
    public double getNumOfPeople() {
        return numOfPeople;
    }

    /**
     * Позволяет изменить количество людей в зоне
     *
     * @param numOfPeople - количество людей
     */
    public void setNumOfPeople(double numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    /**
     * @return Описание зоны. Примечание.
     */
    public String getNote() {
        return note;
    }

    /**
     * Позволяет изменить описание зоны
     *
     * @param note - описание
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return Трехмерный массив, описывающий геометрию зоны. <br>
     * [кольца][точки][x,y,z]
     */
    public double[][][] getXyz() {
        return xyz;
    }

    /**
     * Позволяет изменить геометрию зоны
     *
     * @param xyz - [кольца][точки][x,y,z]
     */
    public void setXyz(double[][][] xyz) {
        this.xyz = xyz;
    }

    /**
     * @param i - кольца
     * @param j - точки
     * @param k - x,y,z
     *
     * @return Трехмерный массив, описывающий геометрию зоны.
     */
    public double getXyz(int i, int j, int k) {
        return xyz[i][j][k];
    }

    /**
     * @return Списко световых указателей, которые находятся в данной зоне
     */
    public ArrayList<L> getLights() {
        return lights;
    }

    /**
     * @return Список устройств мониторинга состояния среды, которые находятся в
     * данной зоне
     */
    public ArrayList<S> getSensors() {
        return sensors;
    }

    /**
     * @return Список речевых оповещателей, которые находятся в данной зоне
     */
    public ArrayList<P> getSpeakers() {
        return speakers;
    }

}

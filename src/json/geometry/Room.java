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
 * Класс, описывающий помещение.
 * Каждое помещение, в свою очередь, состоит из зон.
 *
 * @param <Z> Класс, расширенный {@link Zone}
 * @author mag
 */
public abstract class Room<Z extends Zone> {

    /**
     * Наименование помещения
     */
    private String name;

    /**
     * Идетнификатор помещения в формате UUID. <br>
     * <hr>
     * UUID представляет собой 16-байтный (128-битный) номер. <br>
     *
     * @Example В шестнадцатеричной системе счисления UUID выглядит как:<br>
     * 550e8400-e29b-41d4-a716-446655440000
     */
    private String id;

    /**
     * Высота потолка помещения, в метрах. Используется для внутренних зон, как значение по-умолчанию
     */
    private double ceilingHeight;

    /**
     * Тип помещения по пожарной нагрузке. Используется для внутренних зон, как значение по-умолчанию
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
    private int fireType;

    /**
     * Количество людей в помещении. Используется для внутренних зон, как пропорциональное по площади значение
     * по-умолчанию.
     */
    private int numOfPeople;

    /**
     * Примечание. Дополнительная информация.
     */
    private String note;

    /**
     * Геометрия всего помещения.<br>
     * [кольца][точки][x,y,z]
     * геометрия целого помещения. (В зонах приводится геометрия отдельных зон)
     */
    private double[][][] xyz;

    /**
     * Список зон в данном помещении
     */
    private ArrayList<Z> zones;

    @Override
    public String toString() {
        return "Room{" + "name='" + name + '\'' + ", id='" + id + '\'' + ", "
                + "ceilingHeight=" + ceilingHeight + ", fireType=" + fireType
                + ", numOfPeople=" + numOfPeople + ", note='" + note + '\'' +
                ", zones=" + zones + '}';
    }

    /**
     * @return Название помещения
     */
    public String getName() {
        return name;
    }

    /**
     * Позволяет сменить название помещения
     *
     * @param name - название помещения
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Идентификатор помещения в формате UUID
     */
    public String getUUID() {
        return id;
    }

    /**
     * @return Высота помещения
     */
    public double getCeilingHeight() {
        if (this.ceilingHeight == 0)
            throw new Error("Fail! Ceiling height is 0");
        return ceilingHeight;
    }

    /**
     * Позволяет изменить высоту помещения
     *
     * @param ceilingHeight - высота помещения. Нельзя задать значение <= 0
     */
    public void setCeilingHeight(double ceilingHeight) {
        if (ceilingHeight <= 0)
            throw new NumberFormatException("Warning! You" + " can not ceiling height set to 0");
        this.ceilingHeight = ceilingHeight;
    }

    /**
     * @return Тип пожарной нагрузки
     * <p>
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
    public int getFireType() {
        return fireType;
    }

    /**
     * Позволяет задать тип пожарной нагрузки
     *
     * @param fireType - тип пожарной нагрузки
     */
    public void setFireType(final int fireType) {
        this.fireType = fireType;
    }

    /**
     * @return Количество людей в помещении
     */
    public int getNumOfPeople() {
        return numOfPeople;
    }

    /**
     * Позволяет установить количество людей в помещении
     *
     * @param numOfPeople - количество людей. Не может быть меньше нуля
     */
    public void setNumOfPeople(final int numOfPeople) {
        if (numOfPeople < 0)
            throw new NumberFormatException("Warning! You" + " can not number of people set to -" + numOfPeople);
        this.numOfPeople = numOfPeople;
    }

    /**
     * @return true - если в помещении нет людей <br>
     * false - если в помещении есть люди
     */
    public boolean isEmpty() {
        return numOfPeople == 0;
    }

    /**
     * @return Описание
     */
    public String getNote() {
        return note;
    }

    /**
     * Позволяет изменить описание
     *
     * @param note - текст
     */
    public void setNote(final String note) {
        this.note = note;
    }

    /**
     * @param i - кольца
     * @param j - точки
     * @param k - x, y, z
     * @return Геометрия всего помещения.<br>
     * геометрия целого помещения. (В зонах приводится геометрия отдельных зон)
     */
    public double getXyz(final int i, final int j, final int k) {
        return xyz[i][j][k];
    }

    /**
     * @param j - точки
     * @param k - x, y, z
     * @return Геометрия всего помещения, считая, что отсутствуют
     * внутренние кольца<br>
     * геометрия целого помещения. (В зонах приводится геометрия отдельных зон)
     */
    public double getXyz(final int j, final int k) {
        return xyz[0][j][k];
    }

    /**
     * @return true - если в геометрии помещения имеются внутренние кольца <br>
     * false - если в геометрии помещения отсутствуют внутренние
     * кольца <br>
     * Внутреннее кольцо - нерасчетная област внутри помещения (например:
     * колонны посреди помещения)
     */
    public boolean isInternalRing() {
        return xyz.length >= 1;
    }

    /**
     * @return Геометрия всего помещения. Трехмерный массив
     * [кольца][точки][x,y,z] <br>
     * геометрия целого помещения. (В зонах приводится геометрия отдельных зон)
     */
    public double[][][] getXyz() {
        return xyz;
    }

    /**
     * @return Список всех зон
     */
    public ArrayList<Z> getZones() {
        return zones;
    }

    /**
     * @param i - номер зоны
     * @return i-тую зону, которая содержится в помещении
     */
    public Z getZone(final int i) {
        return zones.get(i);
    }
}
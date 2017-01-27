/*******************************************************************************
 * Copyright (C) 2016 Chirkov Boris <b.v.chirkov@udsu.ru>
 *
 * Project website:       http://eesystem.ru
 * Organization website:  http://rintd.ru
 *
 * --------------------- DO NOT REMOVE THIS NOTICE -----------------------------
 * TransitionExt is part of jSimulationMoving.
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

package json.extendetGeometry;

import json.geometry.Transition;

/**
 * Класс, расширяющий базовый {@link Transition}.
 * Предназначен для полей, которые не входят в *.json файл с геометрией
 * <p>
 * Created by boris on 17.12.16.
 */
public class TransitionExt extends Transition {
    {
        setNumOfPeoplePassing(0);
        setNTay(0);
    }
    /**
     * Количество людей, прошедших через дверь
     */
    private double numOfPeoplePassing;
    /**
     * Время обработки портала N, штуки  (в единицах tay, мин)
     * Признак обработки.Если nTay совпадают, то в данный момент оба портала уже обработаны 
     */
    private int    nTay;
    /**
     * Номер выхода. Этот номер присваивается эвакуационным выходам в порядке
     * их использования
     */
    private int    numberExit;

    /**
     * @return true - если одна из зон, которые соединяет проем, NULL
     */
    public boolean hasNullZone() {
        return getZoneAId() == null || getZoneBId() == null;
    }

    public boolean isExit() {
        return hasNullZone();
    }

    /**
     * Позволяет увеличить счетчик количества людей, которые прошли через дверь
     *
     * @param people количество людей
     */
    public void addPassingPeople(double people) {
        setNumOfPeoplePassing(getNumOfPeoplePassing() + people);
    }

    /**
     * @return Количество людей, прошедших через дверь
     */
    public double getNumOfPeoplePassing() {
        return numOfPeoplePassing;
    }

    /**
     * Позволяет изменить количество людей, прошедших через дверь
     *
     * @param numOfPeoplePassing - количество людей
     */
    public void setNumOfPeoplePassing(double numOfPeoplePassing) {
        this.numOfPeoplePassing = numOfPeoplePassing;
    }

    public void nTayIncrease() {
        setNTay(getNTay() + 1);
    }

    public int getNTay() {
        return nTay;
    }

    public void setNTay(int nTay) {
        this.nTay = nTay;
    }

    /**
     * @return Номер эвакуационного выхода
     */
    public int getNumberExit() {
        return numberExit;
    }

    /**
     * Позволяет присвоить номер эвакуационному выходу
     *
     * @param numberExit номер
     */
    public void setNumberExit(int numberExit) {
        this.numberExit = numberExit;
    }
}

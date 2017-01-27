/*******************************************************************************
 * Copyright (C) 2016 Chirkov Boris <b.v.chirkov@udsu.ru>
 *
 * Project website:       http://eesystem.ru
 * Organization website:  http://rintd.ru
 *
 * --------------------- DO NOT REMOVE THIS NOTICE -----------------------------
 * SafetyZone is part of jSimulationMoving.
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

import json.geometry.Zone;

import java.util.ArrayList;

/**
 * Безопасная зона. Выделена в отделный объект для удобства и разделения
 * функционала (есть поля, которые не нужны классу {@link ZoneExt}).
 * Не имеет пожарной нагрузки и размеров.
 * <p>
 * Created by boris on 23.12.16.
 */
public class SafetyZone extends ZoneExt {

    private ArrayList<Double> minsZ = new ArrayList<>();

    // Инициализация параметров
    {
        setId("sz0");
        setCeilingHeight(3.0);
        setNumOfPeople(0);
        setNote("Safety Zone");
        setType(Zone.FLOOR);
        setSafetyZone(true);
    }

    /**
     * Позволяет добавить новую точку в зону. Считается, что безопасная зона
     * находится на одном уровне с ближайшей к выходу.
     *
     * @param z - координата оси Z
     */
    public void addMinZ(double z) {
        minsZ.add(z);
    }

    /**
     * @param tid номер двери из списка эвакуационных выходов
     *
     * @return значение уровня, на котором находится безопасная зона для
     * заданного выхода
     */
    public double getMinZ(int tid) {
        return minsZ.get(tid);
    }

}

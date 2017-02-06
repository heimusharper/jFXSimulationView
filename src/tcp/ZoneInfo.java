/*
 * Copyright (C) 2017 Chirkov Boris <b.v.chirkov@udsu.ru>
 *
 * Project website:       http://eesystem.ru
 * Organization website:  http://rintd.ru
 *
 * --------------------- DO NOT REMOVE THIS NOTICE -----------------------------
 * CounterPeopleHandler is part of jSimulationMoving.
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
 */

package tcp;

/**
 * Created by boris on 15.01.17.
 */
public class ZoneInfo {

    public static final int PEOPLE       = 0;
    public static final int PERMEABILITY = 1;

    private String zid;
    private int    type;
    private double numOfPeople;
    private double permeability;

    public String getZid() {
        return zid;
    }

    public double getNumOfPeople() {
        return numOfPeople;
    }

    public double getPermeability() {
        return permeability;
    }

    public int getType() {
        return type;
    }
}

/******************************************************************************
 Copyright (C) 2016 Galiullin Marat

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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Класс, описывающий беспроводное устройство - сенсор с датчиками.
 *
 * @author mag
 */
public abstract class Sensor {
    /**
     * Идетнификатор сенсора в формате UUID. <br>
     * <hr>
     * UUID представляет собой 16-байтный (128-битный) номер. <br>
     *
     * @Example В шестнадцатеричной системе счисления UUID выглядит как:<br>
     * 550e8400-e29b-41d4-a716-446655440000
     */
    private String id;

    /**
     * Координаты установки сенсора.
     */
    private double x, y, z;

    /**
     * Идентификатор беспроводного узла
     */
    private long deviceId;

    /**
     * Дополнительная информация
     */
    private String note;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

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

/**
 * Класс, описывающий проемы - двери, границы зон.
 *
 * @author mag
 */
public abstract class Transition {
    /**
     * Идетнификатор перехода в формате UUID. <br>
     * <hr>
     * UUID представляет собой 16-байтный (128-битный) номер. <br>
     *
     * @Example В шестнадцатеричной системе счисления UUID выглядит как:<br>
     * 550e8400-e29b-41d4-a716-446655440000
     */
    private String id;

    /**
     * Высота проема.
     * Опционально.
     */
    private double doorHeight;

    /**
     * Ширина проема
     */
    private double width;

    /**
     * Примечания, дополнительная информация.
     */
    private String note;

    /**
     * Ссылки (UUID) на связанные с данным переходом две зоны - A и B
     */
    private String zoneAId;
    private String zoneBId;

    /**
     * Геометрия проема. Может быть полигоном или полилинией.
     * Если первая точка равна последней, значит это полигон.
     * Колец нет.
     * [точки][x,y,z]
     */
    private double[][] xyz;

    /**
     * Ссылки (UUID) на Светофоры и указатели, привязанные к переходу со
     * стороны помещения A
     */
    private String[] lightsA;

    /**
     * Ссылки (UUID) на Светофоры и указатели, привязанные к переходу со
     * стороны помещения B
     */
    private String[] lightsB;

    @Override
    public String toString() {
        return "Transition {" + "id='" + id + '\'' + ", doorHeight=" +
                doorHeight + ", width=" + width + ", note='" + note + '\'' +
                ", zoneAId='" + zoneAId + '\'' + ", zoneBId='" + zoneBId +
                '\'' + ", num of lightsA=" + lightsA.length + ", " +
                "num of lightsB=" + lightsB.length + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDoorHeight() {
        return doorHeight;
    }

    public void setDoorHeight(double doorHeight) {
        this.doorHeight = doorHeight;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getZoneAId() {
        return zoneAId;
    }

    public void setZoneAId(String zoneAId) {
        this.zoneAId = zoneAId;
    }

    public String getZoneBId() {
        return zoneBId;
    }

    public void setZoneBId(String zoneBId) {
        this.zoneBId = zoneBId;
    }

    public double[][] getXyz() {
        return xyz;
    }

    public void setXyz(double[][] xyz) {
        this.xyz = xyz;
    }

    public double getXyz(int i, int j) {
        return xyz[i][j];
    }

    public String[] getLightsA() {
        return lightsA;
    }

    public void setLightsA(String[] lightsA) {
        this.lightsA = lightsA;
    }

    public String getLightsA(int i) {
        return lightsA[i];
    }

    public String[] getLightsB() {
        return lightsB;
    }

    public void setLightsB(String[] lightsB) {
        this.lightsB = lightsB;
    }

    public String getLightsB(int i) {
        return lightsB[i];
    }
}

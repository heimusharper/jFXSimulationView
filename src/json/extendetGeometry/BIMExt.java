/*******************************************************************************
 * Copyright (C) 2016 Chirkov Boris <b.v.chirkov@udsu.ru>
 *
 * Project website:       http://eesystem.ru
 * Organization website:  http://rintd.ru
 *
 * --------------------- DO NOT REMOVE THIS NOTICE -----------------------------
 * BIMExt is part of jSimulationMoving.
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

import json.geometry.BIM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс, расширяющий базовый {@link BIM}.
 * Фактически - это само здание.
 * Предназначен для полей, которые не входят в *.json файл с геометрией
 * <p>
 * Created by boris on 17.12.16.
 */
public class BIMExt extends BIM<RoomExt, TransitionExt> {
    private static final Logger log = LoggerFactory.getLogger(BIMExt.class);
    /**
     * Безопасная зона.
     * Выделена отдельно. Необходима для работы моделирования. Является
     * вершиной графа.
     */
    private SafetyZone safetyZone;

    // ------ Характеристики здания : START ------
    /**
     * Список зон в здании
     */
    private HashMap<String, ZoneExt> zones;
    /**
     * Список эвакуационных выходов
     */
    private List<TransitionExt>      exitsTransition;

    /**
     * Количество людей в здании
     */
    private double numOfPeople;
    /**
     * Количество выходов из здания
     */
    private int    numOfExits;

    // ------ Характеристики здания : END ------

    /**
     * Пток зон
     */
    private Stream<ZoneExt>       zonesStream;
    /**
     * Пток проемов
     */
    private Stream<TransitionExt> transitionStream;

    /**
     * @return Список зон
     */
    public HashMap<String, ZoneExt> getZones() {
        if (zones != null) return zones;

        List<ZoneExt> zonesList = getZonesStream().collect(Collectors.toList());
        zones = new HashMap<>(zonesList.size());
        zonesList.forEach(z -> zones.put(z.getId(), z));

        // Заполняем связь помещения с дверьми
        getTransitions().forEach(t -> {
            String zidA = t.getZoneAId();
            String zidB = t.getZoneBId();
            if (zidA != null) zones.get(zidA).addTransition(t);
            if (zidB != null) zones.get(zidB).addTransition(t);
        });

        getSafetyZone();

        zones.put(safetyZone.getId(), safetyZone);
        return zones;
    }

    /**
     * @return Количество людей в здании
     */
    public double getNumOfPeople() {
        return numOfPeople == 0 ?
                getZonesStream().mapToDouble(ZoneExt::getNumOfPeople).sum() :
                numOfPeople;
    }

    /**
     * @return Количество эвакуационных выходов
     */
    public int getNumOfExits() {
        return numOfExits == 0 ?
                (int) getTransitionStream().filter(TransitionExt::hasNullZone)
                        .count() :
                numOfExits;
    }

    /**
     * @return Список эвакуационных выходов
     */
    public List<TransitionExt> getExitsTransition() {
        if (exitsTransition != null) return exitsTransition;

        exitsTransition = getTransitionStream()
                .filter(TransitionExt::hasNullZone)
                .collect(Collectors.toList());
        return exitsTransition;
    }

    /**
     * @return Экземпляр зоны, которая объявлена как безопасная. Если зона уже
     * создана, то она и вернется
     */
    public SafetyZone getSafetyZone() {
        // Не пересоздаем объект при каждом вызове
        // Обесчпечивает единую безопасную зону для всего проекта
        if (safetyZone != null) return safetyZone;
        safetyZone = new SafetyZone();

        /* Подсовываем безопасной зоне координату Z первой выходной двери
         Безопасная зона граничит с несколькими выходами, которые
         могут быть на разной высоте. */
        for (TransitionExt t : getExitsTransition()) {
            String zidA = t.getZoneAId();
            String zidB = t.getZoneBId();
            double minZ = 0;
            if (zidA != null) minZ = getZones().get(zidA).getMinZ();
            else if (zidB != null) minZ = getZones().get(zidB).getMinZ();
            safetyZone.addMinZ(minZ);
        }

        log.info("Successful create Safety {}", safetyZone);
        return safetyZone;
    }

    /**
     * Позволяет открыть поток зон
     */
    private void setZonesStream() {
        /* FlatMap преобразует каждый элемент потока в поток других объектов */
        zonesStream = getRooms().stream().flatMap(r -> r.getZones().stream());
    }

    /**
     * @return Поток зон <br> Создается или возвращается существующий
     */
    private Stream<ZoneExt> getZonesStream() {
        setZonesStream();
        return zonesStream;
    }

    /**
     * Позволяет открыть поток проемов
     */
    private void setTransitionStream() {
        transitionStream = getTransitions().stream()
                .filter(TransitionExt::hasNullZone);
    }

    /**
     * @return Поток проемов <br> Создается или возвращается существующий
     */
    private Stream<TransitionExt> getTransitionStream() {
        setTransitionStream();
        return transitionStream;
    }
}

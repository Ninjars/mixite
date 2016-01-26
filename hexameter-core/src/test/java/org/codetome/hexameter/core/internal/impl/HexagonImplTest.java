package org.codetome.hexameter.core.internal.impl;

import org.codetome.hexameter.core.api.AbstractSatelliteData;
import org.codetome.hexameter.core.api.AxialCoordinate;
import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.Point;
import org.codetome.hexameter.core.api.SatelliteData;
import org.codetome.hexameter.core.internal.SharedHexagonData;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.round;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.codetome.hexameter.core.api.AxialCoordinate.fromCoordinates;
import static org.codetome.hexameter.core.api.Point.fromPosition;
import static org.codetome.hexameter.core.internal.impl.HexagonImpl.newHexagon;

public class HexagonImplTest {

    private static final double TEST_RADIUS = 10;
    private static final SharedHexagonData TEST_POINTY_DATA = new SharedHexagonData(HexagonOrientation.POINTY_TOP, TEST_RADIUS);
    private static final SharedHexagonData TEST_FLAT_DATA = new SharedHexagonData(HexagonOrientation.FLAT_TOP, TEST_RADIUS);
    private static final int TEST_GRID_X = 2;
    private static final int TEST_GRID_Z = 3;
    private static final AxialCoordinate TEST_COORDINATE = fromCoordinates(TEST_GRID_X, TEST_GRID_Z);
    private static final int TEST_GRID_Y = -5;
    private static final SatelliteData TEST_SATELLITE_DATA = new AbstractSatelliteData() {
    };
    private static final Map<AxialCoordinate, Object> TEST_SATELLITE_DATA_MAP = new ConcurrentHashMap<>();
    private static final int EXPECTED_POINTY_CENTER_X = 69;
    private static final int EXPECTED_FLAT_CENTER_X = 40;
    private static final int EXPECTED_POINTY_CENTER_Y = 55;
    private static final int EXPECTED_FLAT_CENTER_Y = 78;
    private static final Point[] EXPECTED_FLAT_POINTS = new Point[]{fromPosition(50, 78), fromPosition(45, 87), fromPosition(35, 87), fromPosition(30, 78), fromPosition(35, 69), fromPosition(45, 69)};
    private static final Point[] EXPECTED_POINTY_POINTS = new Point[]{fromPosition(78, 60), fromPosition(69, 65), fromPosition(61, 60), fromPosition(61, 50), fromPosition(69, 45), fromPosition(78, 50)};

    static {
        TEST_SATELLITE_DATA_MAP.put(TEST_COORDINATE, TEST_SATELLITE_DATA);
    }

    private Hexagon target;

    @Before
    public void setUp() {
        target = newHexagon(TEST_POINTY_DATA, TEST_COORDINATE, TEST_SATELLITE_DATA_MAP);
    }

    @Test
    public void shouldReturnProperStringRepresentationOfHexagonWhenToStringCalled() {
        assertEquals("HexagonImpl#{x=" + TEST_GRID_X + ", z=" + TEST_GRID_Z + "}", target.toString());
    }

    @Test
    public void shouldHaveProperPointsWhenPointy() {
        for (int i = 0; i < 6; i++) {
            assertEquals((int) EXPECTED_POINTY_POINTS[i].getCoordinateX(), (int) round(target.getPoints().get(i).getCoordinateX()));
            assertEquals((int) EXPECTED_POINTY_POINTS[i].getCoordinateY(), (int) round(target.getPoints().get(i).getCoordinateY()));
        }
    }

    @Test
    public void shouldHaveProperPointsWhenFlat() {
        target = newHexagon(TEST_FLAT_DATA, TEST_COORDINATE, TEST_SATELLITE_DATA_MAP);
        for (int i = 0; i < 6; i++) {
            assertEquals((int) EXPECTED_FLAT_POINTS[i].getCoordinateX(), (int) round(target.getPoints().get(i).getCoordinateX()));
            assertEquals((int) EXPECTED_FLAT_POINTS[i].getCoordinateY(), (int) round(target.getPoints().get(i).getCoordinateY()));
        }
    }

    @Test
    public void shouldReturnProperSatelliteDataWhenSatelliteDataIsSet() {
        target.setSatelliteData(TEST_SATELLITE_DATA);
        assertEquals(TEST_SATELLITE_DATA, target.getSatelliteData().get());
    }

    @Test
    public void shouldReturnProperSatelliteDataWhenSatelliteDataIsCleared() {
        target.setSatelliteData(TEST_SATELLITE_DATA);
        assertEquals(TEST_SATELLITE_DATA, target.getSatelliteData().get());
        target.clearSatelliteData();
        assertFalse(target.getSatelliteData().isPresent());
    }

    @Test
    public void shouldReturnProperXCoordinateWhenGetGridXIsCalled() {
        assertEquals(TEST_GRID_X, target.getGridX());
    }

    @Test
    public void shouldReturnProperXCoordinateWhenGetGridYIsCalled() {
        assertEquals(TEST_GRID_Y, target.getGridY());
    }

    @Test
    public void shouldReturnProperXCoordinateWhenGetGridZIsCalled() {
        assertEquals(TEST_GRID_Z, target.getGridZ());
    }

    @Test
    public void shouldReturnProperCenterXCoordinateWhenGetCenterXIsCalledWithPointyHexagons() {
        assertEquals(EXPECTED_POINTY_CENTER_X, round(target.getCenterX()));
    }

    @Test
    public void shouldReturnProperCenterXCoordinateWhenGetCenterXIsCalledWithFlatHexagons() {
        target = newHexagon(TEST_FLAT_DATA, TEST_COORDINATE, TEST_SATELLITE_DATA_MAP);
        assertEquals(EXPECTED_FLAT_CENTER_X, round(target.getCenterX()));
    }

    @Test
    public void shouldReturnProperCenterYCoordinateWhenGetCenterYIsCalledWithPointyHexagons() {
        assertEquals(EXPECTED_POINTY_CENTER_Y, round(target.getCenterY()));
    }

    @Test
    public void shouldReturnProperCenterYCoordinateWhenGetCenterYIsCalledWithFlatHexagons() {
        target = newHexagon(TEST_FLAT_DATA, TEST_COORDINATE, TEST_SATELLITE_DATA_MAP);
        assertEquals(EXPECTED_FLAT_CENTER_Y, round(target.getCenterY()));
    }

    @Test
    public void shouldBeEqualToItself() {
        assertEquals(target, target);
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertFalse(target.equals(null));
    }

    @Test
    public void shouldProperlyGetIdWhenGetIdIsCalled() {
        assertEquals(TEST_COORDINATE.toKey(), target.getId());
    }

}

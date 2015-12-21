package org.codetome.hexameter.api;

import static junit.framework.Assert.assertEquals;
import static org.codetome.hexameter.api.AxialCoordinate.fromCoordinates;
import static org.codetome.hexameter.api.AxialCoordinate.fromKey;

import org.junit.Before;
import org.junit.Test;

public class AxialCoordinateTest {

	private static final int TEST_GRID_X = 4;
	private static final int TEST_GRID_Z = 5;
	private AxialCoordinate target;

	@Before
	public void setUp() {
		target = fromCoordinates(TEST_GRID_X, TEST_GRID_Z);
	}

	@Test
	public void shouldReturnProperCoordinateWhenGetGridXIsCalled() {
		assertEquals(TEST_GRID_X, target.getGridX());
	}

	@Test
	public void shouldReturnProperCoordinateWhenGetGridZIsCalled() {
		assertEquals(TEST_GRID_Z, target.getGridZ());
	}

	@Test
	public void shouldReturnProperKeyWhenToKeyIsCalled() {
	    assertEquals(TEST_GRID_X + "," + TEST_GRID_Z, target.toKey());
	}

	@Test
	public void shouldCreateProperAxialCoordinateWhenFromKeyIsCalled() {
	    final AxialCoordinate result = fromKey(TEST_GRID_X + "," + TEST_GRID_Z);
	    assertEquals(target.getGridX(), result.getGridX());
	    assertEquals(target.getGridZ(), result.getGridZ());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToCreateCoordinateFromMalformedKey() {
	    AxialCoordinate.fromKey(null);
	}

}

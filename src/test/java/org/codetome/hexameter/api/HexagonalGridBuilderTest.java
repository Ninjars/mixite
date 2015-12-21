package org.codetome.hexameter.api;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.codetome.hexameter.api.AxialCoordinate.fromCoordinates;
import static org.codetome.hexameter.api.HexagonalGridLayout.TRIANGULAR;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.codetome.hexameter.api.exception.HexagonalGridCreationException;
import org.codetome.hexameter.internal.impl.layoutstrategy.GridLayoutStrategy;
import org.junit.Before;
import org.junit.Test;

public class HexagonalGridBuilderTest {

	private static final int GRID_HEIGHT = 9;
	private static final HexagonalGridLayout GRID_LAYOUT = HexagonalGridLayout.RECTANGULAR;
	private static final GridLayoutStrategy GRID_LAYOUT_STRATEGY = HexagonalGridLayout.RECTANGULAR.getGridLayoutStrategy();
	private static final int GRID_WIDTH = 9;
	private static final HexagonOrientation ORIENTATION = HexagonOrientation.FLAT_TOP;
	private static final double RADIUS = 30;

	private HexagonalGridBuilder target;

	@Before
	public void setUp() {
		target = new HexagonalGridBuilder();
		target.setGridHeight(GRID_HEIGHT).setGridLayout(GRID_LAYOUT).setGridWidth(GRID_WIDTH).setOrientation(ORIENTATION).setRadius(RADIUS);
	}

	@Test
	public void shouldContainProperValuesWhenGettersAreCalled() {
		assertEquals(GRID_HEIGHT, target.getGridHeight());
		assertEquals(GRID_WIDTH, target.getGridWidth());
		assertEquals(GRID_LAYOUT_STRATEGY, target.getGridLayoutStrategy());
		assertEquals(RADIUS, target.getRadius());
		Assert.assertNotNull(target.getSharedHexagonData());
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailGettingSharedHexagonDataWhenOrientationIsNull() {
		target.setOrientation(null);
		target.getSharedHexagonData();
	}

	@Test(expected = IllegalStateException.class)
	public void shouldFailGettingSharedHexagonDataWhenRadiusIsZero() {
		target.setRadius(0);
		target.getSharedHexagonData();
	}

	@Test(expected = HexagonalGridCreationException.class)
	public void shouldFailBuildWhenOrientationIsNull() {
		target.setOrientation(null);
		target.build();
	}

	@Test(expected = HexagonalGridCreationException.class)
	public void shouldFailBuildWhenRadiusIsZero() {
		target.setRadius(0);
		target.build();
	}

	@Test(expected = HexagonalGridCreationException.class)
	public void shouldFailBuildWhenGridLayoutIsNull() {
		target.setGridLayout(null);
		target.build();
	}

	@Test(expected = HexagonalGridCreationException.class)
	public void shouldFailBuildWhenSizeIsNotCompatibleWithLayout() {
		target.setGridLayout(TRIANGULAR);
		target.setGridHeight(4);
		target.build();
	}

	@Test
	public void shouldContainCustomCoordinateWhenCustomCoordinateIsAdded() {
		final int gridX = 1;
		final int gridZ = 2;
		final int size = target.getCustomCoordinates().size();
		target.addCustomAxialCoordinate(fromCoordinates(gridX, gridZ));
		assertTrue(target.getCustomCoordinates().size() == size + 1);
	}

	@Test
	public void shouldProperlySetCustomStorageWhenCustomStorageIsSet() {
		final Map<String, Hexagon> customStorage = new HashMap<> ();
		target.setCustomStorage(customStorage);
		assertEquals(customStorage, target.getCustomStorage());
	}

	@Test
	public void shouldBuildCalculatorWhenBuildCalculatorIsCalled() {
		final HexagonalGridCalculator calc = target.buildCalculatorFor(null);
		assertNotNull(calc);
	}

	@Test
	public void shouldReturnProperOrientationWhenGetOrientationIsCalled() {
		assertEquals(ORIENTATION, target.getOrientation());
	}

	@Test
	public void shouldBuildWhenProperParametersArePresent() {
		final HexagonalGrid grid = target.build();
		assertNotNull(grid);
	}

}

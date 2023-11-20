package org.hexworks.mixite.core.api.defaults

import org.hexworks.mixite.core.api.Hexagon
import org.hexworks.mixite.core.api.HexagonPathingCalculator
import org.hexworks.mixite.core.api.HexagonalGrid
import org.hexworks.mixite.core.api.contract.PathingAgent
import org.hexworks.mixite.core.api.contract.SatelliteData

class DefaultHexagonPathingCalculator<T: SatelliteData> : HexagonPathingCalculator<T> {

    override fun pathToAllInRange(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        searchDistance: Int,
        condition: (Hexagon<T>) -> Boolean
    ): List<List<T>> {
        TODO("Not yet implemented")
    }

    override fun pathToHex(grid: HexagonalGrid<T>, agent: PathingAgent<T>): List<Hexagon<T>> {
        TODO("Not yet implemented")
    }
}

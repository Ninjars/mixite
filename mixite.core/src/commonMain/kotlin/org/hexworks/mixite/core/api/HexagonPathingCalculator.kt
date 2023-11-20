package org.hexworks.mixite.core.api

import org.hexworks.mixite.core.api.contract.PathingAgent
import org.hexworks.mixite.core.api.contract.SatelliteData

interface HexagonPathingCalculator<T : SatelliteData> {
    fun pathToAllInRange(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        searchDistance: Int,
        condition: (Hexagon<T>) -> Boolean
    ): List<List<T>>

    fun pathToHex(grid: HexagonalGrid<T>, agent: PathingAgent<T>): List<Hexagon<T>>
}

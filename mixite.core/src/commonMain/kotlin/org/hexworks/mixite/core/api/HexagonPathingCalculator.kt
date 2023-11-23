package org.hexworks.mixite.core.api

import org.hexworks.mixite.core.api.contract.PathingAgent
import org.hexworks.mixite.core.api.contract.SatelliteData

interface HexagonPathingCalculator<T : SatelliteData> {
    fun pathsToAll(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        goals: Iterable<Hexagon<T>>,
        ): List<List<Hexagon<T>>>

    fun pathsToAllInRange(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        maxMovementCost: Double,
        goals: Iterable<Hexagon<T>>,
    ): List<List<Hexagon<T>>>

    fun pathToHex(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        goal: Hexagon<T>,
    ): List<Hexagon<T>>
}

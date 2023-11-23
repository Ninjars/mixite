package org.hexworks.mixite.core.api.defaults

import org.hexworks.mixite.core.api.Hexagon
import org.hexworks.mixite.core.api.HexagonPathingCalculator
import org.hexworks.mixite.core.api.HexagonalGrid
import org.hexworks.mixite.core.api.contract.PathingAgent
import org.hexworks.mixite.core.api.contract.SatelliteData
import org.hexworks.mixite.core.internal.impl.PriorityQueue
import kotlin.math.abs

private typealias HexPriority<T> = Pair<Hexagon<T>, Double>

class DefaultHexagonPathingCalculator<T : SatelliteData> : HexagonPathingCalculator<T> {
    private val hexPriorityComparator = object : Comparator<HexPriority<T>> {
        override fun compare(a: HexPriority<T>, b: HexPriority<T>): Int =
            a.second.compareTo(b.second)
    }

    /*
     These references are held to avoid unnecessary object creation with sequential
     calls to the PathingCalculator. The contents are cleared after each pathing call.
     */
    private val frontier = PriorityQueue(hexPriorityComparator)
    private val cameFrom = HashMap<Hexagon<T>, Hexagon<T>?>()
    private val costForPosition = HashMap<Hexagon<T>, Double>()

    override fun pathsToAll(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        goals: Iterable<Hexagon<T>>
    ): List<List<Hexagon<T>>> {
        return pathsToAllInRange(
            grid,
            agent,
            start,
            -1.0,
            goals,
        )
    }

    /**
     * Dijkstra’s algorithm implementation providing cheapest paths to all provided goals within
     * movement cost range. Supply negative maxMovementCost to remove range restriction.
     *
     * @return list of paths to all available goals. Maintains the order of the goals list but
     * returns no object for unreachable goals.
     */
    override fun pathsToAllInRange(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        maxMovementCost: Double,
        goals: Iterable<Hexagon<T>>,
    ): List<List<Hexagon<T>>> {
        frontier.offer(HexPriority(start, 0.0))
        cameFrom[start] = null
        costForPosition[start] = 0.0

        while (!frontier.isEmpty) {
            val current = frontier.poll()?.first ?: break

            val currentCost = costForPosition[current] ?: 0.0

            grid.getNeighborsOf(current).forEach { next ->
                if (agent.isPassable(current, next)) {
                    val newCost = currentCost + agent.movementCost(current, next)

                    if ((maxMovementCost < 0 || newCost <= maxMovementCost)
                        && (!costForPosition.containsKey(next) || newCost < costForPosition[next]!!)
                    ) {
                        costForPosition[next] = newCost
                        frontier.offer(HexPriority(next, newCost))
                        cameFrom[next] = current
                    }
                }
            }
        }
        return goals.mapNotNull { goal ->
            if (cameFrom.containsKey(goal)) {
                ArrayList<Hexagon<T>>().apply {
                    extractPath(goal, cameFrom)
                }
            } else {
                null
            }
        }.also {
            resetState()
        }
    }

    /**
     * A* algorithm to find optimal path to the goal.
     *
     * @return empty list if no path to goal could be found, or sequential list of hexagons
     * from start to goal
     */
    override fun pathToHex(
        grid: HexagonalGrid<T>,
        agent: PathingAgent<T>,
        start: Hexagon<T>,
        goal: Hexagon<T>,
    ): List<Hexagon<T>> {
        frontier.offer(HexPriority(start, 0.0))
        cameFrom[start] = null
        costForPosition[start] = 0.0

        while (!frontier.isEmpty) {
            val current = frontier.poll()?.first

            if (current == goal || current == null) break

            val currentCost = costForPosition[current] ?: 0.0
            grid.getNeighborsOf(current).forEach { next ->
                if (agent.isPassable(current, next)) {
                    val newCost = currentCost + agent.movementCost(current, next)

                    if (!costForPosition.containsKey(next) || newCost < costForPosition[next]!!) {
                        costForPosition[next] = newCost
                        val priority = newCost + heuristic(goal, next)
                        frontier.offer(HexPriority(next, priority))
                        cameFrom[next] = current
                    }
                }
            }
        }

        return if (cameFrom.containsKey(goal)) {
            ArrayList<Hexagon<T>>().apply {
                extractPath(goal, cameFrom)
            }
        } else {
            emptyList()
        }.also {
            resetState()
        }
    }

    private fun resetState() {
        frontier.clear()
        cameFrom.clear()
        costForPosition.clear()
    }

    /**
     * Recursively follow a path through the supplied map and append to the arraylist in reverse
     * order to result in a correctly ordered sequence of moves from start to goal.
     */
    private fun ArrayList<Hexagon<T>>.extractPath(
        hex: Hexagon<T>?,
        map: HashMap<Hexagon<T>, Hexagon<T>?>
    ) {
        if (hex == null) return

        extractPath(map[hex], map)
        add(hex)
    }

    private fun heuristic(a: Hexagon<T>, b: Hexagon<T>): Double =
        (abs(a.gridX - b.gridX) + abs(a.gridY - b.gridY) + abs(a.gridZ - b.gridZ)).toDouble()
}

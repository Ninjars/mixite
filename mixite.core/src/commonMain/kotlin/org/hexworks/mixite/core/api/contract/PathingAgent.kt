package org.hexworks.mixite.core.api.contract

import org.hexworks.mixite.core.api.Hexagon

interface PathingAgent<T : SatelliteData> {
    fun isPassable(from: Hexagon<T>, to: Hexagon<T>): Boolean
    fun movementCost(from: Hexagon<T>, to: Hexagon<T>): Double
}

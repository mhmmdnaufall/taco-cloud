package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.TacoOrder

interface OrderRepository {

    fun save(order: TacoOrder): TacoOrder

    fun findById(id: Long): TacoOrder?

}
package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.Ingredient

interface IngredientRepository {

    fun findAll(): List<Ingredient>

    fun findById(id: String): Ingredient?

    fun save(ingredient: Ingredient): Ingredient

}
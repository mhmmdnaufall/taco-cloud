package mhmmdnaufall.tacos.web

import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.data.IngredientRepository
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IngredientByIdConverter(
        private val ingredientRepository: IngredientRepository
) : Converter<String, Ingredient> {

    override fun convert(id: String): Ingredient? = ingredientRepository.findById(id)

}
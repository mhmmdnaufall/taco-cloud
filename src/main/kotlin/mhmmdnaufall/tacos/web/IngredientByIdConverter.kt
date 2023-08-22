package mhmmdnaufall.tacos.web

import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.Ingredient.Type
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class IngredientByIdConverter : Converter<String, Ingredient> {

    private val ingredientMap = mutableMapOf<String, Ingredient>()

    init {
        ingredientMap.run {
            put("FLTO", Ingredient("FLTO", "Flour Tortilla", Type.WRAP))
            put("COTO", Ingredient("COTO", "Corn Tortilla", Type.WRAP));
            put("GRBF", Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
            put("CARN", Ingredient("CARN", "Carnitas", Type.PROTEIN));
            put("TMTO", Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
            put("LETC", Ingredient("LETC", "Lettuce", Type.VEGGIES));
            put("CHED", Ingredient("CHED", "Cheddar", Type.CHEESE));
            put("JACK", Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
            put("SLSA", Ingredient("SLSA", "Salsa", Type.SAUCE));
            put("SRCR", Ingredient("SRCR", "Sour Cream", Type.SAUCE));
        }
    }

    override fun convert(id: String): Ingredient? = ingredientMap[id]
}
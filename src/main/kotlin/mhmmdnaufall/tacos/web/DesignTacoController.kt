package mhmmdnaufall.tacos.web

import jakarta.validation.Valid
import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.Ingredient.Type
import mhmmdnaufall.tacos.Taco
import mhmmdnaufall.tacos.TacoOrder
import mhmmdnaufall.tacos.data.IngredientRepository
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes

@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
class DesignTacoController(
        private val ingredientRepository: IngredientRepository
) {

    @GetMapping
    fun showDesignForm() = "design"

    @PostMapping
    fun processTaco(
            log: Logger,
            @Valid taco: Taco,
            errors: Errors,
            @ModelAttribute tacoOrder: TacoOrder
    ): String {
        log.info(taco.toString())
        return if (errors.hasErrors()) {
            "design"
        } else {
            tacoOrder.addTaco(taco)
            log.info("Processing taco: {}", taco)

            "redirect:/orders/current"
        }
    }

    @ModelAttribute
    fun addIngredientsToModel(model: Model) {
        val ingredients: List<Ingredient> = ingredientRepository.findAll()
        val types: Array<Type> = Ingredient.Type.values()
        types.forEach { type ->
            model.addAttribute(type.toString().lowercase(), filterByType(ingredients, type))
        }
    }

    @ModelAttribute(name = "tacoOrder")
    fun order() = TacoOrder()

    @ModelAttribute(name = "taco")
    fun taco() = Taco()

    private fun filterByType(ingredients: List<Ingredient>, type: Type): Iterable<Ingredient> {
        return ingredients.filter { it.type == type }
    }

}
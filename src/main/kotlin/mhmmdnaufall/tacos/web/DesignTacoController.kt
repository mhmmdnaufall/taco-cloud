package mhmmdnaufall.tacos.web

import jakarta.validation.Valid
import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.Ingredient.Type
import mhmmdnaufall.tacos.Taco
import mhmmdnaufall.tacos.TacoOrder
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
class DesignTacoController {

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
        val ingredients =  listOf(
                Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                Ingredient("CARN", "Carnitas", Type.PROTEIN),
                Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                Ingredient("LETC", "Lettuce", Type.VEGGIES),
                Ingredient("CHED", "Cheddar", Type.CHEESE),
                Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                Ingredient("SLSA", "Salsa", Type.SAUCE),
                Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        )

        val types: Array<Type> = Type.values()
        for (type in types) {
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
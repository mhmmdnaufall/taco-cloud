package mhmmdnaufall.tacos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.Date

data class Taco(

        var id: Long? = null,

        var createdAt: Date = Date(),

        @field:NotBlank
        @field:Size(min = 5, message = "Name must be at least 5 characters long")
        var name: String? = null,

        @field:Size(min = 1, message = "You must choose at least 1 ingredient")
        var ingredients: MutableList<IngredientRef> = mutableListOf()

)
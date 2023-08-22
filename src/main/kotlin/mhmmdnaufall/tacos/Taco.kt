package mhmmdnaufall.tacos

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class Taco(

        @field:NotBlank
        @field:Size(min = 5, message = "Name must be at least 5 characters long")
        var name: String? = null,

        @field:NotNull
        @field:Size(min = 1, message = "You must choose at least 1 ingredient")
        var ingredients: List<Ingredient>? = null

)
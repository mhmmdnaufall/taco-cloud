package mhmmdnaufall.tacos.web

import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.Ingredient.Type
import mhmmdnaufall.tacos.data.IngredientRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito


class IngredientByIdConverterTest {

    private lateinit var converter: IngredientByIdConverter

    @BeforeEach
    fun setUp() {
        val ingredientRepository = Mockito.mock(IngredientRepository::class.java)

        Mockito.`when`(ingredientRepository.findById("AAAA"))
                .thenReturn(Ingredient("AAAA", "TEST INGREDIENT", Type.CHEESE))

        Mockito.`when`(ingredientRepository.findById("ZZZZ"))
                .thenReturn(null)

        converter = IngredientByIdConverter(ingredientRepository)
    }

    @Test
    fun returnValueWhenPresent() {
        assertEquals(Ingredient("AAAA", "TEST INGREDIENT", Type.CHEESE), converter.convert("AAAA"))
    }

    @Test
    fun returnNullWhenMissing() {
        assertNull(converter.convert("ZZZZ"))
    }
}
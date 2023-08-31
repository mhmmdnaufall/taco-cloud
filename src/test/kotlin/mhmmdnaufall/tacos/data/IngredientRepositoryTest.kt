package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.Ingredient
import mhmmdnaufall.tacos.Ingredient.Type
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class IngredientRepositoryTest {

    @Autowired
    private lateinit var ingredientRepository: IngredientRepository

    @Test
    fun findById() {
        val flto = ingredientRepository.findById("FLTO")
        assertNotNull(flto)
        assertEquals(Ingredient("FLTO", "Flour Tortilla", Type.WRAP), flto)

        val xxxx = ingredientRepository.findById("XXXX")
        assertNull(xxxx)
    }
}
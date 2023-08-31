package mhmmdnaufall.tacos

import mhmmdnaufall.tacos.data.IngredientRepository
import mhmmdnaufall.tacos.data.OrderRepository
import mhmmdnaufall.tacos.web.DesignTacoController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

@WebMvcTest(DesignTacoController::class)
class DesignTacoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private lateinit var ingredients: List<Ingredient>

    private lateinit var design: Taco

    @MockBean
    private lateinit var ingredientRepository: IngredientRepository

    @BeforeEach
    fun setUp() {
        ingredients = listOf(
                Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
                Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
                Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
                Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
                Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
                Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
                Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
                Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        )
        Mockito.`when`(ingredientRepository.findAll())
                .thenReturn(ingredients)

        Mockito.`when`(ingredientRepository.findById("FLTO"))
                .thenReturn(Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP))

        Mockito.`when`(ingredientRepository.findById("GRBF"))
                .thenReturn(Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN))

        Mockito.`when`(ingredientRepository.findById("CHED"))
                .thenReturn(Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE))

        design = Taco()
        design.name = "Test Taco"

        design.ingredients = mutableListOf(
                IngredientRef("FLTO"), IngredientRef("GRBF"), IngredientRef("CHED")
        )
    }

    @Test
    fun testShowDesignForm() {
        mockMvc
                .perform(
                        get("/design")
                )
                .andExpect(
                        status().isOk
                )
                .andExpect(
                        view().name("design")
                )
                .andExpect(
                        model().attribute("wrap", ingredients.subList(0, 2))
                )
                .andExpect(model().attribute("protein", ingredients.subList(2, 4)))
                .andExpect(model().attribute("veggies", ingredients.subList(4, 6)))
                .andExpect(model().attribute("cheese", ingredients.subList(6, 8)))
                .andExpect(model().attribute("sauce", ingredients.subList(8, 10)))
    }

    @Test
    fun processTaco() {
        mockMvc
                .perform(
                        post("/design")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
                )
                .andExpect(
                        status().is3xxRedirection
                )
                .andExpect(
                        header().stringValues("Location", "/orders/current")
                )
    }
}
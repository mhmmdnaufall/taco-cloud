package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.IngredientRef
import mhmmdnaufall.tacos.Taco
import mhmmdnaufall.tacos.TacoOrder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.MethodMode

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    fun saveOrderWithTwoTacos() {

        val taco1 = Taco(
                name = "Taco One",
                ingredients = mutableListOf(
                        IngredientRef("FLTO"),
                        IngredientRef("GRBF"),
                        IngredientRef("CHED")
                )
        )

        val taco2 = Taco(
                name = "Taco Two",
                ingredients = mutableListOf(
                        IngredientRef("COTO"),
                        IngredientRef("CARN"),
                        IngredientRef("JACK")
                )
        )

        val order = TacoOrder(
                deliveryName = "Test Taco Order",
                deliveryCity = "Jakarta",
                deliveryState = "ID",
                deliveryStreet = "Jalan Ibu Kita Kartini",
                deliveryZip = "11111",
                ccNumber = "4111111111111111",
                ccExpiration = "08/25",
                ccCVV = "111"
        )

        order.run {
            addTaco(taco1)
            addTaco(taco2)
        }

        val savedOrder = orderRepository.save(order)
        assertNotNull(savedOrder)

        val fetchedOrder = orderRepository.findById(savedOrder.id!!)
        assertNotNull(fetchedOrder)

        assertEquals("Test Taco Order", fetchedOrder?.deliveryName)
        assertEquals("Jakarta", fetchedOrder?.deliveryCity)
        assertEquals("ID", fetchedOrder?.deliveryState)
        assertEquals("Jalan Ibu Kita Kartini", fetchedOrder?.deliveryStreet)
        assertEquals("11111", fetchedOrder?.deliveryZip)
        assertEquals("4111111111111111", fetchedOrder?.ccNumber)
        assertEquals("08/25", fetchedOrder?.ccExpiration)
        assertEquals("111", fetchedOrder?.ccCVV)

        assertEquals(savedOrder.placedAt, fetchedOrder?.placedAt)

        val tacos: List<Taco>? = fetchedOrder?.tacos
        assertNotNull(tacos)
        assertEquals(2, tacos?.size)
        assertTrue(tacos?.containsAll(listOf(taco1, taco2))!!)

    }
}
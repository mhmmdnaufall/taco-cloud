package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.IngredientRef
import mhmmdnaufall.tacos.Taco
import mhmmdnaufall.tacos.TacoOrder
import org.springframework.asm.Type
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.Types
import java.util.*

@Repository
class JdbcOrderRepository(
        private val jdbcOperations: JdbcOperations
) : OrderRepository {

    @Transactional
    override fun save(order: TacoOrder): TacoOrder {
        val pscf = PreparedStatementCreatorFactory(
                """
                    INSERT INTO Taco_Order 
                    (delivery_name, delivery_street, delivery_city, delivery_state,
                     delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        )
        pscf.setReturnGeneratedKeys(true)

        order.placedAt = Date()

        val psc = pscf.newPreparedStatementCreator(
                listOf(
                        order.deliveryName,
                        order.deliveryStreet,
                        order.deliveryCity,
                        order.deliveryState,
                        order.deliveryZip,
                        order.ccNumber,
                        order.ccExpiration,
                        order.ccCVV,
                        order.placedAt
                )
        )

        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(psc, keyHolder)
        val orderId: Long? = keyHolder.key?.toLong()
        order.id = orderId

        val tacos: List<Taco> = order.tacos

        tacos.forEachIndexed { i, taco ->
            saveTaco(orderId, i, taco)
        }

        return order
    }

    override fun findById(id: Long): TacoOrder? {
        return try {
            val order = jdbcOperations.queryForObject(
                    """
                    SELECT id, delivery_name, delivery_street, delivery_city,
                    delivery_state, delivery_zip, cc_number, cc_expiration,
                    cc_cvv, placed_at FROM Taco_Order WHERE id=?
                """.trimIndent(), { row, _ ->
                TacoOrder(
                        id = row.getLong("id"),
                        deliveryName = row.getString("delivery_name"),
                        deliveryStreet = row.getString("delivery_street"),
                        deliveryCity = row.getString("delivery_city"),
                        deliveryState = row.getString("delivery_state"),
                        deliveryZip = row.getString("delivery_zip"),
                        ccNumber = row.getString("cc_number"),
                        ccExpiration = row.getString("cc_expiration"),
                        ccCVV = row.getString("cc_cvv"),
                        placedAt = Date(row.getTimestamp("placed_at").time),
                        tacos = findTacosByOrderId(row.getLong("id")).toMutableList()
                )
            },
                    id
            )
            order
        } catch (e: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    private fun findTacosByOrderId(orderId: Long): List<Taco> = jdbcOperations.query(
            """
                SELECT id, name, created_at FROM Taco
                WHERE taco_order=? order by taco_order_key
            """.trimIndent(), { row, _ ->
                Taco(
                        id = row.getLong("id"),
                        name = row.getString("name"),
                        createdAt = Date(row.getTimestamp("created_at").time),
                        ingredients = findIngredientsByTacoId(row.getLong("id")).toMutableList()
                )
            },
            orderId
    )

    private fun saveTaco(orderId: Long?, orderKey: Int, taco: Taco): Long {
        taco.createdAt = Date()
        val pscf = PreparedStatementCreatorFactory(
                """
                    INSERT INTO Taco (name, created_at, taco_order, taco_order_key)
                    VALUES (?, ?, ?, ?)
                """.trimIndent(),
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        )
        pscf.setReturnGeneratedKeys(true)

        val psc = pscf.newPreparedStatementCreator(
                listOf(
                        taco.name,
                        taco.createdAt,
                        orderId,
                        orderKey
                )
        )

        val keyHolder = GeneratedKeyHolder()
        jdbcOperations.update(psc, keyHolder)
        val tacoId = keyHolder.key?.toLong()!!
        taco.id = tacoId

        saveIngredientRefs(tacoId, taco.ingredients)

        return tacoId

    }

    private fun findIngredientsByTacoId(tacoId: Long): List<IngredientRef> = jdbcOperations.query(
            """
                SELECT ingredient FROM Ingredient_Ref
                WHERE taco = ? ORDER BY taco_key
            """.trimIndent(),
            { row, _ -> IngredientRef(row.getString("ingredient")) },
            tacoId
    )

    private fun saveIngredientRefs(tacoId: Long, ingredientRefs: List<IngredientRef>) {
        ingredientRefs.forEachIndexed { key, ingredientRef ->
            jdbcOperations.update(
                    "INSERT INTO Ingredient_Ref (ingredient, taco, taco_key) VALUES (?, ?, ?)",
                    ingredientRef.ingredient, tacoId, key
            )
        }
    }

}
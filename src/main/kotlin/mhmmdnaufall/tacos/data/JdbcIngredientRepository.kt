package mhmmdnaufall.tacos.data

import mhmmdnaufall.tacos.Ingredient
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class JdbcIngredientRepository(
        private val jdbcTemplate: JdbcTemplate
) : IngredientRepository {

    override fun findAll(): List<Ingredient> {
        return jdbcTemplate.query(
                "SELECT id, name, type, FROM Ingredient"
        ) { row, _ -> mapRowToIngredient(row) }
    }

    override fun findById(id: String): Ingredient? {
        val result: List<Ingredient> = jdbcTemplate.query(
                "SELECT id, name, type FROM Ingredient WHERE id=?",
                { row, _ -> mapRowToIngredient(row) },
                id
        )
        return if (result.isNotEmpty()) result[0] else null
    }

    override fun save(ingredient: Ingredient): Ingredient {
        jdbcTemplate.update(
                "INSERT INTO Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.id, ingredient.name, ingredient.type.toString()
        )
        return ingredient
    }

    private fun mapRowToIngredient(row: ResultSet): Ingredient {
        return Ingredient(
                row.getString("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type"))
        )
    }
}
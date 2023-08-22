package mhmmdnaufall.tacos

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesignAndOrderTacosBrowserTest {

    @LocalServerPort
    private var port: Int? = null

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Test
    fun `testDesignATacoPage HappyPath`() {
        browser.get(homePageUrl())
        clickDesignATaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        clickBuildAnotherTaco()
        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    @Test
    fun `testDesignATacoPage EmptyOrderInfo`() {
        browser.get(homePageUrl())
        clickDesignATaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitEmptyOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    @Test
    fun `testDesignATacoPage InvalidOrderInfo`() {
        browser.get(homePageUrl())
        clickDesignATaco()
        assertDesignPageElements()
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA")
        submitInvalidOrderForm()
        fillInAndSubmitOrderForm()
        assertEquals(homePageUrl(), browser.currentUrl)
    }

    private fun buildAndSubmitATaco(name: String, vararg ingredients: String) {
        assertDesignPageElements()

        for (ingredient in ingredients) {
            browser.findElement(By.cssSelector("input[value='$ingredient']")).click()
        }
        browser.findElement(By.cssSelector("input#name")).sendKeys(name)
        browser.findElement(By.cssSelector("form")).submit()
    }

    private fun assertDesignPageElements() {
        assertEquals(designPageUrl(), browser.currentUrl)
        val ingredientGroups: List<WebElement> = browser.findElements(By.className("ingredient-group"))
        assertEquals(5, ingredientGroups.size)

        val wrapGroup: WebElement = browser.findElement(By.cssSelector("div.ingredient-group#wraps"))
        val wraps: List<WebElement> = wrapGroup.findElements(By.tagName("div"))
        assertEquals(2, wraps.size)
        assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla")
        assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla")

        val proteinGroup: WebElement = browser.findElement(By.cssSelector("div.ingredient-group#proteins"))
        val proteins: List<WebElement> = proteinGroup.findElements(By.tagName("div"))
        assertEquals(2, proteins.size)
        assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef")
        assertIngredient(proteinGroup, 1, "CARN", "Carnitas")

        val cheeseGroup: WebElement = browser.findElement(By.cssSelector("div.ingredient-group#cheeses"))
        val cheeses: List<WebElement> = proteinGroup.findElements(By.tagName("div"))
        assertEquals(2, proteins.size)
        assertIngredient(cheeseGroup, 0, "CHED", "Cheddar")
        assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack")

        val veggieGroup: WebElement = browser.findElement(By.cssSelector("div.ingredient-group#veggies"))
        val veggies: List<WebElement> = proteinGroup.findElements(By.tagName("div"))
        assertEquals(2, proteins.size)
        assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes")
        assertIngredient(veggieGroup, 1, "LETC", "Lettuce")

        val sauceGroup: WebElement = browser.findElement(By.cssSelector("div.ingredient-group#sauces"))
        val sauces: List<WebElement> = proteinGroup.findElements(By.tagName("div"))
        assertEquals(2, proteins.size)
        assertIngredient(sauceGroup, 0, "SLSA", "Salsa")
        assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream")
    }

    private fun fillInAndSubmitOrderForm() {
        assertTrue(browser.currentUrl
                .startsWith(orderDetailsPageUrl()))
        fillField("input#deliveryName", "Ima Hungry")
        fillField("input#deliveryStreet", "1234 Culinary Blvd.")
        fillField("input#deliveryCity", "Foodsville")
        fillField("input#deliveryState", "CO")
        fillField("input#deliveryZip", "81019")
        fillField("input#ccNumber", "4111111111111111")
        fillField("input#ccExpiration", "10/23")
        fillField("input#ccCVV", "123")
        browser.findElement(By.cssSelector("form")).submit()
    }

    private fun submitEmptyOrderForm() {
        assertEquals(currentOrderDetailsPageUrl(), browser.currentUrl)
        browser.findElement(By.cssSelector("form")).submit()

        assertEquals(orderDetailsPageUrl(), browser.currentUrl)

        val validationErrors: List<String> = getValidationErrorTexts()
        assertEquals(9, validationErrors.size)
        assertTrue(validationErrors
                .containsAll(listOf(
                        "Please correct the problems below and resubmit.",
                        "Delivery name is required",
                        "Street is required",
                        "City is required",
                        "State is required",
                        "Zip code is required",
                        "Not a valid credit card number",
                        "Must be formatted MM/YY",
                        "Invalid CVV"
                ))
        )
    }

    private fun getValidationErrorTexts(): List<String> {
        val validationErrorElements: List<WebElement> = browser.findElements(By.className("validationError"))
        return validationErrorElements.map { it.text }
    }

    private fun submitInvalidOrderForm() {
        assertTrue(browser.currentUrl
                .startsWith(orderDetailsPageUrl()))
        fillField("input#deliveryName", "I")
        fillField("input#deliveryStreet", "1")
        fillField("input#deliveryCity", "F")
        fillField("input#deliveryState", "C")
        fillField("input#deliveryZip", "8")
        fillField("input#ccNumber", "1234432112344322")
        fillField("input#ccExpiration", "14/91")
        fillField("input#ccCVV", "1234")
        browser.findElement(By.cssSelector("form")).submit()
    }

    private fun fillField(fieldName: String, value: String) {
        val field: WebElement = browser.findElement(By.cssSelector(fieldName))
        field.clear()
        field.sendKeys(value)
    }

    private fun assertIngredient(ingredientGroup: WebElement, ingredientIdx: Int, id: String, name: String) {
        val proteins: List<WebElement> = ingredientGroup.findElements(By.tagName("div"))
        val ingredient: WebElement = proteins[ingredientIdx]

        assertEquals(id, ingredient.findElement(By.tagName("input")).getAttribute("value"))
        assertEquals(name, ingredient.findElement(By.tagName("span")).text)
    }

    private fun clickDesignATaco() {
        assertEquals(homePageUrl(), browser.currentUrl)
        browser.findElement(By.cssSelector("a[id='design']")).click()
    }

    private fun clickBuildAnotherTaco() {
        assertTrue(browser.currentUrl
                .startsWith(orderDetailsPageUrl()))
        browser.findElement(By.cssSelector("a[id='another']")).click()
    }

    private fun homePageUrl() = "http://localhost:$port/"

    private fun designPageUrl() = "${homePageUrl()}design"

    private fun orderDetailsPageUrl() = "${homePageUrl()}orders"

    private fun currentOrderDetailsPageUrl() = "${homePageUrl()}orders/current"

    companion object {
        private lateinit var browser: HtmlUnitDriver

        @BeforeAll
        @JvmStatic
        fun setup() {
            browser = HtmlUnitDriver()
            browser.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(10))
        }

        @AfterAll
        @JvmStatic
        fun teardown(){
            browser.quit()
        }
    }


}
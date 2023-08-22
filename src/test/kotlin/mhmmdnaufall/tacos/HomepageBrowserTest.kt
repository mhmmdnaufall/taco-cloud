package mhmmdnaufall.tacos

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.server.LocalServerPort
import  org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import java.time.Duration


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HomepageBrowserTest {

        @LocalServerPort
        private var port: Int? = null

        @Autowired
        private lateinit var rest: TestRestTemplate

        @Test
        fun testHomePage() {
                val homepage = "http://localhost:$port"
                browser.get(homepage)

                val titleText = browser.title
                assertEquals("Taco Cloud", titleText)

                val h1Text = browser.findElement(By.tagName("h1")).text
                assertEquals("Welcome to...", h1Text)

                val imgSrc = browser.findElement(By.tagName("img")).getAttribute("src")
                assertEquals("${homepage}/images/TacoCloud.png", imgSrc)
        }

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
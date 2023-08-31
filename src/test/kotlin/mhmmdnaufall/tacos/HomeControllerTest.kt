package mhmmdnaufall.tacos

import mhmmdnaufall.tacos.web.WebConfiguration
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.MockMvcBuilder.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testHomePage() {
        mockMvc
                .perform(
                        get("/")
                )
                .andExpect(
                        status().isOk
                )
                .andExpect(
                        view().name("home")
                )
                .andExpect(
                        content().string(
                                Matchers.containsString("Welcome to...")
                        )
                )
    }
}
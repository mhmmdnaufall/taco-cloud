package mhmmdnaufall.tacos

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

import org.springframework.test.web.servlet.MockMvcBuilder.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*

@WebMvcTest
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
                        content().string(
                                Matchers.containsString("Welcome to...")
                        )
                )
    }
}
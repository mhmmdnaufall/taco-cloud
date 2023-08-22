package mhmmdnaufall.tacos.web

import jakarta.validation.Valid
import mhmmdnaufall.tacos.TacoOrder
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
class OrderController {

    @GetMapping("/current")
    fun orderForm() = "orderForm"

    @PostMapping
    fun processOrder(
            log: Logger,
            @Valid order: TacoOrder,
            errors: Errors,
            sessionStatus: SessionStatus
    ): String {
        log.info(order.toString())
        return if (errors.hasErrors()) {
            "orderForm"
        } else {
            log.info("Order submitted: {}", order)
            sessionStatus.setComplete()
            "redirect:/"
        }
    }

}
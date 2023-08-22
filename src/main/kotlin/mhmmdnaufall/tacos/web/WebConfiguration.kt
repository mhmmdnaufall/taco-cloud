package mhmmdnaufall.tacos.web

import mhmmdnaufall.tacos.resolver.LogArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(val logArgumentResolver: LogArgumentResolver) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(resolvers)
        resolvers.add(logArgumentResolver)
    }

    override fun addViewControllers(registry: ViewControllerRegistry) {
        super.addViewControllers(registry)
        registry.addViewController("/").setViewName("home")
    }

}
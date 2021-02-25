import me.ruslanys.telegraff.core.dsl.handler
import me.ruslanys.telegraff.core.dto.request.MarkdownMessage

handler("/start2") {

    process { _, _ ->
        MarkdownMessage("Привет!")
    }
    
}
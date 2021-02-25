import me.ruslanys.telegraff.core.dsl.handler
import me.ruslanys.telegraff.core.dto.request.MarkdownMessage
import me.ruslanys.telegraff.core.exception.ValidationException

enum class PaymentMethod(val content: String) {
    CARD("Картой"), CASH("Наличкой")
}

handler("/start", "такси") {
    step<String>("locationFrom") {
        question {
            MarkdownMessage("Какая модель авто? 🚕")
        }
    }

    step<String>("locationTo") {
        question {
            MarkdownMessage("Какой год авто?", "👴🏿 2010", "2011", "2012", "2020 🦄")
        }
    }

    step<PaymentMethod>("paymentMethod") {
        val card = "💳 Картой - я мажор"
        val cash = "💵 Наличкой - бо и так в конверте получаю"

        question {
            MarkdownMessage("Оплата картой или наличкой?", card, cash)
        }

        validation {
            when (it.toLowerCase()) {
                card -> PaymentMethod.CARD
                cash -> PaymentMethod.CASH
                else -> throw ValidationException("Пожалуйста, выбери один из вариантов")
            }
        }
    }

    step<String>("deliveryTime") {
        question {
            MarkdownMessage(
                "Как срочно Вам нужно авто?",
                "30 дней",
                "60 дней",
                "70 дней, я не спешу бля",
                "НА ЗАВТРА БРО ИБШ, батя платит!"
            )
        }
    }

    process { state, answers: Map<String, Any> ->
        val from = answers["locationFrom"] as String
        val to = answers["locationTo"] as String
        val paymentMethod = answers["paymentMethod"] as PaymentMethod

        // Business logic

        MarkdownMessage(
            """
            Заказ принят от пользователя #${state.chat.id}. 
            Авто которое мы привезем: $from 
            и оно будет $to года. 
            
            Оплата будет: ${paymentMethod.content}.
        """.trimIndent()
        )
    }
}
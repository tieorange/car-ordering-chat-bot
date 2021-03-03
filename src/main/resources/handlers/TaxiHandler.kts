import me.ruslanys.telegraff.core.dsl.handler
import me.ruslanys.telegraff.core.dto.request.MarkdownMessage
import me.ruslanys.telegraff.core.dto.request.PhotoMessage
import me.ruslanys.telegraff.core.exception.ValidationException

enum class FuelType(val content: String) {
    FUEL("Benzin"), DYSEL("Dizel")
}


handler("/start", "такси") {

    step<String>("productionYear") {
        /* TODO: add this  question before the below one
* ✅ Этот бот поможет вам быстро рассчитать стоимость растаможки "евробляхи" по новому закону о льготной растаможке №3704-Д и 4643-Д ✅
* */

        question {
            MarkdownMessage("\uD83D\uDE97 Введите год выпуска автомобиля:", "2007", "2008", "2010")
        }
    }

    step<String>("enginePower") {
        question {
            MarkdownMessage(
                "\uD83D\uDE97 Выберите объем двигателя?",
                "👴🏿 До 2000см3",
                "2001см3-3000см3",
                "3001см3-4000см3"
            )
        }
    }

    step<String>("enginePower2") {
        question {
            MarkdownMessage("\uD83D\uDE97 Введите объем двигателя в см³:")
        }
    }


    step<FuelType>("fuelType") {
        val fuel = "💳 \uD83D\uDE97 Бензин"
        val dysel = "💵 \uD83D\uDE97 Дизель"

        question {
            MarkdownMessage("\uD83D\uDE97 Выберите тип топлива:", fuel, dysel)
        }

        validation {
            when (it) {
                fuel -> FuelType.FUEL
                dysel -> FuelType.DYSEL
                else -> throw ValidationException("Пожалуйста, выбери один из вариантов")
            }
        }
    }

    step<String>("dsfdsfsdaf") {
        question {
            MarkdownMessage("✅ Отлично! Теперь просчитываем стоимость растаможки...", "OK")
        }
    }

    step<String>("deliveryTime2") {
        question {
            MarkdownMessage(
                "Сумма таможенных платежей для вашего автомобиля 1175 € \n" +
                        "\n" +
                        "❗️ Дополнительные платежи:\n" +
                        "\uD83D\uDD18 Добровольный взнос 8500 грн", "OK"
            )
        }
    }

    process { state, answers: Map<String, Any> ->

        // TODO: 1 STAWKA BAZOWA: CURRENT_Year - Production_year
        val currentYear = 2021
        val productionYear = answers["productionYear"] as String
        val yearResult = currentYear - productionYear.toInt()

        // TODO: 2 EngineProwerStawka: do 2k - 0.25EUR; Ilya the orlinyj glaz
        //  2001-3000 - 0.2;
        //  3001-4000 - 0.25;
        //  4001-5000 - 0.35;
        //  5001-more - 0.5;
        //        val enginePower = answers["enginePower"] as String
        val enginePower2 = answers["enginePower2"] as String
        val enginePowerResult = enginePower2.toInt()
        val enginePowerResultEnum =
            when (enginePowerResult) {
                in 2001..3000 -> {
                    0.2
                }
                in 3001..4000 -> {
                    0.25
                }
                in 4001..5000 -> {
                    0.35
                }
                else -> {
                    0.5
                }
            }


        // TODO: 3; Benzin - 0;
        //  Hybrid - 0
        //  Dyzel - 100
        val fuelType = answers["fuelType"] as FuelType
        val fuelTypeResult: Int = if (fuelType == FuelType.FUEL) {
            0
        } else {
            100
        }


        //        val deliveryTime = answers["deliveryTime"] as String

        // Business logic

        val FINAL_RESULT = yearResult + enginePowerResultEnum * fuelTypeResult
        val bytes = readClasspathResource("images/sample.png")
        //        PhotoMessage(bytes)

        PhotoMessage(
            bytes,
            ("⚠️ Сумма таможенных платежей для вашего автомобиля $FINAL_RESULT € \n" +
                    "\n" +
                    "❗️ Дополнительные платежи:\n" +
                    "\uD83D\uDD18 Добровольный взнос 8500 грн (250EUR)" +
                    "").trimIndent()
        )
    }
}
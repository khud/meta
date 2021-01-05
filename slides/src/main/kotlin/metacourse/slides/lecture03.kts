import io.kotex.beamer.frame
import io.kotex.core.document
import io.kotex.core.makeTitle
import metacourse.slides.createPreamble

val doc = document(createPreamble("Язык TSG")) {
    makeTitle()

    section("Языки типа Lisp") {
        frame("История вопроса") {

        }

        frame("Деревья абстрактного синтаксиса") {

        }

        frame("Атомы и S-выражения") {

        }

        frame("Рекурсия") {

        }

        frame("Плоские языки") {

        }

        frame("Связь с \\lambda-исчислением") {

        }
    }

    section("Синтаксис языка") {
        frame("a-значения и e-значения") {

        }

        frame("Грамматика") {

        }

        frame("Ветвления") {

        }

        frame("Простейшая программа") {

        }

        frame("Поиск подстроки в строке") {

        }
    }

    section("Семантика языка") {
        frame("Среда, связи и состояние") {

        }

        frame("Интерпретатор языка") {

        }
    }
}
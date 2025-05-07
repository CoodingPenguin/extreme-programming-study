import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.example.main
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class testMain : StringSpec({
    "prints Hello, world!" {
        val output = ByteArrayOutputStream()
        val originalOut = System.out
        System.setOut(PrintStream(output))

        try {
            main()
        } finally {
            System.setOut(originalOut)
        }

        output.toString().trim() shouldBe "Hello, world!"
    }
})
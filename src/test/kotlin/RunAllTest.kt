import com.intuit.karate.Runner
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Test
import java.io.File

class RunAllTest {
    companion object {
        private const val DEFAULT_NAME = "PROJECT_NAME"
        private const val DEFAULT_THREADS = 1
        private const val DEFAULT_OUTPUT_DIR = "target"
    }

    @Test
    fun runAll() {
        val nbThreads = System.getenv("REPORT_THREADS_NUMBER")?.toIntOrNull() ?: DEFAULT_THREADS
        val result = Runner.parallel(javaClass, nbThreads)

        result.reportDir?.let {
            val projectName = System.getenv("REPORT_PROJECT_NAME") ?: DEFAULT_NAME
            val reportDir =  System.getenv("REPORT_OUTPUT_DIR") ?: DEFAULT_OUTPUT_DIR

            generateReport(it, projectName, reportDir)
        }
    }

    private fun generateReport(output: String, projectName: String, reportDir: String) {
        val jsonFiles = FileUtils.listFiles(File(output), arrayOf("json"), true)
            .map { it.absolutePath }
        ReportBuilder(jsonFiles, Configuration(File(reportDir), projectName)).generateReports()
    }
}

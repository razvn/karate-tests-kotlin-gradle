package karate

import com.intuit.karate.Runner
import net.masterthought.cucumber.Configuration
import net.masterthought.cucumber.ReportBuilder
import org.apache.commons.io.FileUtils
import java.io.File
import java.lang.Integer.max
import kotlin.system.exitProcess

object RunAll {
    private const val DEFAULT_NAME = "PROJECT_NAME"
    private const val DEFAULT_FEATURES_DIR = "/tmp/karate"
    private const val DEFAULT_THREADS = 1
    private const val DEFAULT_OUTPUT_DIR = "target"

    private const val ENV_FEATURES_DIR = "KARATE_FEATURES_DIR"
    private const val ENV_PROJECT_NAME = "KARATE_PROJECT_NAME"
    private const val ENV_OUTPUT_DIR = "KARATE_OUTPUT_DIR"
    private const val ENV_THREADS_NUMBER = "KARATE_THREADS_NUMBER"
    private const val ENV_BASE_URL = "KARATE_BASE_URL"

    @JvmStatic
    fun main(args: Array<String>) {

        println("Args: ${args.joinToString(",")}")

        val argsMap = readArgs(args.toList())

        if (argsMap.containsKey(AppArg.HELP)) {
            printHelp()
            exitProcess(0)
        }

        val nbThreads = argsMap[AppArg.THREADS]?.toIntOrNull() ?: System.getenv(ENV_THREADS_NUMBER)?.toIntOrNull() ?: DEFAULT_THREADS
        val featuresDir = argsMap[AppArg.FEATURES] ?: System.getenv(ENV_FEATURES_DIR) ?: DEFAULT_FEATURES_DIR
        argsMap[AppArg.URL]?.let {
            System.setProperty(ENV_BASE_URL, it)
        }
        val dir = File(featuresDir)
        println("*** Features Dir: ${dir.absolutePath}")
        val result = Runner.path("${dir.absolutePath}/").parallel(nbThreads)

        result.reportDir?.let {
            val projectName = argsMap[AppArg.PROJECT] ?: System.getenv(ENV_PROJECT_NAME) ?: DEFAULT_NAME
            val reportDir = argsMap[AppArg.OUTPUT] ?:  System.getenv(ENV_OUTPUT_DIR) ?: DEFAULT_OUTPUT_DIR

            generateReport(it, projectName, reportDir)
        }
    }

    private fun generateReport(output: String, projectName: String, reportDir: String) {
        val jsonFiles = FileUtils.listFiles(File(output), arrayOf("json"), true)
            .map { it.absolutePath }
        ReportBuilder(jsonFiles, Configuration(File(reportDir), projectName)).generateReports()
    }

    private fun readArgs(args: List<String>): Map<AppArg, String> {
        return AppArg.values().mapNotNull { arg ->
            val idx = max(args.indexOf(arg.short), args.indexOf(arg.long))
            if (idx > -1) {
                if (arg == AppArg.HELP) arg to ""
                else {
                    val value = args.getOrNull(idx + 1)
                    value?.let {
                        arg to it
                    }
                }
            } else null
        }.toMap()
    }


    enum class AppArg(val short: String, val long: String) {
        PROJECT("-p", "--project"),
        THREADS("-t", "--threads"),
        FEATURES("-f", "--features"),
        OUTPUT("-o", "--output"),
        URL("-u", "--url"),
        HELP("-h", "--help"),
    }

    private fun printHelp() {
        println("""
            Available parameters: 
                -h (--help): print this message
                -p (--project): project name (env: KARATE_PROJECT_NAME, default: PROJECT_NAME)
                -t (--threads): number of parallel threads (env: KARATE_THREADS_NUMBER, default: 1)
                -f (--features): path to the features to run (env: KARATE_FEATURES_DIR, default: /tmp/karate)
                -o (--output): path where the reports will be generated (env: KARATE_OUTPUT_DIR, default: target)
                -u (--url): base url for server requests (env: KARATE_BASE_URL, default: http://localhost:8080)

            Notice: Command line arguments override the environnement parameters.
        """.trimIndent())
    }
}

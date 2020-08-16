# Karate runner and report generator

Exemple of how to use Karate for running tests on another project.

Also, build as a command line tool that can be run to execute tests from a directory and generate the report.


## Build

To build the runnable jar run: 
```shell script
./gradlew fatJar
```

The Jar with all the dependencies will be created in `build/libs` 
called: `karate-tests-kotlin-gradle-1.0-SNAPSHOT-all.jar`
 
## Run

Run the jar using:
```
java -jar build/libs/karate-tests-kotlin-gradle-1.0-SNAPSHOT-all.jar
```

You can use either command line parameters or Env var to set up your configuration

```text
 Available parameters: 
     -h (--help): print this message
     -p (--project): project name for the report (env: KARATE_PROJECT_NAME, default: PROJECT_NAME)
     -t (--threads): number of parallel threads (env: KARATE_THREADS_NUMBER, default: 1)
     -f (--features): path to the features to run (env: KARATE_FEATURES_DIR, default: /tmp/karate)
     -o (--output): path where the reports will be generated (env: KARATE_OUTPUT_DIR, default: target)
     -u (--url): base url for server requests (env: KARATE_BASE_URL, default: http://localhost:8080)
```

Env vars: 
- KARATE_FEATURES_DIR (default: `/tmp/karate`)
- KARATE_PROJECT_NAME (default: `PROJECT_NAME`)
- KARATE_OUTPUT_DIR (default: `target`)
- KARATE_THREADS_NUMBER (default: `1`)
- KARATE_BASE_URL (default: `http://localhost:8080`)


*Notice: Command line arguments override the environnement parameters.*

ex:
```
java -jar build/libs/karate-tests-kotlin-gradle-1.0-SNAPSHOT-all.jar\
    -p MyTestProject\
    -t 2\
    -f ./build/libs/karate\
    -u http://google.com\
    -o /tmp/report
```
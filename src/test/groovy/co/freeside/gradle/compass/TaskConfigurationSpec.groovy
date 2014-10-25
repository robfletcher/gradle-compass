package co.freeside.gradle.compass

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TaskConfigurationSpec extends Specification {

  Project project
  CompassTask task

  def setup() {
    project = ProjectBuilder.builder().build()
    project.with {
      apply plugin: "co.freeside.compass"
    }
    task = project.tasks.findByName("compassCompile")
  }

  def "the default path for #property is #expectedPath"() {
    expect:
    task[property] == project.file(expectedPath)

    and:
    with(task.scriptArgs()) {
      def i = indexOf(argument)
      i >= 0
      get(i + 1) == "$project.rootDir/$expectedPath"
    }

    where:
    property  | expectedPath
    "sassDir" | "src/main/sass"
    "cssDir"  | "build/stylesheets"

    argument = toArgument(property)
  }

  def "#property is not set by default"() {
    expect:
    task[property] == null

    and:
    !task.scriptArgs().contains(argument)

    where:
    property         | _
    "imagesDir"      | _
    "javascriptsDir" | _
    "fontsDir"       | _
    "config"         | _

    argument = toArgument(property)
  }

  def "can specify a path for #property"() {
    given:
    project.with {
      compass[property] = file(path)
    }

    expect:
    task[property] == project.file(path)

    and:
    with(task.scriptArgs()) {
      def i = indexOf(argument)
      i >= 0
      get(i + 1) == "$project.rootDir/$path"
    }

    where:
    property         | path
    "sassDir"        | "src/sass"
    "cssDir"         | "build/css"
    "imagesDir"      | "src/images"
    "javascriptsDir" | "src/js"
    "fontsDir"       | "src/fonts"
    "config"         | "src/resources/config.rb"

    argument = toArgument(property)
  }

  def "can specify a value for #property"() {
    given:
    project.with {
      compass[property] = value
    }

    expect:
    task[property] == value

    and:
    with(task.scriptArgs()) {
      def i = indexOf(argument)
      i >= 0
      get(i + 1) == value
    }

    where:
    property              | value
    "environment"         | "production"
    "httpPath"            | "http"
    "generatedImagesPath" | "generated"
    "outputStyle"         | "compact"

    argument = toArgument(property)
  }

  def "#property is disabled by default"() {
    expect:
    task[property] == false

    and:
    !task.scriptArgs().contains(argument)

    where:
    property         | _
    "sourcemap"      | _
    "time"           | _
    "debugInfo"      | _
    "quiet"          | _
    "trace"          | _
    "force"          | _
    "boring"         | _
    "relativeAssets" | _
    "noLineComments" | _

    argument = toArgument(property)
  }

  def "can enable #property"() {
    given:
    project.with {
      compass[property] = true
    }

    expect:
    task[property] == true

    and:
    task.scriptArgs().contains(argument)

    where:
    property         | _
    "sourcemap"      | _
    "time"           | _
    "debugInfo"      | _
    "quiet"          | _
    "trace"          | _
    "force"          | _
    "boring"         | _
    "relativeAssets" | _
    "noLineComments" | _

    argument = toArgument(property)
  }

  def "can specify #paths.size() import directories"() {
    given:
    project.with {
      compass {
        importPath = files(*paths)
      }
    }

    expect:
    def args = task.scriptArgs()
    def indexes = args.findIndexValues { it == "--import-path" }
    indexes.collect { i -> args[((int) i) + 1] } == paths.collect { "$project.rootDir/$it" }

    where:
    paths                       | _
    ["someDir"]                 | _
    ["someDir", "someOtherDir"] | _
  }

  private String toArgument(String property) {
    def result = "--" + property.replaceAll(~/[A-Z]/) {
      "-${it.toLowerCase()}"
    }
    println "$property = $result"
    result
  }
}

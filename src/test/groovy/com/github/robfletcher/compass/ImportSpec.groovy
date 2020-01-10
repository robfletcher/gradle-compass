package com.github.robfletcher.compass

class ImportSpec extends CompassPluginSpec {

  def "can import from an external directories"() {
    given:
    buildFile << '''
      compass {
        importPath = files("src/import1/sass", "src/import2/sass")
      }
    '''

    and:
    file("src/import1/sass/_variables.scss") << '''$font: Georgia, serif;'''
    file("src/import2/sass/_rules.scss") << '''body { font-family: $font; }'''
    file("src/main/sass/main.scss") << '''
      @import "variables";
      @import "rules";
    '''

    when:
    runTasksSuccessfully COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/main.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

}

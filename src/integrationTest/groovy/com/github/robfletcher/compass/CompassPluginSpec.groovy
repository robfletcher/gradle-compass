package com.github.robfletcher.compass

import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import org.gololang.gradle.test.integration.framework.IntegrationSpec
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSRuleList
import spock.lang.Shared

abstract class CompassPluginSpec extends IntegrationSpec {

  @Shared parser = new CSSOMParser(new SACParserCSS3())
  protected final COMPILE_TASK_NAME = "compassCompile"
  protected final CLEAN_TASK_NAME = "compassClean"
  protected final WATCH_TASK_NAME = "compassWatch"

  def setup() {
    buildFile.text = """
      buildscript {
        repositories {
          maven {
            url "file://${localRepoLocation()}"
            jcenter()
          }
        }
        dependencies {
          classpath "com.github.robfletcher:compass-gradle-plugin:+"
        }
        configurations.all {
          resolutionStrategy.cacheDynamicVersionsFor 0, "seconds"
        }
      }
      apply plugin: "com.github.robfletcher.compass"
    """
  }

  protected static String localRepoLocation() {
    System.properties."localRepo.location"
  }

  protected CSSRuleList stylesheet(String path) {
    stylesheet file(path)
  }

  protected CSSRuleList stylesheet(File file) {
    assert file, "file $file.path does not exist"
    file.withReader { r ->
      parser.parseStyleSheet(new InputSource(r), null, null).cssRules
    }
  }
}

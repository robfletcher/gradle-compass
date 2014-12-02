package com.github.robfletcher.compass

import spock.util.concurrent.PollingConditions

class WatchSpec extends CompassPluginSpec {

  def conditions = new PollingConditions(timeout: 60, initialDelay: 5, delay: 1)

  File srcFile, targetFile

  def setup() {
    srcFile = file("src/main/sass/styles.scss")
    srcFile << '''
      $font: Georgia, serif;
      body { font-family: $font; }
    '''

    targetFile = new File(dir.root, "build/stylesheets/styles.css")
  }

  def "compiles stylesheets on start"() {
    when: "the watch task starts"
    startWatching()

    then: "the stylesheet gets compiled"
    waitUntilFileExists targetFile
    with(stylesheet(targetFile)) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

  def "recompiles stylesheets when they change"() {
    given: "the watch task is running"
    startWatching()
    waitUntilFileExists targetFile

    when: "the stylesheet is changed"
    srcFile.write '''
      $font: Optima, serif;
      body { font-family: $font; }
    '''

    then: "the stylesheet is recompiled"
    conditions.eventually {
      assert targetFile.lastModified() >= srcFile.lastModified()
    }
    with(stylesheet(targetFile)) {
      item(0).cssText == "body { font-family: Optima, serif }"
    }
  }

  def "new stylesheets are compiled"() {
    given:
    startWatching()
    waitUntilFileExists targetFile

    when: "a new stylesheet is created"
    def newFile = file("src/main/sass/new.scss")
    newFile << '''
      $size: 16px;
      body { font-size: $size; }
    '''

    then: "the new stylesheet is compiled"
    waitUntilFileExists new File(dir.root, "build/stylesheets/new.css")
  }

  private void startWatching() {
    runInBackground WATCH_TASK_NAME
    conditions.eventually {
      assert standardOutput.any {
        it.contains "Compass is watching for changes."
      }
    }
  }

  private void waitUntilFileExists(File file) {
    conditions.eventually {
      assert file.exists()
    }
  }

  protected void runInBackground(String... tasks) {
    Thread.start {
      run(tasks)
    }
  }
}

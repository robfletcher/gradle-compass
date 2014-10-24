package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

class CompassTask extends JRubyExec {

  String command
  @InputDirectory File sassDir
  @OutputDirectory File cssDir

  CompassTask() {
    jrubyArgs "-S"
    script = new File("compass")
    configuration = "compass"
//
//    sassDir = project.compass.getSassDir()
//    cssDir = project.compass.getCssDir()
  }

  @Override
  void exec() {
    scriptArgs command, "--sass-dir", getSassDir(), "--css-dir", getCssDir()
    println "Using: ${scriptArgs()}"
    super.exec()
  }
}

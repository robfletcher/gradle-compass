package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

import static co.freeside.gradle.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyExec {

  String command
  @InputDirectory File sassDir
  @OutputDirectory File cssDir

  CompassTask() {
    script = new File("compass")
    configuration = CONFIGURATION_NAME
  }

  @Override
  List<String> jrubyArgs() {
    def jrubyArgs = super.jrubyArgs()
    if (!jrubyArgs.contains("-S")) {
      jrubyArgs << "-S"
    }
    return jrubyArgs
  }

  @Override
  List<String> scriptArgs() {
    def scriptArgs = [command]
    scriptArgs << "--sass-dir" << getSassDir().path
    scriptArgs << "--css-dir" << getCssDir().path
    scriptArgs.addAll(super.scriptArgs())
    return scriptArgs
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}

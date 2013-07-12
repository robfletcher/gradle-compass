

import org.gradle.GradleLauncher
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

/**
 * Created with IntelliJ IDEA.
 * User: koen
 * Date: 17/05/12
 * Time: 21:01
 */
class ProjectWrapper {
  /*@Delegate*/ Project project

  def runTasks(String... tasks) {
    runTasks([:], tasks as List<String>)
  }

  def runTasks(Map<String, Object> args, String... tasks) {
    runTasks(args, tasks as List<String>)
  }

  def runTasks(Map<String, Object> args, List<String> tasks) {
    if (OperatingSystem.current().isWindows()) {
      tasks.remove 'clean'
    }

    def startParameter = project.gradle.startParameter.newBuild()
    startParameter.projectDir = project.projectDir
    startParameter.buildFile = new File(project.projectDir, args.buildScript?:'build.gradle')
    startParameter.taskNames = tasks
    def launcher = GradleLauncher.newInstance(startParameter)
    launcher.addListener(new GradleBuildTestListener())
    def result = launcher.run()
    result.rethrowFailure()
  }

  def file(path) {
    project.file(path)
  }

    @Override
    String toString() {
        return project.name
    }
}

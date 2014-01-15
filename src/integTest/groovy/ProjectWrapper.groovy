import org.gradle.*
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.internal.os.OperatingSystem

class ProjectWrapper {

  Project project

  BuildResult runTasks(String... tasks) {
    runTasks([:], tasks as List<String>)
  }

  BuildResult runTasks(Map<String, Object> args, String... tasks) {
    runTasks(args, tasks as List<String>)
  }

  BuildResult runTasks(Map<String, Object> args, List<String> tasks) {
    if (OperatingSystem.current().isWindows()) {
      tasks.remove "clean"
    }

    def startParameter = project.gradle.startParameter.newBuild()
    startParameter.projectDir = project.projectDir
    startParameter.buildFile = new File(project.projectDir, args.buildScript ?: "build.gradle")
    startParameter.taskNames = tasks
    def launcher = GradleLauncher.newInstance(startParameter)
    launcher.addListener(new GradleBuildTestListener())
    def result = launcher.run()
    result.rethrowFailure()
  }

  File file(path) {
    project.file(path)
  }

  ConfigurableFileTree fileTree(path) {
    project.fileTree(path)
  }

  @Override
  String toString() {
    project.name
  }
}

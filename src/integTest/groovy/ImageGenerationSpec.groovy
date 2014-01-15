import org.gradle.testfixtures.ProjectBuilder
import spock.lang.*
import static groovy.io.FileType.FILES

class ImageGenerationSpec extends Specification {

  def projectDir = new File(System.getProperty("integTest.projects"))
  def spritesProjectDir = new File(projectDir, "sprites")

  def cleanup() {
    new File(spritesProjectDir, "images").deleteDir()

    new File(spritesProjectDir, "src/main/webapp/css").deleteDir()
    new File(spritesProjectDir, "src/main/webapp/img").eachFile(FILES) {
      it.delete()
    }
  }

  def "sprites image is generated inside images dir"() {
    given:
    def project = new ProjectWrapper(project: ProjectBuilder.builder().withProjectDir(spritesProjectDir).build())

    when:
    project.runTasks "compileSass"

    then:
    project.fileTree("src/main/webapp/img").include("*.png").files.size() == 1
    project.fileTree("images").include("*.png").files.size() == 0
  }
}

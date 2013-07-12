import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.*

/**
 * User: ko
 * Date: 07/07/13
 * Time: 21:53
 */
class CompileSassTaskTest {

    def projectDir = new File(System.getProperty('integTest.projects'))
    def spritesProjectDir = new File(projectDir, "sprites")

    @Before
    public void setup(){
        new File(spritesProjectDir, "images")?.deleteDir()

        new File(spritesProjectDir, "src\\main\\webapp\\css")?.deleteDir()
        new File(spritesProjectDir, "src\\main\\webapp\\img\\icons-s11962582c8.png")?.delete()
    }

    @Test
    public void spritesImageIsGeneratedInsideImagesDir() {

        def project = new ProjectWrapper(project: ProjectBuilder.builder().withProjectDir(spritesProjectDir).build())
        project.runTasks "compileSass"

        assertTrue(project.file("src/main/webapp/img/icons-s11962582c8.png").exists())
        assertFalse(project.file("images/icons-s11962582c8.png").exists())
    }
}

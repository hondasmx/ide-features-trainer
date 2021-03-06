package training.learn.lesson.general

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.util.PsiTreeUtil
import training.commands.kotlin.TaskContext
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.parseLessonSample

class MultipleSelectionHtmlLesson(module: Module) : KLesson("Multiple Selections", module, "HTML") {
  private val sample = parseLessonSample("""<!doctype html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Multiple selections</title>
    </head>
    <body>
        <table>
            <tr>
                <<caret>th>Firstname</th>
                <th>Lastname</th>
                <th>Points</th>
            </tr>
            <tr>
                <td>Eve</td>
                <td>Jackson</td>
                <td>94</td>
            </tr>
        </table>
    </body>
</html>
""".trimIndent())

  override val lessonContent: LessonContext.() -> Unit
    get() = {
      prepareSample(sample)

      actionTask("SelectNextOccurrence") {
        "Press ${action(it)} to select the symbol at the caret."
      }
      actionTask("SelectNextOccurrence") {
        "Press ${action(it)} again to select the next occurrence of this symbol."
      }
      actionTask("UnselectPreviousOccurrence") {
        "Press ${action(it)} to deselect the last occurrence."
      }
      actionTask("SelectAllOccurrences") {
        "Press ${action(it)} to select all occurrences in the file."
      }
      task {
        text("Type <code>td</code> to replace all occurrences of <code>th</code> with <code>td</code>.")
        stateCheck { checkMultiChange() }
        test { type("td") }
      }
    }

  private fun TaskContext.checkMultiChange(): Boolean {
    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)

    val childrenOfType1 = PsiTreeUtil.findChildrenOfType(psiFile, HtmlTag::class.java)

    var count = 0

    for (htmlTag in childrenOfType1) {
      if (htmlTag.name == "th") return false
      if (htmlTag.name == "td") count++
    }
    return count == 6
  }
}
package org.jetbrains.training.eduUI;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.training.eduUI.panel.EduPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.List;


public class EduEditor implements TextEditor {

    private Project myProject;
    private FileEditor myDefaultEditor;
    private JComponent myComponent;


    public EduEditor(@NotNull final Project project, @NotNull final VirtualFile file) {

        myProject = project;
        myDefaultEditor = TextEditorProvider.getInstance().createEditor(myProject, file);
        myComponent = myDefaultEditor.getComponent();
        final EduPanel eduPanel = new EduPanel(275);
        myComponent.add(eduPanel, BorderLayout.WEST);
    }



    private FileEditor getDefaultEditor() {
        return myDefaultEditor;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myDefaultEditor.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "Edu Editor";
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return myDefaultEditor.getState(level);
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        myDefaultEditor.setState(state);
    }

    @Override
    public boolean isModified() {
        return myDefaultEditor.isModified();
    }

    @Override
    public boolean isValid() {
        return myDefaultEditor.isValid();
    }

    @Override
    public void selectNotify() {
        myDefaultEditor.selectNotify();
    }

    @Override
    public void deselectNotify() {
        myDefaultEditor.deselectNotify();
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        myDefaultEditor.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        myDefaultEditor.removePropertyChangeListener(listener);
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return myDefaultEditor.getBackgroundHighlighter();
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return myDefaultEditor.getCurrentLocation();
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return myDefaultEditor.getStructureViewBuilder();
    }

    @Override
    public void dispose() {
        Disposer.dispose(myDefaultEditor);
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> key) {
        return myDefaultEditor.getUserData(key);
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        myDefaultEditor.putUserData(key, value);
    }


    @Nullable
    public static EduEditor getSelectedEduEditor(@NotNull final Project project) {
        try {
            final FileEditor fileEditor = FileEditorManagerEx.getInstanceEx(project).getSplitters().getCurrentWindow().
                    getSelectedEditor().getSelectedEditorWithProvider().getFirst();
            if (fileEditor instanceof EduEditor) {
                return (EduEditor)fileEditor;
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }

    @Nullable
    public static Editor getSelectedEditor(@NotNull final Project project) {
        final EduEditor eduEditor = getSelectedEduEditor(project);
        if (eduEditor != null) {
            FileEditor defaultEditor = eduEditor.getDefaultEditor();
            if (defaultEditor instanceof PsiAwareTextEditorImpl) {
                return ((PsiAwareTextEditorImpl)defaultEditor).getEditor();
            }
        }
        return null;
    }



    @NotNull
    @Override
    public Editor getEditor() {
        if (myDefaultEditor instanceof TextEditor) {
            return ((TextEditor)myDefaultEditor).getEditor();
        }
        return EditorFactory.getInstance().createViewer(new DocumentImpl(""), myProject);
    }

    @Override
    public boolean canNavigateTo(@NotNull Navigatable navigatable) {
        if (myDefaultEditor instanceof TextEditor) {
            ((TextEditor)myDefaultEditor).canNavigateTo(navigatable);
        }
        return false;
    }

    @Override
    public void navigateTo(@NotNull Navigatable navigatable) {
        if (myDefaultEditor instanceof TextEditor) {
            ((TextEditor)myDefaultEditor).navigateTo(navigatable);
        }
    }

    public static void deleteGuardedBlocks(@NotNull final Document document) {
        if (document instanceof DocumentImpl) {
            final DocumentImpl documentImpl = (DocumentImpl)document;
            List<RangeMarker> blocks = documentImpl.getGuardedBlocks();
            for (final RangeMarker block : blocks) {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(new Runnable() {
                            @Override
                            public void run() {
                                document.removeGuardedBlock(block);
                            }
                        });
                    }
                });
            }
        }
    }
}
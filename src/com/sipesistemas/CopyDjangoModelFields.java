package com.sipesistemas;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;

import java.awt.datatransfer.StringSelection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR;

public class CopyDjangoModelFields extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        StringBuilder fields = new StringBuilder();
        Editor editor = e.getData(EDITOR);
        if (editor == null) {
            return;
        }
        String textoSelecionado = editor.getCaretModel().getCurrentCaret().getEditor()
                .getSelectionModel().getSelectedText();
        String regex = "(\\S+)\\s*=\\s*[^(]*\\(([^)]*)\\)";
        Pattern pattern = Pattern.compile(regex);

        if (textoSelecionado == null) {
            return;
        }

        Matcher m = pattern.matcher(textoSelecionado);
        boolean first = true;
        while (m.find()) {
            if (first) {
                fields.append('(');
                first = false;
            }
            fields.append("'");
            fields.append(m.group(1));
            fields.append("', ");
        }
        if (!first) {
            fields.append(")");
        }
        CopyPasteManager.getInstance().setContents(new StringSelection(fields.toString()));
    }
}

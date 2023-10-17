package app.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

public class SearchConditionStringFilter extends DocumentFilter {

    private final String regex = "[\\w%]";

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String str, final AttributeSet attrs) throws BadLocationException {
        if (Pattern.matches(this.regex, str)) {
            super.insertString(fb, offset, str, attrs);
        }
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String str, final AttributeSet attrs) throws BadLocationException {
        if (Pattern.matches(this.regex, str)) {
            super.insertString(fb, offset, str, attrs);
        }
    }
}
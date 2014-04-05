package com.gparser.editors;

import com.gparser.files.data.CommentIndicator;

/**
 * Created by Gilad Ber on 3/22/14.
 */
public class CommentLineEditor implements LineEditor
{
    private final CommentIndicator indicator;

    public CommentLineEditor(CommentIndicator indicator)
    {
        this.indicator = indicator;
    }

    @Override
    public String editLine(String line)
    {
        return line.replaceFirst(indicator.value, "");
    }
}

package com.apollottb.ticketparser;

import java.util.regex.Pattern;

public interface Searchable<T extends PdfWord>
{
	public Pattern getPattern();
	public T createPdfWord();
}

package com.gparser.raw;

/**
 * Created by Gilad Ber on 4/4/14.
 */
public class ParserMetaData
{
	public final String commentIndicator;
	public final String titleIndicator;

	private ParserMetaData(String commentIndicator, String titleIndicator)
	{
		this.commentIndicator = commentIndicator;
		this.titleIndicator = titleIndicator;
	}

	public static final class Builder
	{
		private String commentIndicator = "%%";
		private String titleIndicator = "%#";

		public Builder commentIndicator(String s)
		{
			this.commentIndicator = s;
			return this;
		}

		public Builder titleIndicator(String s)
		{
			this.titleIndicator = s;
			return this;
		}

		public ParserMetaData build()
		{
			return new ParserMetaData(this.commentIndicator, this.titleIndicator);
		}

	}
}

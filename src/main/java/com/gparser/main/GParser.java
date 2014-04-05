package com.gparser.main;

import com.gparser.ui.MainWindow;

/**
 * Application executor.
 * Created by Gilad Ber on 3/29/14.
 */
public class GParser
{

	public void execute()
	{
		try
		{
			new MainWindow().setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}

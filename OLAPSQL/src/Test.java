/*
 * SOAP Supervising, Observing, Analysing Projects
 * Copyright (C) 2003-2004 SOAPteam
 * 
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import javaCC.Exemple;
import javaCC.ParseException;

/**
 * @author claire
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test {

	public static void main(String[] args) {
		
		Exemple parser=null;
		String cde= "drop fact test;";
		try {
			FileReader f = new FileReader("T:\\IUP Master 1\\sem2\\BOT\\projet3 BOT\\exemple.olapsql");
			if (parser == null)
			{
				parser = new Exemple (f);
			}
			else
			{
				parser.ReInit(f);
			}
			try {
				parser.execute();
				System.out.println("ok");
			} catch (ParseException e) {
				System.out.println("Erreur de syntaxe OLAPSQL");
				e.printStackTrace();

			}
		
		
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}
}

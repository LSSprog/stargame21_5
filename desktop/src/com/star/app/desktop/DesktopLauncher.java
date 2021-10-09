package com.star.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.star.app.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new StarGame(), config);
	}

//	1. Разобраться с кодом
//  2. Кнопка паузы и выхода в меню на игровом экране.
//  3. * Сделать GameOverScreen. Если HP < 0 то игра должна перейти в GameOverScreen, и отображать там статистику ( можно счет )
//
//	По нажатию на экране GameOverScreen возвращаемся в экран меню
}

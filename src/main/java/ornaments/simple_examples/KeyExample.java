package ornaments.simple_examples;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyExample {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Key Listener Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Создание компонента, на котором будет отслеживаться нажатие клавиши
        JPanel panel = new JPanel();

        // Добавление слушателя клавиатуры к панели
        panel.setFocusable(true); // Установка фокуса для получения событий клавиатуры
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Опционально: что-то выполняется при вводе символа (в том числе, Unicode-символы)
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Опционально: что-то выполняется при нажатии клавиши
                int keyCode = e.getKeyCode();
                System.out.println("Key pressed: " + KeyEvent.getKeyText(keyCode));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Опционально: что-то выполняется при отпускании клавиши
                int keyCode = e.getKeyCode();
                System.out.println("Key released: " + KeyEvent.getKeyText(keyCode));
            }
        });

        frame.getContentPane().add(panel); // Добавляем панель в контент-пейн фрейма
        frame.setVisible(true);
    }
}


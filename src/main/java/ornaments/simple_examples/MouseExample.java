package ornaments.simple_examples;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseExample {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Click Listener Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Создание компонента, на котором будет отслеживаться клик мыши
        JButton button = new JButton("Click me!");

        // Добавление слушателя мыши к кнопке
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Button clicked!");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Опционально: что-то выполняется при нажатии кнопки мыши
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Опционально: что-то выполняется при отпускании кнопки мыши
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Опционально: что-то выполняется, когда курсор входит в область компонента
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Опционально: что-то выполняется, когда курсор покидает область компонента
            }
        });

        frame.getContentPane().add(button); // Добавляем кнопку в контент-пейн фрейма
        frame.setVisible(true);
    }
}

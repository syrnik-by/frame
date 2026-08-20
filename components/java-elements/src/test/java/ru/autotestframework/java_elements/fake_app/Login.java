package ru.autotestframework.java_elements.fake_app;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField = new JTextField();
    private JTextField lockedField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JButton cancelButton = new JButton("Cancel");
    private JButton loginButton = new JButton("Login");
    private JCheckBox rememberMe = new JCheckBox("Remember me");

    public Login() {
        super("Login");
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }

    private void initComponents() {
        nameField.setName("userName");
        nameField.getDocument().addDocumentListener(getDocumentListener());
        passwordField.setName("password");
        passwordField.getDocument().addDocumentListener(getDocumentListener());
        PanelBuilder builder = new PanelBuilder(
                new FormLayout("left:pref, 3dlu, pref:g", "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref"));
        builder.setBorder(Borders.DIALOG_BORDER);
        CellConstraints labelConstraints = new CellConstraints();
        CellConstraints compConstraints = new CellConstraints();
        builder.addLabel("&Name: ", labelConstraints.xy(1, 1), nameField, compConstraints.xywh(3, 1, 1, 1));
        builder.addLabel("Password: ", labelConstraints.xy(1, 3), passwordField, compConstraints.xy(3, 3));
        rememberMe.setName("rememberMe");
        builder.add(rememberMe, compConstraints.xy(3, 5));
        ButtonBarBuilder buttonBarBuilder = new ButtonBarBuilder();
        buttonBarBuilder.addGlue();
        buttonBarBuilder.addGriddedButtons(new JButton[] {cancelButton, loginButton});
        JPanel buttonBar = buttonBarBuilder.getPanel();
        builder.add(buttonBar, compConstraints.xyw(1, 7, 3));

        lockedField.setName("DisabledField");
        lockedField.setEnabled(false);
        lockedField.getDocument().addDocumentListener(getDocumentListener());
        builder.addLabel("DisabledField: ", labelConstraints.xy(1, 9), lockedField, compConstraints.xywh(3, 9, 1, 1));

        JPanel panel = builder.getPanel();
        Container contentPane = getContentPane();
        contentPane.add(panel);
        loginButton.setName("login");
        loginButton.setEnabled(false);
        cancelButton.setName("cancel");
        cancelButton.addActionListener(e -> dispose());
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("Test"));
        cancelButton.setComponentPopupMenu(popup);
        loginButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    Login.this,
                    "You logged in as: " + nameField.getText() + " with password: " + passwordField.getText(),
                    "Login Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        pack();
    }

    private DocumentListener getDocumentListener() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLoginState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLoginState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLoginState();
            }
        };
    }

    private void updateLoginState() {
        loginButton.setEnabled(nameField.getText().length() != 0 && passwordField.getPassword().length != 0);
    }
}

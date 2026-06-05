package ui.panel;

import service.AuthService;
import ui.component.GradientPanel;
import ui.component.RoundedButton;
import ui.component.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginPanel {
    private final AuthService authService;
    private final Runnable onLoginBerhasil;

    public LoginPanel(AuthService authService, Runnable onLoginBerhasil) {
        this.authService = authService;
        this.onLoginBerhasil = onLoginBerhasil;
    }

    public void tampilkan() {
        JDialog dialog = new JDialog((Frame) null, "Login Sistem Perpustakaan", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        GradientPanel header = new GradientPanel(
                new Color(255, 192, 203), new Color(216, 191, 216));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(15, 10, 15, 10));
        JLabel lblTitle = new JLabel("LOGIN PETUGAS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);
        dialog.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 20));
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 20, 10, 20));
        JTextField txtNip = UIFactory.createTextField("");
        JPasswordField txtPass = UIFactory.createPasswordField();
        form.add(new JLabel("NIP:")); form.add(txtNip);
        form.add(new JLabel("Password:")); form.add(txtPass);
        dialog.add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnPanel.setBackground(Color.WHITE);
        RoundedButton btnLogin = new RoundedButton("Login", 20);
        btnLogin.setBackground(UIFactory.COLOR_PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        RoundedButton btnDaftar = new RoundedButton("Buat Akun", 20);
        btnDaftar.setBackground(new Color(255, 200, 221));
        btnDaftar.setForeground(Color.WHITE);
        btnPanel.add(btnLogin);
        btnPanel.add(btnDaftar);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> prosesLogin(dialog, txtNip, txtPass));
        btnDaftar.addActionListener(e -> new RegisterPanel(authService).tampilkan(dialog));
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        dialog.setVisible(true);
    }

    private void prosesLogin(JDialog dialog, JTextField txtNip, JPasswordField txtPass) {
        String nip  = txtNip.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();
        if (nip.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Silakan masukkan NIP dan Password Anda!");
            return;
        }
        AuthService.HasilLogin hasil = authService.login(nip, pass);
        switch (hasil) {
            case BERHASIL:
                dialog.dispose();
                onLoginBerhasil.run();
                break;
            case BELUM_ADA_AKUN:
                JOptionPane.showMessageDialog(dialog,
                        "Data anda tidak ditemukan, silahkan buat akun terlebih dahulu.");
                break;
            case SALAH_KREDENSIAL:
                JOptionPane.showMessageDialog(dialog, "NIP/Password Salah!");
                break;
        }
    }
}

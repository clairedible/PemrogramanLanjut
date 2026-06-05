package ui.panel;

import model.Pegawai;
import service.AuthService;
import ui.component.UIFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class RegisterPanel {
    private final AuthService authService;

    public RegisterPanel(AuthService authService) {
        this.authService = authService;
    }

    public void tampilkan(JDialog parent) {
        JDialog dialog = new JDialog(parent, "Registrasi", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField tNip  = UIFactory.createTextField("");
        JTextField tNama = UIFactory.createTextField("");
        JPasswordField tPass = UIFactory.createPasswordField();

        JSpinner tTglLahir = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor de = new JSpinner.DateEditor(tTglLahir, "dd-MM-yyyy");
        tTglLahir.setEditor(de);
        tTglLahir.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tTglLahir.setPreferredSize(new Dimension(280, 35));

        form.add(new JLabel("NIP:"));           form.add(tNip);
        form.add(new JLabel("Nama:"));          form.add(tNama);
        form.add(new JLabel("Tanggal Lahir:")); form.add(tTglLahir);
        form.add(new JLabel("Password:"));      form.add(tPass);

        JButton btnSimpan  = new JButton("Simpan");
        JButton btnCancel  = new JButton("Cancel");

        btnSimpan.addActionListener(e -> {
            String nip  = tNip.getText().trim();
            String nama = tNama.getText().trim();
            String pass = new String(tPass.getPassword()).trim();
            String tgl  = new SimpleDateFormat("dd-MM-yyyy").format(tTglLahir.getValue());
            if (nip.isEmpty() || nama.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Silakan lengkapi semua form pendaftaran Anda!");
                return;
            }
            authService.daftarPegawai(new Pegawai(nip, nama, pass, tgl));
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        south.add(btnSimpan);
        south.add(btnCancel);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(south, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
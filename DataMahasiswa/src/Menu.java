import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Main()
public class Menu extends JFrame {
    public static void main(String[] args) {
        // Buat objek window
        Menu window = new Menu();

        // Atur ukuran window
        window.setSize(480, 560);
        // Letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        //isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JSlider sliderIPK;
    private JLabel IPKLabel;


    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        // buat objek database
        database = new Database();
        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD,20f));

        // atur isi combo box
        String[] jenisKelaminData = {"","Laki-laki","Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(selectedIndex == -1){
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // inisialisasi JSlider
        sliderIPK.setMinimum(0);
        sliderIPK.setMaximum(40); // 4.0 IPK * 10 untuk presisi
        sliderIPK.setValue(20); // Nilai awal 2.0
        sliderIPK.setPaintTicks(true);
        sliderIPK.setPaintLabels(true);
        sliderIPK.setMajorTickSpacing(10);
        sliderIPK.setMinorTickSpacing(1);

        // Menambahkan listener untuk JSlider
        sliderIPK.addChangeListener(e -> {
            int value = sliderIPK.getValue();
            double ipk = value / 10.0;
            IPKLabel.setText("IPK: " + ipk);
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //saat tombol
                if (selectedIndex != -1) {
                    deleteData();
                }
            }

        });

// saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNIM = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                double selectedIPK = Double.parseDouble(mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString());

                // ubah isi textfield dan combo box
                nimField.setText(selectedNIM);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                sliderIPK.setValue((int)(selectedIPK*10));
                // ubah tombol "Add" menjadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });

    }
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin","IPK"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try{
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");

            int i =0;
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("IPKsemester");
                temp.addRow(row);
                i++;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return temp;
    }
    private boolean isNimExists(String nim) {
        try {
            ResultSet rs = database.selectQuery("SELECT COUNT(*) FROM mahasiswa WHERE nim = '" + nim + "'");
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insertData() {
        if (nimField.getText().isEmpty() || namaField.getText().isEmpty() ||
                jenisKelaminComboBox.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();

        double ipk = sliderIPK.getValue()/10.0;

        if (isNimExists(nim)) {
            JOptionPane.showMessageDialog(this,
                    "NIM sudah terdaftar!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // tambahkan data ke dalam list
        String sql = "INSERT INTO mahasiswa VALUES (null,'"+nim+"','"+nama+"','"+jenisKelamin+"','"+ipk+"');";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    public void updateData() {
        if (nimField.getText().isEmpty() || namaField.getText().isEmpty() ||
                jenisKelaminComboBox.getSelectedItem().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String originalNIM = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        double ipk = sliderIPK.getValue()/10.0;

        // ubah data mahasiswa di list
        /*listMahasiswa.get(selectedIndex).setNim(nim);
        listMahasiswa.get(selectedIndex).setNama(nama);
        listMahasiswa.get(selectedIndex).setJenisKelamin(jenisKelamin);
        listMahasiswa.get(selectedIndex).setIPKsemester(ipk);*/

        // update tabel
        //mahasiswaTable.setModel(setTable());

        // ganti database

        String sql = "UPDATE mahasiswa SET " +
                "nim = '" + nim + "', " +
                "nama = '" + nama + "', " +
                "jenis_kelamin = '" + jenisKelamin + "', " +
                "IPKsemester = " + ipk + " " +
                "WHERE nim = '" + originalNIM + "'";

        database.insertUpdateDeleteQuery(sql);

        mahasiswaTable.setModel(setTable());
        mahasiswaTable.clearSelection();
        mahasiswaTable.repaint();

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Update berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
    }

    public void deleteData() {
        // Tampilkan dialog konfirmasi
        String nimToDelete = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
        int confirmation = JOptionPane.showConfirmDialog(
                this, // Parent component
                "Apakah Anda yakin ingin menghapus data ini?", // Pesan konfirmasi
                "Konfirmasi Hapus", // Judul dialog
                JOptionPane.YES_NO_OPTION // Tampilkan tombol Yes dan No
        );

        // Jika pengguna memilih "Yes"
        if (confirmation == JOptionPane.YES_OPTION) {
            // Hapus data dari list
            //listMahasiswa.remove(selectedIndex);

            // ganti dari listmahasiswa jadi perintah sql ke database
            String sqlr = "DELETE FROM mahasiswa WHERE nim = '"+ nimToDelete +"';";
            database.insertUpdateDeleteQuery(sqlr);

            // Update tabel
            mahasiswaTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Delete berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } else {
            // Jika pengguna memilih "No", tidak melakukan apa-apa
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public void clearForm() {
        // kosongkan semua textfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        sliderIPK.setValue(20);
        IPKLabel.setText("IPK: 2.0");
        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
    /*private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan",2.5));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki",3.5));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", 4));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", 3.6));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", 3.5));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", 1.3));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan",3.2));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", 3.2));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", 2.4));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", 2.8));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", 3.7));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", 3.3));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", 3.2));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", 3));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", 2.6));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", 2.1));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki",2.5 ));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", 3.2));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", 3.3));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", 2.4));
    }*/
}


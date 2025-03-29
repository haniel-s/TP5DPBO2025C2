# TP5DPBO2025C2
Saya Haniel Septian Putra Alren dengan NIM 2310978 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
# video ketika program dijalankan


https://github.com/user-attachments/assets/3b337ccd-0b86-4af3-9ba5-679715d302f9


# Desain Program
Program diupdate sehingga sekarang dapat terhubung dengan database. setiap proses CRUD sekarang mempengaruhi isi database mahasiswa. Karena pada program yang baru ini tidak ada kolom IPK maka saya menambahkannya secara manual. setelah terhubung pada Database, saya menambahkan alert jika nim pada data mahasiswa yang mau insert sudah ada pada database (menambahkan fungsi baru bernama isNimExist). Alert juga ditampilkan jika data yang mau di insert/update disaat mengisi terdapat field yang kosong. 

# Penjelasan Alur
Di tampilan awal terdapat menu input mahasiswa. data yang harus diinput adalah nim,nama, gender berupa dropdown, dan IPK berupa slider.
selanjutnya di bawahnya terdapat list dari mahasiswa yang sudah terdaftar.
alur pengunaan user :
1. user menginputkan nim,nama, memilih kelamin, memilih IPK mengunakan slider.
2. user dapat meng-click data yang mau diubah dari list mahasiswa yang terdaftar dibawah kolom input
3. setelah di click user dapat melakukan update data mahasiswa
4. setelah itu user dapat meng-click update dan data akan terupdate 
5. user dapat meng-click data yang mau dihapus dari list mahasiswa yang terdaftar dibawah kolom input
6. setelah di click user dapat melakukan delete pada data mahasiswa yang dipilih
7. user harus mengkonfirmasi dahulu apakah benar data ingin di delete
8. setelah click yes maka data akan dihapus
alur ketika cek alert :
1. tester menginput data tanpa mengisi salah satu dari nim/nama/jenis kelamin lalu klik add
2. klik salah satu data dari list mahasiswa dibawah lalu hapus salah satu dari nim/nama/jenis kelamin lalu klik update  
3. tester menginput data dengan mengisi nim yang sudah ada sebelumnya pada list mahasiswa lalu klik add

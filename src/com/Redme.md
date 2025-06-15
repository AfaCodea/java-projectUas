# Proyek Sistem Perpustakaan Universitas

## Anggota Kelompok
- Agil Prasunza
- Muhammad Raihan Firdaus
- Jordy

## Gambaran Proyek
Proyek ini adalah Sistem Perpustakaan berbasis Java yang mengimplementasikan berbagai konsep Pemrograman Berorientasi Objek (OOP). 
Sistem ini memungkinkan pengelolaan perpustakaan universitas dengan fitur seperti peminjaman buku, manajemen anggota, dan pencatatan transaksi.

## Implementasi Konsep OOP

### 1. Class dan Object
- `MainApp`: Class utama yang menjalankan aplikasi
- `Perpustakaan`: Class service yang mengelola logika bisnis
- `Buku`: Class yang merepresentasikan buku
- `Mahasiswa`: Class yang merepresentasikan anggota perpustakaan
- `Peminjaman`: Class yang merepresentasikan transaksi peminjaman
- `ItemPerpustakaan`: Class abstrak untuk item yang dapat dipinjam
- `DapatDipinjam`: Interface untuk item yang dapat dipinjam

### 2. Variabel dan Method
- **Variabel**:
  - Private instance variables: `nomorBuku`, `judul`, `pengarang`, `status`
  - Private instance variables: `nim`, `nama`, `jurusan`
  - Static variables untuk manajemen perpustakaan
- **Method**:
  - `pinjam()`: Method untuk meminjam buku
  - `kembalikan()`: Method untuk mengembalikan buku
  - `cekStatus()`: Method untuk mengecek status peminjaman
  - Berbagai method getter dan setter

### 3. Access Modifiers
- **Private**: Digunakan untuk data sensitif (nomor buku, data mahasiswa)
- **Public**: Digunakan untuk method yang perlu diakses dari class lain
- **Protected**: Digunakan dalam inheritance untuk class `ItemPerpustakaan`

### 4. Constructor
- Constructor default untuk inisialisasi objek
- Constructor dengan parameter untuk data buku dan mahasiswa
- Constructor overloading untuk berbagai skenario inisialisasi

### 5. Inheritance (Pewarisan)
- `ItemPerpustakaan` sebagai class abstrak induk
- `Buku` mewarisi dari `ItemPerpustakaan`
- Method overriding untuk perilaku khusus buku

### 6. Encapsulation (Enkapsulasi)
- Atribut private dengan method getter/setter public
- Implementasi penyembunyian data
- Akses terkontrol ke informasi buku dan peminjaman

### 7. Polymorphism (Polimorfisme)
- Interface `DapatDipinjam` untuk item yang dapat dipinjam
- Method overriding dalam class turunan
- Dynamic method dispatch untuk operasi peminjaman

### 8. Abstraction (Abstraksi)
- Class abstrak `ItemPerpustakaan`
- Method abstrak untuk operasi perpustakaan
- Template method pattern untuk proses peminjaman

### 9. Interface
- Interface `DapatDipinjam` untuk item yang dapat dipinjam
- Implementasi interface dalam class `Buku`
- Multiple interface implementation

### 10. Implementasi GUI
- Antarmuka berbasis Java Swing
- `LoginGUI` untuk autentikasi
- `DashboardGUI` untuk manajemen perpustakaan
- Event handling untuk interaksi pengguna dimana aplikasi itu sendiri menjadi responsive dan users frendly

## Fitur
1. **Manajemen Buku**
   - Pencatatan buku baru
   - Pencarian buku
   - Update status buku

2. **Manajemen Anggota**
   - Pendaftaran mahasiswa
   - Pencarian anggota
   - Riwayat peminjaman

3. **Operasi Peminjaman**
   - Proses peminjaman buku
   - Proses pengembalian
   - Pengecekan status

4. **Keamanan**
   - Sistem login
   - Validasi input
   - Pencatatan log aktivitas

## Detail Teknis
- **Bahasa**: Java
- **Framework GUI**: Java Swing
- **Design Pattern**: MVC (Model-View-Controller)
- **Logging**: Custom logger untuk pencatatan aktivitas

## Cara Menjalankan
1. Pastikan file `./run.sh` memiliki permission eksekusi:
   ```bash
   chmod +x run.sh
   ```

2. Jalankan aplikasi menggunakan script:
   ```bash
   ./run.sh
   ```

3. Tunggu hingga aplikasi dimuat dan tampilan login muncul

4. Login menggunakan kredensial yang tersedia:
   - Username: admin
   - Password: admin123

5. Setelah login berhasil, Anda akan diarahkan ke dashboard perpustakaan, cara ini lebih praktis karen
   developer tidak perlu menuliskan code compile dan run secara terpisah.
   Pemberian Izin Eksekusi (chmod +x run.sh): Ini adalah langkah penting dan benar agar script dapat dijalankan. Menjalankan Script (./run.sh): Ini adalah cara yang benar untuk menjalankan script di sistem operasi berbasis Unix/Linux.
   Kredensial Login: Menyediakan kredensial default sangat membantu pengguna. Manfaat run.sh: Poin tentang kepraktisan dan otomatisasi kompilasi/eksekusi sangat benar.

Catatan: Script `run.sh` akan secara otomatis:
- Compile semua file Java yang diperlukan
- Menjalankan class `MainApp`
- Mengatur classpath yang diperlukan
- Menangani error jika terjadi masalah

Jadi, sementara javac MainApp.java adalah langkah penting dalam memahami cara kerja Java, run.sh adalah solusi yang lebih praktis dan efisien untuk proyek yang lebih kompleks atau untuk otomatisasi.

Catatan Alternatif 'selain menggunakan run.sh':
- javac MainApp.java
- java MainApp  

## Pengembangan Selanjutnya
- Sistem notifikasi untuk pengembalian
- Fitur reservasi buku
- Laporan statistik peminjaman
- Integrasi dengan sistem akademik

---------- end ------------------

Thanks for all !!!!

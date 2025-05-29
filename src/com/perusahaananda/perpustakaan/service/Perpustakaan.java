package com.perusahaananda.perpustakaan.service;

import com.perusahaananda.perpustakaan.model.Mahasiswa;
import com.perusahaananda.perpustakaan.model.Buku;
import com.perusahaananda.perpustakaan.model.DapatDipinjam;
import com.perusahaananda.perpustakaan.model.ItemPerpustakaan;
import com.perusahaananda.perpustakaan.model.Peminjaman;
import com.perusahaananda.perpustakaan.util.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kelas service yang mengelola logika bisnis perpustakaan.
 */
public class Perpustakaan {
    // 2. Variables
    // 3. Access Modifiers: private untuk enkapsulasi
    private List<ItemPerpustakaan> koleksiItem;
    private List<Mahasiswa> daftarAnggota;
    private List<Peminjaman> daftarPeminjaman;

    // 4. Constructors
    public Perpustakaan() {
        this.koleksiItem = new ArrayList<>();
        this.daftarAnggota = new ArrayList<>();
        this.daftarPeminjaman = new ArrayList<>();
        // Inisialisasi data contoh
        inisialisasiDataContoh();
    }

    private void inisialisasiDataContoh() {
        tambahBuku(new Buku("Pemrograman Java", "John Doe", "978-123"));
        tambahBuku(new Buku("Algoritma dan Struktur Data", "Jane Smith", "978-456"));
        tambahBuku(new Buku("Basis Data", "Peter Jones", "978-789"));

        registrasiAnggota(new Mahasiswa("Alice", "A001"));
        registrasiAnggota(new Mahasiswa("Bob", "A002"));
    }

    // 2. Methods
    // Operasi untuk Buku (dan ItemPerpustakaan secara umum)
    public void tambahItem(ItemPerpustakaan item) {
        this.koleksiItem.add(item);
        Logger.log(item.getJenisItem() + " \"" + item.getJudul() + "\" berhasil ditambahkan.");
    }

    // 7. Polymorphism (Overloading): Metode tambahBuku spesifik jika diperlukan
    public void tambahBuku(Buku buku) {
        tambahItem(buku); // Menggunakan metode tambahItem yang lebih umum
    }

    public List<ItemPerpustakaan> getSemuaItem() {
        return new ArrayList<>(koleksiItem); // Mengembalikan salinan untuk proteksi
    }

    public List<Buku> getSemuaBuku() {
        return koleksiItem.stream()
                .filter(item -> item instanceof Buku)
                .map(item -> (Buku) item)
                .collect(Collectors.toList());
    }

    public Buku cariBukuByIsbn(String isbn) {
        for (ItemPerpustakaan item : koleksiItem) {
            if (item instanceof Buku) {
                Buku buku = (Buku) item;
                if (buku.getIsbn().equalsIgnoreCase(isbn)) {
                    return buku;
                }
            }
        }
        return null;
    }

    public List<Buku> cariBukuByJudul(String judul) {
        return koleksiItem.stream()
                .filter(item -> item instanceof Buku && item.getJudul().toLowerCase().contains(judul.toLowerCase()))
                .map(item -> (Buku) item)
                .collect(Collectors.toList());
    }

    // Operasi untuk Anggota
    public void registrasiAnggota(Mahasiswa anggota) {
        this.daftarAnggota.add(anggota);
        Logger.log("Anggota \"" + anggota.getNama() + "\" berhasil diregistrasi.");
    }

    public List<Mahasiswa> getSemuaAnggota() {
        return new ArrayList<>(daftarAnggota);
    }

    public Mahasiswa cariAnggotaById(String idAnggota) {
        for (Mahasiswa anggota : daftarAnggota) {
            if (anggota.getIdAnggota().equalsIgnoreCase(idAnggota)) {
                return anggota;
            }
        }
        return null;
    }

    // Operasi Peminjaman dan Pengembalian
    public boolean prosesPeminjaman(String isbnBuku, String idAnggota, LocalDate tanggalPinjam) {
        Buku buku = cariBukuByIsbn(isbnBuku);
        Mahasiswa anggota = cariAnggotaById(idAnggota);

        if (buku == null) {
            Logger.log("Error: Buku dengan ISBN " + isbnBuku + " tidak ditemukan.");
            return false;
        }
        if (anggota == null) {
            Logger.log("Error: Anggota dengan ID " + idAnggota + " tidak ditemukan.");
            return false;
        }

        if (buku.isTersedia() && buku instanceof DapatDipinjam) {
            DapatDipinjam itemDipinjam = (DapatDipinjam) buku;
            itemDipinjam.pinjamItem(anggota, tanggalPinjam);
            anggota.tambahKeRiwayat(buku);
            Peminjaman peminjaman = new Peminjaman(buku, anggota, tanggalPinjam);
            daftarPeminjaman.add(peminjaman);
            return true;
        } else {
            Logger.log("Error: Buku '" + buku.getJudul() + "' tidak tersedia atau tidak dapat dipinjam.");
            return false;
        }
    }

    public boolean prosesPengembalian(String isbnBuku) {
        Buku buku = cariBukuByIsbn(isbnBuku);

        if (buku == null) {
            Logger.log("Error: Buku dengan ISBN " + isbnBuku + " tidak ditemukan.");
            return false;
        }

        if (!buku.isTersedia() && buku instanceof DapatDipinjam) {
            DapatDipinjam itemDikembalikan = (DapatDipinjam) buku;
            for (Peminjaman p : daftarPeminjaman) {
                if (p.getBuku().getIsbn().equals(buku.getIsbn()) && p.getTanggalKembaliAktual() == null) {
                    p.setTanggalKembaliAktual(LocalDate.now());
                    break;
                }
            }
            itemDikembalikan.kembalikanItem();
            return true;
        } else {
            Logger.log("Error: Buku '" + buku.getJudul() + "' tidak sedang dipinjam atau tidak dapat dipinjam.");
            return false;
        }
    }

    public List<Peminjaman> getDaftarPeminjamanAktif() {
        return daftarPeminjaman.stream()
                .filter(p -> p.getTanggalKembaliAktual() == null && !p.getBuku().isTersedia())
                .collect(Collectors.toList());
    }

    public List<Peminjaman> getSemuaTransaksiPeminjaman() {
        return new ArrayList<>(daftarPeminjaman);
    }

    // Method untuk menghapus anggota
    public boolean hapusAnggota(String idAnggota) {
        Mahasiswa anggota = cariAnggotaById(idAnggota);
        
        if (anggota == null) {
            Logger.log("Error: Anggota dengan ID " + idAnggota + " tidak ditemukan.");
            return false;
        }

        // Cek apakah anggota memiliki peminjaman aktif
        boolean memilikiPeminjamanAktif = daftarPeminjaman.stream()
                .anyMatch(p -> p.getAnggota().equals(anggota) && p.getTanggalKembaliAktual() == null);

        if (memilikiPeminjamanAktif) {
            Logger.log("Error: Tidak dapat menghapus anggota " + anggota.getNama() + " karena masih memiliki peminjaman aktif.");
            return false;
        }

        // Hapus anggota
        daftarAnggota.remove(anggota);
        Logger.log("Anggota \"" + anggota.getNama() + "\" berhasil dihapus.");
        return true;
    }

    public List<Mahasiswa> getSemuaMahasiswa() {
        return new ArrayList<>(daftarAnggota);
    }

    public boolean hapusMahasiswa(String idMahasiswa) {
        Mahasiswa mahasiswa = cariMahasiswaById(idMahasiswa);
        if (mahasiswa == null) {
            return false;
        }
        return daftarAnggota.remove(mahasiswa);
    }

    public Mahasiswa cariMahasiswaById(String id) {
        for (Mahasiswa mahasiswa : daftarAnggota) {
            if (mahasiswa.getIdAnggota().equals(id)) {
                return mahasiswa;
            }
        }
        return null;
    }

    public void registrasiMahasiswa(Mahasiswa mahasiswaBaru) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrasiMahasiswa'");
    }
}
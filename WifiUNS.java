import java.util.*;

public class WifiUNS {
    static final int THRESHOLD = 150; // jarak max antar node untuk ada edge

    public static void main(String[] args) {
        // ArrayList untuk menyimpan semua titik wifi
        ArrayList<WifiPoint> daftar = getDaftarWifi();
        Graf graf = new Graf(daftar.size() + 1);

        // Bangun adjacency list: tambah edge jika jarak < THRESHOLD
        for (int i = 0; i < daftar.size(); i++) {
            for (int j = i + 1; j < daftar.size(); j++) {
                double jarak = hitungJarak(daftar.get(i), daftar.get(j));
                if (jarak < THRESHOLD) {
                    graf.tambahEdge(daftar.get(i).id, daftar.get(j).id, jarak);
                }
            } 
        } // --kompleksitas O(n^2) untuk membangun graf,karena harus memeriksa semua pasangan titik wifi--

        Scanner sc = new Scanner(System.in);
        System.out.println("=== SISTEM INFORMASI WIFI UNS ===\n");
        for (WifiPoint wp : daftar)
            System.out.printf("%d. %s (%s) - Kecepatan Internet: %d Mbps%n", wp.id, wp.namaGedung, wp.kategori, wp.kecepatan);

        System.out.print("\nMasukkan nomor lokasi (1-" + daftar.size() + "): ");
        int pilihan = 1;
        try {
            pilihan = sc.nextInt();
            if (pilihan < 1 || pilihan > daftar.size()) pilihan = 1;
        } catch (Exception e) { pilihan = 1; } // --kompleksitas O(1) untuk input user, karena hanya satu input--

        WifiPoint lokasi = daftar.get(pilihan - 1);
        System.out.println("\nLokasi referensi: " + lokasi.namaGedung); 
        //--ada kompleksitas O(1) untuk menampilkan lokasi referensi, karena hanya menampilkan satu lokasi--

        // Dijkstra dari lokasi user
        double[] jarak = graf.dijkstra(lokasi.id, daftar.size()); // --kompleksitas O((V + E) log V) untuk Dijkstra, karena menggunakan priority queue--

        // 5 terdekat berdasarkan shortest path
        System.out.println("\n=== 5 WiFi Terdekat (Dijkstra) dari " + lokasi.namaGedung + " ===");
        ArrayList<int[]> hasil = new ArrayList<>();
        for (WifiPoint wp : daftar) {
            if (wp.id != lokasi.id && jarak[wp.id] != Double.MAX_VALUE)
                hasil.add(new int[]{wp.id, (int) jarak[wp.id]});
        } // --kompleksitas O(n) untuk mengumpulkan hasil, karena harus memeriksa semua titik wifi--
        hasil.sort((a, b) -> a[1] - b[1]);
        int batas = Math.min(5, hasil.size());
        for (int i = 0; i < batas; i++) {
            WifiPoint wp = daftar.get(hasil.get(i)[0] - 1);
            System.out.printf(java.util.Locale.US, "%s - Jarak: %.2f, Kecepatan Internet: %d Mbps%n", wp.namaGedung, jarak[wp.id], wp.kecepatan);
        } // --kompleksitas O(n log n) untuk sorting hasil, karena menggunakan sort pada ArrayList--

        // Rekomendasi terbaik: skor = jarak_dijkstra - (kecepatan / 10.0)
        System.out.println("\n=== Rekomendasi WiFi Terbaik ===");
        WifiPoint terbaik = null;
        double skorTerbaik = Double.MAX_VALUE;
        for (WifiPoint wp : daftar) {
            if (wp.id == lokasi.id || jarak[wp.id] == Double.MAX_VALUE) continue;
            double skor = jarak[wp.id] - (wp.kecepatan / 10.0);
            if (skor < skorTerbaik) { skorTerbaik = skor; terbaik = wp; }
        }
        if (terbaik != null)
            System.out.printf(java.util.Locale.US, "Rekomendasi: %s (Jarak: %.2f, Kecepatan Internet: %d Mbps)%n",
                    terbaik.namaGedung, jarak[terbaik.id], terbaik.kecepatan);
        else
            System.out.println("Tidak ada WiFi terjangkau dari lokasi ini.");

        sc.close();
    } // --kompleksitas O(n) untuk mencari rekomendasi terbaik, karena harus memeriksa semua titik wifi--

    static double hitungJarak(WifiPoint a, WifiPoint b) {
        int dx = a.x - b.x, dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    } // --kompleksitas O(1) untuk menghitung jarak, karena hanya melakukan operasi aritmatika sederhana--

    static ArrayList<WifiPoint> getDaftarWifi() {
        ArrayList<WifiPoint> d = new ArrayList<>();
        d.add(new WifiPoint(1,  "Tower UNS",          "fasilitas", 295, 460, 35));
        d.add(new WifiPoint(2,  "UNS INN",             "fasilitas", 310, 430, 40));
        d.add(new WifiPoint(3,  "LPPMP",               "fasilitas", 250, 380, 30));
        d.add(new WifiPoint(4,  "Menwa",               "fasilitas", 400, 410, 32));
        d.add(new WifiPoint(5,  "SPMB",                "fasilitas", 350, 360, 45));
        d.add(new WifiPoint(6,  "Akademik",            "fasilitas", 420, 365, 38));
        d.add(new WifiPoint(7,  "PPLH",                "fasilitas", 480, 370, 25));
        d.add(new WifiPoint(8,  "Rektorat",            "fasilitas", 380, 320, 42));
        d.add(new WifiPoint(9,  "Auditorium",          "fasilitas", 360, 290, 40));
        d.add(new WifiPoint(10, "FT",                  "fakultas",  250, 330, 35));
        d.add(new WifiPoint(11, "FP",                  "fakultas",  470, 310, 30));
        d.add(new WifiPoint(12, "FIB",                 "fakultas",  320, 295, 37));
        d.add(new WifiPoint(13, "UPT Bahasa",          "fasilitas", 270, 255, 33));
        d.add(new WifiPoint(14, "FSRD",                "fakultas",  330, 250, 36));
        d.add(new WifiPoint(15, "Perpustakaan",        "fasilitas", 400, 240, 45));
        d.add(new WifiPoint(16, "FMIPA",               "fakultas",  460, 270, 28));
        d.add(new WifiPoint(17, "FK",                  "fakultas",  480, 220, 40));
        d.add(new WifiPoint(18, "UPT TIK",             "fasilitas", 370, 200, 50));
        d.add(new WifiPoint(19, "FEB",                 "fakultas",  240, 215, 31));
        d.add(new WifiPoint(20, "FH",                  "fakultas",  280, 190, 34));
        d.add(new WifiPoint(21, "FISIP",               "fakultas",  220, 165, 27));
        d.add(new WifiPoint(22, "Pascasarjana",        "fakultas",  360, 165, 39));
        d.add(new WifiPoint(23, "FKIP",                "fakultas",  330, 110, 43));
        d.add(new WifiPoint(24, "Masjid Nurul Huda",   "fasilitas", 380,  70, 48));
        d.add(new WifiPoint(25, "Student Center",      "fasilitas", 440,  90, 41));
        d.add(new WifiPoint(26, "Biro Kemahasiswaan",  "fasilitas", 480,  65, 37));
        d.add(new WifiPoint(27, "Graha UKM",           "fasilitas", 500,  90, 36));
        d.add(new WifiPoint(28, "GOR UNS",             "fasilitas", 540, 120, 32));
        d.add(new WifiPoint(29, "Stadion UNS",         "fasilitas", 510, 145, 29));
        d.add(new WifiPoint(30, "Javanologi",          "fasilitas", 580, 130, 26));
        return d;
    }
} // --kompleksitas O(n) untuk membuat daftar wifi, karena hanya menambahkan elemen ke ArrayList--

// === Graf dengan Adjacency List ===
class Graf {
    // Adjacency list: tiap node punya ArrayList of Edge
    ArrayList<ArrayList<Edge>> adj;

    Graf(int n) {
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    } // --kompleksitas O(n) untuk membuat adjacency list, karena harus membuat n ArrayList--

    void tambahEdge(int u, int v, double bobot) {
        adj.get(u).add(new Edge(v, bobot));
        adj.get(v).add(new Edge(u, bobot));
    } // --kompleksitas O(1) untuk menambah, karena hanya menambahkan edge ke dua ArrayList--

    // Dijkstra: O((V + E) log V)
    double[] dijkstra(int sumber, int n) {
        double[] dist = new double[n + 1];
        Arrays.fill(dist, Double.MAX_VALUE);
        dist[sumber] = 0;

        // PriorityQueue: {jarak, nodeId}
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0, sumber});

        while (!pq.isEmpty()) {
            double[] curr = pq.poll();
            int u = (int) curr[1];
            if (curr[0] > dist[u]) continue;
            for (Edge e : adj.get(u)) {
                if (dist[u] + e.bobot < dist[e.tujuan]) {
                    dist[e.tujuan] = dist[u] + e.bobot;
                    pq.offer(new double[]{dist[e.tujuan], e.tujuan});
                }
            }
        }
        return dist;
    }// --kompleksitas O((V + E) log V) untuk Dijkstra, karena menggunakan priority queue--
}

class Edge {
    int tujuan;
    double bobot;
    Edge(int tujuan, double bobot) { this.tujuan = tujuan; this.bobot = bobot; }
} // --kompleksitas O(1) untuk membuat edge, karena hanya menyimpan dua nilai--

class WifiPoint {
    int id, x, y, kecepatan;
    String namaGedung, kategori;
    WifiPoint(int id, String namaGedung, String kategori, int x, int y, int kecepatan) {
        this.id = id; this.namaGedung = namaGedung; this.kategori = kategori;
        this.x = x; this.y = y; this.kecepatan = kecepatan;
    } // --kompleksitas O(1) untuk membuat WifiPoint, karena hanya menyimpan beberapa nilai--
}
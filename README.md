# TUGAS BESAR STIMA 1 KELOMPOK GREEDLAND

## The Author
| NO | NAMA | NIM |
--- | --- | --- |
| 1 | Yobel Dean Christoper | 13521067 |
| 2 | Maggie Zeta Rosida S| 13521117 |
| 3 | Jeremya Dharmawan Raharjo | 13521131 |

## Overview
Tugas Besar ini Merupakan Implementasi Algoritma Greedy yang Kami pelajari di kelas untuk menyelesaikan persoalan, yaitu permainan Galaxio.


## Cara Penggunaan
- **Install Prerequisites: Stater Pack Game Galaxio: https://github.com/EntelectChallenge/2021-Galaxio/releases/tag/2021.3.2 , Java(Minimal Versi 11), Intellij IDEA, NetBeans, .Net Core 3.1, dan Maven**
- Pastikan kalian sudah menginstall Visualizer Game Galaxio juga
- Klon repository ini ke komputer kalian
- Pada Folder stater-pack(berdasarkan Stater Pack Game yang kalian download), kalian dapat meletakkan file "run.bat" di folder tersebut. Jangan lupa modifikasi file sesuai kebutuhan serta file yang berkaitan (file JSON ”appsettings.json” dalam folder “runner-publish” dan “engine-publish” ; modifikasi diperlukan untuk menentukan jumlah bot dalam permainan atau menggunakan bot modifikasi yang telah kami buat).
- Apabila ingin menggunakan bot modifikasi yang telah kami buat, silakan menggunakan file .jar yang ada di folder target/[namabot].jar
- Contoh konfigurasi file "run.bat" dapat dilihat di tautan berikut: https://docs.google.com/document/d/1Ym2KomFPLIG_KAbm3A0bnhw4_XQAsOKzpTa70IgnLNU/edit
- Apabila ingin memodifikasi file source code kami dan mengkompilasi menjadi file .jar baru untuk membuat bot modifikasi yang baru, gunakan terminal dan ketik perintah "mvn clean package"
- Jalankan game melalui file "run.bat" yang telah dimodifikasi. Enjoy the Galaxio! :)

## Tentang Gim dan Algoritmanya
Algoritma greedy merupakan strategi dimana memecahkan permasalahan optimasi, dengan menggunakan teknik optimasi yang sesuai untuk mencapai optimum global melalui solusi optimum lokal(diharapkan konvergen menuju optimum global). Kami menggunakan algoritma greedy berdasarkan teknik heruistik sehingga melahirkan beberapa kandidat yang akan bisa diimplementasikan pada gim ini.

Galaxio merupakan gim bertahan, menggunakan bot untuk mencari makanan dan menaklukan musuh yang lebih kecil. Kami memutuskan untuk menggunakan 3 pendekatan heuistik greedy sebagai berikut:
1. _Defensive Greedy_
2. _Offensive Greedy_
3. _Greedy by Growth Rate_

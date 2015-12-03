# Tugas 2 IF3110 Pengembangan Aplikasi Berbasis Web

Membuat website tanya jawab seperti Stack Exchange dengan REST dan SOAP dan arsitektur berorientasi servis.

### Tujuan Pembuatan Tugas

Diharapkan dengan tugas ini anda dapat mengerti:
* Produce dan Consume REST API
* Mengimplementasikan oauth sederhana
* Produce dan Consume Web Services dengan protokol SOAP
* Membuat web application dengan frontend menggunakan JSP yang akan memanggil web services dengan SOAP dan REST.

### Petunjuk Pengerjaan

1. Buatlah organisasi pada github dengan nama sesuai dengan nama tim yang anda daftarkan.
2. Tambahkan anggota tim pada organisasi anda.
3. Fork pada repository ini dengan organisasi yang telah dibuat.
4. Silakan commit pada repository anda (hasil fork). Lakukan berberapa commit dengan pesan yang bermakna, contoh: `fix css`, `create post done`, jangan seperti `final`, `benerin dikit`. Disarankan untuk tidak melakukan commit dengan perubahan yang besar karena akan mempengaruhi penilaian (contoh: hanya melakukan satu commit kemudian dikumpulkan). **Commit dari setiap anggota tim akan mempengaruhi penilaian.** Jadi, setiap anggota tim harus melakukan commit yang berpengaruh terhadap proses pembuatan aplikasi.
5. Ganti bagian **link laporan** dengan link laporan anda (misal dropbox) dalam format PDF yang terdiri dari:
  - Perbedaan SOAP dan REST, serta kelebihan dan kekurangan dari SOAP dan REST.
  - Konsep melakukan sharing session dengan menggunakan REST.
  - Kelebihan dan kelemahan dari arsitektur aplikasi tugas ini, dibandingkan dengan aplikasi monolitik (login, CRUD DB, dll jadi dalam satu aplikasi)
6. Pull request dari repository anda ke repository ini dengan format **Nama kelompok** - **NIM terkecil** - **Nama Lengkap dengan NIM terkecil** sebelum **Jumat, 20 November 2015 23.59**.

### Link Laporan

https://drive.google.com/a/std.stei.itb.ac.id/file/d/0B66-YXA_-IIqZmd5TlBsZ0s2ZWs/view?usp=sharing

### Arsitektur Umum Server

```
*----------------------------------------------------*
|Database                                            |
|(MYSQL, PGSQL, dll)                                 |
*----------------------------------------------------*
        |                               |
     Adapter                          Adapter    
        |                               |
*----------------*           *------------------------*
|Identity Service|   consume |Stackexchange Webservice|
|Java Servlet    |<----------|JAX-WS (atau Java SOAP WebService lain)
|REST API        |           |SOAP                    |
*----------------*           *------------------------*
            ^                     ^
             \ consume           / consume
              \                 /
              *---------------*
              |Frontend WebApp|
              |     JSP       |
              *---------------*
                     |
                   connect
                     |
              *----------------------* 
              |Web Browser           |
              |(chrome, firefox, dll)|
              *----------------------*
```
Tugas 2 ini terdiri dari berberapa komponen yang harus dibuat:
* Front-end: digunakan untuk menangani HTTP request dari web browser dan menghasilkan HTTP response. Front end ini wajib dibuat dengan **Java+Java Server Pages**
* Stackexchange Webservice: digunakan untuk interface yang dipanggil oleh front-end dan melakukan query ke database, operasi insert, dan operasi update untuk entitas User, Question, dan Answer. Webservice ini akan dipanggil oleh frontend menggunakan SOAP. Webservice ini wajib dibuat dengan **JAX-WS dengan protokol SOAP atau Webservice lain** yang basisnya menggunakan **SOAP dan Java**.
* Identity service: digunakan untuk menerima email (sebagai username) dan password pengguna dan menghasilkan access token. Server ini akan dipanggil oleh frontend atau stackexchange webservice menggunakan REST. Identity service ini wajib dibuat dengan menggunakan **Java Servlet**
* Perhatikan bahwa anda tidak perlu menggunakan banyak mesin untuk membuat aplikasi ini. Contohnya, pada satu mesin anda bisa menggunakan port 8000 untuk JSP, port 8001 untuk identity service, dan port 8002 untuk stackexchange web service.

### Deskripsi Tugas

Anda diminta untuk membuat stackexchange seperti tugas 1.  Kebutuhan fungsional tugas yang harus dibuat adalah sebagai berikut.

1. Registrasi pengguna yang minimal terdiri dari nama, email, dan password. Anda harus melakukan validasi bahwa email yang sama tidak boleh digunakan untuk dua kali mendaftar.
2. Login pengguna.
3. List pertanyaan, seperti tugas 1.
4. Bertanya, seperti tugas 1. Pengguna harus login terlebih dahulu untuk bertanya. 
5. Ubah pertanyaan, seperti tugas 1. Ubah pertanyaan hanya dapat dilakukan oleh si pembuat pertanyaan tersebut.
6. Hapus pertanyaan, seperti tugas 1. Hapus pertanyaan hanya dapat dilakukan oleh si pembuat pertanyaan tersebut.
7. Lihat pertanyaan.
8. Menjawab pertanyaan, seperti tugas 1. Pengguna harus login terlebih dahulu.
9. Vote up/down pada suatu pertanyaan seperti tugas 1. Satu pengguna hanya dapat melakukan vote pada suatu pertanyaan maksimal satu kali, dan hanya bisa vote up XOR vote down. Vote tidak perlu menggunakan AJAX.
10. Tampilan dibebaskan. Tampilan **tidak akan dinilai**. Boleh menggunakan asset dan tampilan dari tugas sebelumnya. Boleh menggunakan CSS Framework seperti Bootstrap atau javascript library seperti jquery.
11. Tidak perlu validasi javascript
12. Tidak perlu memperhatikan aspek keamanan dan etika penyimpanan data.


### Skenario 

Skenario ini adalah kebutuhan non fungsional yang harus dipenuhi.

##### Registrasi
1. Pengguna mengakses halaman registrasi, contoh: `/register.jsp`
2. Pengguna mengisi form.
3. JSP akan memanggil fungsi Stackexchange web service dengan SOAP, misal `register(name,email,password);` Contohnya dapat dilihat pada
[link berikut](http://www.mkyong.com/webservices/jax-ws/jax-ws-hello-world-example/)
Perhatikan pemanggilan pada contoh ini seperti melakukan remote procedure call.
4. Web service akan memberikan pesan sukses atau error.

#### Login
1. Pengguna mengakses halaman login, contoh: `/login.jsp` dan mengisi form.
2. JSP akan membuka HTTP Request Connection ke Identity Service, contoh `POST /login` dengan body data email dan password.
3. Identity service akan query DB apakah email dan password valid.
4. Identity service akan memberikan HTTP Response `access token` dan `lifetime` jika email dan password ada di dalam DB, atau error jika tidak ditemukan data.
5. Token ini digunakan sebagai representasi state dari session pengguna dan harus dikirimkan ketika pengguna ingin melakukan Create, Update, Delete Question, Vote, dan Answer.
6. Silakan definsikan format request dan response sesuai kebutuhan anda. Anda dapat menggunakan JSON atau XML untuk REST.

#### Create Question, Update Question dll
1. Pengguna mengakses halaman create post, misal `/create.jsp` dan mengisi form.
2. JSP akan memanggil fungsi Stackexchange ws dengan SOAP, misalnya `createQuestion(access_token,title,content)`
3. Fungsi `createQuestion` pada Stackexchange ws akan melakukan HTTP Request connection ke Identity Service, siapakah pemilik `access_token` yang diberikan.
- Jika `access_token` yang diberikan sudah kadaluarsa, maka `createQuestion` akan memberikan response expired token.
- Jika `access_token` tidak valid, maka `createQuestion` akan memberikan response error ke JSP.
- Jika valid, maka `createQuestion` akan melakukan insert ke DB, dan memberikan response ok ke JSP.
4. Untuk update question, delete question, answer question, dan vote up/down kira-kira mekanismenya hampir sama dengan create question.
5. Silakan definisikan format object request dan response sesuai kebutuhan anda.

#### Prosedur Demo
Sebelum demo, asisten akan melakukan checkout ke hash commit terakhir yang dilakukan sebelum deadline. Hal ini digunakan untuk memastikan kode yang akan didemokan adalah kode yang terakhir disubmit sebelum deadline.



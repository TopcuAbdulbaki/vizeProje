package mailapi;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //Ana menü işlemleri kullanıcıya verildi
        System.out.println("""
                Ana menuye hosgeldiniz.
                Lutfen yapmak istediginiz islemi seciniz.
                1. Elit Uye Ekleme
                2. Genel Uye Ekleme
                3. Mail Gonderme
                """);
        //Ana menü işlem seçimi kullanıcı tarafından numara girilerek yapılır
        Scanner islemSecimi = new Scanner(System.in);
        int islem = islemSecimi.nextInt();
        //Yapılacak işlemler Switch-case yapısından yararlanarak seçmeli olarak gerçekleştirilir
        FileReader DosyaOkuyucu = new FileReader("Kullanıcılar.txt");
        BufferedReader tamponOkuyucu = new BufferedReader(DosyaOkuyucu);
        String bilgi = null;
        File dosya = new File("Kullanıcılar.txt");
        FileWriter yazici = new FileWriter(dosya, true);
        if (!dosya.exists()) {
            dosya.createNewFile();
        }
        int boyut = 100;
        String[] Mailler = new String[boyut];
        int n = 0;
        boolean elitUyeleriGectik = false;
        boolean genelUyeleriGectik = false;
        switch (islem) {
            //kullanıcı bilgileri alınıyor
            case 1 -> {System.out.println("Elit Uye Ekleme");
                Scanner kayit = new Scanner(System.in);
                System.out.print("isim : ");
                String isim = kayit.nextLine();
                System.out.print("soyisim : ");
                String soyisim = kayit.nextLine();
                System.out.print("email : ");
                String email = kayit.nextLine();
                try {
                    // Dosya adları ve yolları belirledik
                    String dosyaAdi = "Kullanıcılar.txt";
                    String geciciDosyaAdi = "geciciDosya.txt";

                    // Dosyaları açtık
                    BufferedReader buffOkuyucu = new BufferedReader(new FileReader(dosyaAdi));
                    BufferedWriter bwriter = new BufferedWriter(new FileWriter(geciciDosyaAdi));

                    // Satırları okuduk ve gerekli düzenlemeleri yaptık
                    String satir;
                    boolean elitUyelerBulundu = false;
                    while ((satir = buffOkuyucu.readLine()) != null) {
                        if (satir.trim().equals("ELİT UYELER")) {
                            bwriter.write(satir + "\n");
                            bwriter.write(isim + "   " + soyisim + "   " + email + "\n");
                            elitUyelerBulundu = true;
                        } else {
                            bwriter.write(satir + "\n");
                        }
                    }

                    // Dosyaları kapattık ve geçici dosyayı orijinal dosyanın üzerine yazdık
                    buffOkuyucu.close();
                    bwriter.close();
                    File orijinalDosya = new File(dosyaAdi);
                    orijinalDosya.delete();
                    File geciciDosya = new File(geciciDosyaAdi);
                    geciciDosya.renameTo(orijinalDosya);

                    System.out.println("Ekleme işlemi tamamlandı.");
                } catch (IOException e) {
                    System.out.println("Dosya okuma/yazma hatası: " + e.getMessage());
                }
            }


            case 2 -> {
                System.out.println("Genel Uye Ekleme");
                Scanner kayit = new Scanner(System.in);
                System.out.print("isim : ");
                String isim = kayit.nextLine();
                System.out.print("soyisim : ");
                String soyisim = kayit.nextLine();
                System.out.print("email : ");
                String email = kayit.nextLine();
                System.out.println(isim + "    " + soyisim + "    " + email);
                BufferedWriter geciciBellek = new BufferedWriter(yazici);
                geciciBellek.write(isim + "   " + soyisim + "   " + email + "\n");
                geciciBellek.close();
            }
            case 3 -> {
                System.out.println("Mail Gonderme");
                //Mail menüsü
                System.out.println("""
                        Lutfen Mail Gonderilecekleri Seciniz
                        1. Elit Uyeler
                        2. Genel Uyeler
                        3. Tum Uyeler
                        """);
                //Mail menüsü işlem seçimi
                Scanner MailSecimi = new Scanner(System.in);
                int mail = MailSecimi.nextInt();

                switch (mail) {

                    case 1 -> {

                        while ((bilgi = tamponOkuyucu.readLine()) != null) {
                            if (!elitUyeleriGectik && bilgi.contains("ELİT UYELER")) {
                                // ELİT UYELER kısmını geçtik
                                elitUyeleriGectik = true;
                            } else if (elitUyeleriGectik && bilgi.contains("GENEL UYELER")) {
                                // GENEL UYELER kısmına geldik, döngüden çıkabiliriz
                                break;
                            } else if (elitUyeleriGectik) {
                                // ELİT UYELER kısmını geçtikten sonra gelen satırlarda e-postaları yazdır
                                String[] parcalar = bilgi.split(" ");
                                String email = parcalar[2];
                                System.out.println(email);
                                Mailler[n]=email;
                                System.out.println(Mailler[n]);
                                n++;
                                EmailSender emailSender= new EmailSender();
                                emailSender.main(Mailler);

                            }
                        }
                    }

                    case 2 -> {

                        while ((bilgi = tamponOkuyucu.readLine()) != null) {
                            if (!genelUyeleriGectik && bilgi.contains("GENEL UYELER")) {
                                // GENEL UYELER kısmını geçtik
                                genelUyeleriGectik = true;
                            } else if (genelUyeleriGectik) {
                                // GENEL UYELER kısmını geçtikten sonra gelen satırlarda e-postaları yazdır
                                String[] parcalar = bilgi.split(" ");
                                String email = parcalar[2];
                                System.out.println(email);
                                Mailler[n]=email;
                                System.out.println(Mailler[n]);
                                n++;
                                EmailSender emailSender= new EmailSender();
                                emailSender.main(Mailler);
                            }
                        }
                    }
                    case 3 -> {


                        elitUyeleriGectik = false;
                        genelUyeleriGectik = false;

                        while ((bilgi = tamponOkuyucu.readLine()) != null) {
                            if (!elitUyeleriGectik && bilgi.contains("ELİT UYELER")) {
                                // ELİT UYELER kısmını geçtik
                                elitUyeleriGectik = true;
                            } else if (!genelUyeleriGectik && bilgi.contains("GENEL UYELER")) {
                                // GENEL UYELER kısmını geçtik
                                genelUyeleriGectik = true;
                            } else if (elitUyeleriGectik && !genelUyeleriGectik) {
                                // ELİT UYELER kısmını geçtikten sonra gelen satırlarda e-postaları yazdırır
                                String[] parcalar = bilgi.split(" ");
                                String email = parcalar[2];
                                Mailler[n]=email;
                                System.out.println(Mailler[n]);
                                n++;
                                //System.out.println(email);
                            } else if (elitUyeleriGectik && genelUyeleriGectik) {
                                // GENEL UYELER kısmını geçtikten sonra gelen satırlarda e-postaları yazdırır
                                String[] parcalar = bilgi.split(" ");
                                String email = parcalar[2];
                                Mailler[n]=email;
                                System.out.println(Mailler[n]);
                                n++;
                               System.out.println(email);
                               EmailSender emailSender= new EmailSender();
                               emailSender.main(Mailler);

                            } else {
                                System.out.println("yok");
                            }
                        }
                    }
                    default -> System.out.println("Gecerli bir secim yapmadiniz.");
                }
            }
        }
    }
}
class EmailSender {


    public static void main(String [] alıcılar) {
        // SMTP ayarları
        String host = "smtp.gmail.com";
        String kullaniciAdi = "abdlbktpc7@gmail.com";
        String sifre = "gqtcpwotfutfyssp";
        int port = 587;

        // Gönderici bilgileri
        String gonderenAdi = "abdulbaki";
        String gonderenEmail = "abdlbktpc7@gmail.com";

        // Alıcılar
        String alicilar = "adam43adam83@gmail.com\n" + "Abdulbaki Topçu abdlbktpc7@gmail.com";

        // Mesaj içeriği
        String konu = "gelirse şükredin";
        String icerik = "Alllllllaaaaaaahhhh";

        // Ayarları yapılandırma
        Properties ozellikler = new Properties();
        ozellikler.put("mail.smtp.auth", "true");
        ozellikler.put("mail.smtp.starttls.enable", "true");
        ozellikler.put("mail.smtp.host", host);
        ozellikler.put("mail.smtp.port", port);

        // Oturum oluşturma ve kimlik doğrulama
        Session oturum = Session.getInstance(ozellikler, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(kullaniciAdi, sifre);
            }
        });

        try {
            // Mesaj oluşturma
            MimeMessage mesaj = new MimeMessage(oturum);
            mesaj.setFrom(new InternetAddress(gonderenEmail, gonderenAdi));
            for (String alici : alıcılar) {
                mesaj.addRecipient(Message.RecipientType.TO, new InternetAddress(alici));
            }
            mesaj.setSubject(konu);
            mesaj.setText(icerik);

            // Mesajı gönderme
            Transport.send(mesaj);
            System.out.println("Mesaj gönderildi.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println("Mesaj gönderilemedi. Hata: " + e.getMessage());
        }
    }
}


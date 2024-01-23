UZDIZ Zadaca u kojoj su obrađeni GOF uzorci dizajna
U datotekama vozila, mjesta, osoba, paketa nalaze se podaci koje program koristi za izvršavanje.

Naredbe su:
VR n - pomak vremena za n sati
IP - prikaz paketa
SV - prikaz svih vozila 
VV {registracija} - prikaz vožnji vozila s pojedinom registracijom - npr. VV VŽ100PK
VS {registracija} n - prikaz segmenata n vožnje određene registracije - npr. VS VŽ100PK 1
PP - prikaz composite strukture za područja, gradove, ulice

ADMIN {Ime i prezime} - npr. ADMIN Pero Kos - pretvara korisnka Pero Kos u admina, a to je priprema za proxy.
PROXY {Ime i prezime} - npr. PROXY Pero Kos - pretplacuje korisnika kao Observera svim paketima
UNPROXY {Ime i prezime} - npr. korisnik Pero Kos - vise ne prima obavijesti za nijedan paket

PO 'primatelja/pošiljatelja' paket [D | N]
  ● D – šalju se obavijesti
  ● N – ne šalju se obavijesti
  Npr. PO 'Pero Kos' N CROVŽ0001 - N - korisnik Pero Kos više ne prima obavijesti za pakete
  
PS
Sintaksa:
■ PS vozilo [A | NI | NA]
  ● A – aktivno
  ● NI – nije ispravno
  ● NA nije aktivno
  Npr.  PS VŽ100PK NI - Vozilu VŽ100PK postavlja se status da nije ispravno.

SPV '{naziv}' - spremi se trenutno stanje sustava pod zeljenim nazivom
npr. SPV '1' - sprema stanje sustava pod naziv 1

PPV '{naziv}' - vraca stanje sustava na spremljeno stanje pod navedenim nazivom
npr. PPV '1' - vraca stanje sustava na spremljeno stanje pod nazivom 1

Q - izlaz iz programa

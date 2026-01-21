import java.util.ArrayList; 
import java.util.Collections;
import java.util.Scanner; 

public class BlackJack {
    // VARIABLES GLOBALS COMPARTIDES
    static Scanner sc = new Scanner(System.in);
    static int diners = 100;
    static boolean tincPistola = false;
    static int bales = 0;
    static String[] nomsProductes = {"Pistola", "Bala", "Cervesa"};
    static int[] preusProductes = {50, 10, 5};

    // MÈTODES PRINCIPALS
    public static void main(String[] args) {
        boolean sortir = false;
        do {
            System.out.println("_______________________\n");
            System.out.println("DINERS: " + diners + " | BALES: " + bales);
            System.out.println("_______________________");
            System.out.println("\n1. Jugar \n2. Botiga \n3. Sortir");
            int op = llegirNumero("\nOpció: ", 1, 3);
            
            if (op == 1) {
                if (diners > 0) {
                    jugarPartida();
                } else {
                    System.out.println("No tens diners ni per apostar la pols que portes a la butxaca. Dispara o marxa.");
                }
            }
            else if (op == 2) entrarTenda();
            else sortir = true;

            if (diners <= 0 && !sortir) {
                if (tincPistola && bales > 0) {
                    if (llegirNumero("Disparar? (1.Si 2.No): ", 1, 2) == 1) dispararCrupier();
                    else sortir = true;
                } else sortir = true;
            }
        } while (!sortir);
    }

    // MÈTODE PER ASSEGURAR QUE EL USUARI INTRODUEIX UN NÚMERO DINS DEL RANG PERMES
    public static int llegirNumero(String text, int minim, int maxim) {
        int numero = -1;
        // El bucle es repeteix si el número està fora de rang
        while (numero < minim || numero > maxim) {
            System.out.print(text);
            if (sc.hasNextInt()) {
                numero = sc.nextInt();
                if (numero < minim || numero > maxim) System.out.println("Error: Fora de rang.");
            } else {
                System.out.println("Error: Escriu un número.");
                sc.next();
            }
        }
        return numero;
    }

    // MÈTODE PER ENTRAR A LA BOTIGA
    public static void entrarTenda() {
        System.out.println("__________");
        System.out.println("\n BOTIGA");
        System.out.println("__________\n");

        // Utilitzem bucle per mostrar productes i preus respectius
        for (int i = 0; i < nomsProductes.length; i++) {
            System.out.println((i + 1) + ". " + nomsProductes[i] + " - $" + preusProductes[i]);
        }
        
        System.out.println("4. Tornar");
        int opcio = llegirNumero("\nComprar: ", 1, 4);
        if (opcio != 4) {
            int pos = opcio - 1;
            if (diners >= preusProductes[pos]) {
                diners -= preusProductes[pos];
                if (pos == 0) tincPistola = true;
                if (pos == 1) bales++;
                if (pos == 2) System.out.println("Glup glup... m'emborratxaré al final!"); // Missatge Cervesa
                
                // Si no és la cervesa, només diem 'Comprat!'
                if (pos != 2) {
                    System.out.println("Comprat!");
                }
            } else {
                System.out.println("No tens diners.");
            }
        }
    }

    // MÈTODE PER JUGAR UNA PARTIDA DE BLACKJACK
    public static void jugarPartida() {
        int aposta = llegirNumero("Aposta: ", 1, diners);
        ArrayList<String> baralla = crearBaralla();
        
        ArrayList<String> maJugador = new ArrayList<>(); 
        ArrayList<String> maCrupier = new ArrayList<>(); 
        
        // Repartiment inicial (dos a cada un)
        maJugador.add(baralla.remove(0)); 
        maCrupier.add(baralla.remove(0));
        maJugador.add(baralla.remove(0));
        maCrupier.add(baralla.remove(0));

        boolean plantat = false;
        
        // Torn del Jugador
        while (!plantat && calcularPunts(maJugador) <= 21) {
            System.out.println("\nLa teva mà: " + maJugador + " (" + calcularPunts(maJugador) + ")");
            System.out.println("Carta Crupier visible: " + maCrupier.get(0));
            
            if (llegirNumero("\n1. Demanar | 2. Plantar-se: ", 1, 2) == 1) {
                maJugador.add(baralla.remove(0));
            } else {
                plantat = true;
            }
        }
        
        int puntsJugador = calcularPunts(maJugador);
        int puntsCrupier = calcularPunts(maCrupier);

        System.out.println("_____________");
        System.out.println("\n RESULTATS");
        System.out.println("_____________\n");
        
        if (puntsJugador > 21) {
            System.out.println("T'has passat. Has perdut $" + aposta);
            diners -= aposta;
        } else {
            // Torn del Crupier
            while (puntsCrupier < 17) {
                maCrupier.add(baralla.remove(0));
                puntsCrupier = calcularPunts(maCrupier);
            }

            System.out.println("Mà Jugador: " + maJugador + " (" + puntsJugador + ")");
            System.out.println("Mà Crupier: " + maCrupier + " (" + puntsCrupier + ")");

            if (puntsCrupier > 21) {
                System.out.println("El Crupier s'ha passat! GUANYES.");
                diners += aposta;
            } else if (puntsJugador > puntsCrupier) {
                System.out.println("\nGUANYES!");
                diners += aposta;
            } else if (puntsJugador < puntsCrupier) {
                System.out.println("El Crupier guanya.");
                diners -= aposta;
            } else { 
                System.out.println("Empat.");
            }
        }
    }

    // MÈTODE PER CREAR I BARALLAR LA BARAJA(52 CARTES)
    public static ArrayList<String> crearBaralla() {
        ArrayList<String> novaBaralla = new ArrayList<>();
        char[] pals = {"♥", "♦", "♣", "♠"};
        String[] valors = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

        for (String pal : pals) {
            for (String valor : valors) {
                novaBaralla.add(valor + pal);
            }
        }

        Collections.shuffle(novaBaralla);
        return novaBaralla;
    }

    // MÈTODE PER CALCULAR ELS PUNTS D'UNA MÀ
    public static int calcularPunts(ArrayList<String> ma) {
        int total = 0; int asos = 0;

        for (String carta : ma) {
            if (carta.startsWith("A")) { 
                total += 11; 
                asos++; 
            } else if (carta.startsWith("K") || carta.startsWith("Q") || carta.startsWith("J") || carta.startsWith("10")) {
                total += 10;
            } else {
                total += Character.getNumericValue(carta.charAt(0));
            }
        }

        // Si el total supera 21 i hi ha Asos, els convertim de 11 a 1
        while (total > 21 && asos > 0) { 
            total -= 10; asos--; 
        }
        return total;
    }

    // MÈTODE PER DISPARAR AL CRUPIER
    public static void dispararCrupier() {
        bales--;
        if (Math.random() > 0.5) {
            System.out.println("Headshoot. Emportat els diners ràpid!");
            diners = 200;
        } else {
            System.out.println("Has fallat la bala! Cuidado amb els guardaespatlles!");
            diners = -1;
        }
    }
}
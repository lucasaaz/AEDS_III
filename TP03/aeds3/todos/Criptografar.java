package todos;

 public class Criptografar {

    // ---------- criptografar ----------
    public static String cifrar(String antiga, String chave) {
        String nova = "";

        for (int i = 0; i < antiga.length(); i++) {
            int a = antiga.charAt(i);
            int c = chave.charAt(i);
            int n = a + c;

            if (antiga.charAt(i) >= 48 && antiga.charAt(i) <= 57) { // numeros
                if (n <= 10) {
                    nova += (char) n;
                } else {
                    nova += (char) n % 10;
                }
            } else if (antiga.charAt(i) >= 65 && antiga.charAt(i) <= 90) { // letras maiusculas
                if (n <= 26) {
                    nova += (char) n;
                } else {
                    nova += (char) ((n % 26) + 65);
                }
            } else if (antiga.charAt(i) >= 97 && antiga.charAt(i) <= 122) { // letras minusculas
                if (n <= 26) {
                    nova += (char) n;
                } else {
                    nova += (char) ((n % 26) + 97);
                }
            } else { // outros caracteres
                if (n <= 10) {
                    nova += (char) n;
                } else {
                    nova += (char) n % 10;
                }
            }

            System.out.println(a + " " + c + " = " + nova.charAt(i));

        }

        return nova;
    }
    // ----------------------------------

    // ---------- descriptografar ----------
    public static String decifrar(String nova, String chave) {
        String antiga = "";

        for (int i = 0; i < nova.length(); i++) {
            int n = nova.charAt(i);
            int c = chave.charAt(i);
            int a = n - c;

            if (a <= 255) {
                antiga += (char) a;
            } else {
                antiga += (char) a % 255;
            }
        }

        return antiga;
    }
    // ----------------------------------
}
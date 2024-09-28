import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Esta clase representa un juego de preguntas y respuestas sobre leyes absurdas en diferentes partes del mundo.
 */
public class LeyesAbsurdasQuiz {

    // Contantes para los colores en la consola
    public static final String verde="\u001B[32m";
    public static final String rojo="\u001B[31m";
    public static final String azul="\u001B[34m";
    public static final String reset="\u001B[0m";


    /**
     * Punto de entrada para el programa. Inicia el juego y muestra el resultado.
     * @param args Los argumentos de la línea de comandos (no se utilizan en este programa).
     */
    public static void main(String[] args) {

        String[] preguntas;
        String[] respuestas;
        int respuestasCorrectas = 0;
        int[] numeroPreguntas;
        double totalPreguntas;
        double porcentaje;
        int[] preguntasImprimidas = new int[20];
        Arrays.fill(preguntasImprimidas, 0, 20, -1);
        boolean exit;

        exit = false;
        while(!exit){
            int opcion = menuQuiz();
            switch (opcion) {
                case 1:
                    preguntas = arrayEnunciados();
                    respuestas = arrayRespuestas();
                    String nombreUser = presentacion();
                    numeroPreguntas = numeroPreguntas();
                    totalPreguntas = numeroPreguntas[0];
                    Arrays.fill(preguntasImprimidas, 0, 20, -1);
                    boolean jugar = true;
                    while (jugar) {
                         boolean resultado = iniciarRonda(numeroPreguntas, preguntas, preguntasImprimidas, respuestas);
                        if (numeroPreguntas[0] == 0) {
                            jugar = false;
                        }
                        if (resultado) {
                            respuestasCorrectas++;
                        }
                    }
                    porcentaje = calculoPorcentaje(respuestasCorrectas, totalPreguntas);
                    finalizacion(respuestasCorrectas, porcentaje);

                    int respuestasIncorrectas = (int) (totalPreguntas - respuestasCorrectas);
                    guardarEstadisticas(nombreUser, respuestasCorrectas, respuestasIncorrectas);
                    break;

                case 0:
                    System.out.println("Grácias por jugar, Bye Bye");
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
    }

    /**
     * Muestra un menú de opciones al usuario y solicita que elija una de ellas.
     * @return La opción seleccionada por el usuario.
     */
    private static int menuQuiz (){
        int opcion;
        System.out.println("¿Que quieres hacer?");
        System.out.println("1. Jugar");
        System.out.println("0. Salir");

        opcion = Teclat.llegirInt();
        return opcion;
    }

    /**
     * Genera un array con los enunciados de las preguntas cargados desde un archivo de texto.
     * @return Un array de Strings con los enunciados de las preguntas.
     */
    private static String[] arrayEnunciados() {
        List<String> preguntasList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/preguntas.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                preguntasList.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de preguntas: " + e.getMessage());
        }
        return preguntasList.toArray(new String[0]);
    }

    /**
     * Genera un array con las respuestas correctas a las preguntas cargadas desde un archivo de texto.
     * @return Un array de Strings con las respuestas correctas.
     */
    private static String[] arrayRespuestas() {
        List<String> respuestasList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/respuestas.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                respuestasList.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de respuestas: " + e.getMessage());
        }
        return respuestasList.toArray(new String[0]);
    }

    /**
     * Muestra un mensaje de presentación al inicio del juego.
     */
    private static String presentacion() {
        String nombreUser;

        System.out.println(azul+"\n.:·º:·..LEYES ABSURDAS QUIZ..·:º·:."+reset);
        System.out.println("\nBienvenido al mejor Quiz sobre leyes absurdas alrededor del mundo");
        System.out.print("\nIntroduce tu nombre: ");
        nombreUser = Teclat.llegirString();
        System.out.println("¡Muy bien " + nombreUser + " a jugar!");

        return nombreUser;
    }

    /**
     * Solicita al usuario la cantidad de preguntas que desea jugar.
     * @return Un array de enteros con la cantidad de preguntas elegidas por el usuario.
     */
    private static int[] numeroPreguntas() {
        int[] numeroDePreguntas = new int[1];
        boolean correcto;

        correcto = false;
        do {
            System.out.println("\n¿Cuantas preguntas quieres jugar?\t (mínimo 5, máximo 20)");
            numeroDePreguntas[0] = Teclat.llegirInt();
            if (numeroDePreguntas[0] >= 5 && numeroDePreguntas[0] <= 20) {
                System.out.println("¡EMPEZAMOS!");
                correcto = true;
            } else {
                System.out.println("Lo siento, este numero de preguntas no está disponible");
            }
        } while (!correcto);

        return numeroDePreguntas;
    }

    /**
     * Inicia una ronda de juego, seleccionando y mostrando una pregunta aleatoria al usuario.
     * @param numeroPreguntas Un array de enteros que indica el número de preguntas restantes.
     * @param preguntas Un array de Strings con los enunciados de las preguntas.
     * @param preguntasImprimidas Un array de enteros que registra las preguntas ya impresas.
     * @param respuestas Un array de Strings con las respuestas correctas.
     * @return true si la respuesta del usuario es correcta, false si es incorrecta.
     */
    private static boolean iniciarRonda(int[] numeroPreguntas, String[] preguntas, int[] preguntasImprimidas, String[] respuestas) {
        boolean validarPregunta;
        boolean resultado=false;
        String respuesta;
        int preguntaRandom;
        boolean valido=false;

            do {
                preguntaRandom = new Random().nextInt(preguntas.length);
                validarPregunta = checkPregunta(preguntaRandom, preguntasImprimidas);
            } while (!validarPregunta);
            System.out.println(preguntas[preguntaRandom]);
            numeroPreguntas[0]--;

            // Solicita la respuesta del usuario y válida que sea "Sí" o "No"
            System.out.println("Di tu respuesta (SI/NO)");
            do{
                respuesta = Teclat.llegirString();
                if (respuesta.equalsIgnoreCase("Si")||respuesta.equalsIgnoreCase("No")){
                    valido=true;
                } else{
                    System.out.println("Recuerda que solamente puedes responder: SI/NO");
                }
            }while (!valido);

        // Comprueba si la respuesta del usuario es correcta y actualiza el resultado
            if(respuesta.equalsIgnoreCase(respuestas[preguntaRandom])){
                resultado = true;
            }

        // Imprime un mensaje de acierto o fallo dependiendo del resultado
            if (resultado){
                System.out.println(verde+"Has acertado \uD83E\uDD73"+reset);
            }else{
                System.out.println(rojo+"oh, has fallado \uD83D\uDE2D"+reset);
            }
            return resultado;
    }

    /**
     * Verifica si una pregunta ya ha sido impresa en una ronda anterior.
     * @param preguntaRandom El índice de la pregunta seleccionada aleatoriamente.
     * @param preguntasImprimidas Un array de enteros que registra las preguntas ya impresas.
     * @return true si la pregunta no ha sido impresa anteriormente, false si ya ha sido impresa.
     */
    private static boolean checkPregunta(int preguntaRandom, int[] preguntasImprimidas) {
        boolean valido = true;
        for (int i = 0; i < preguntasImprimidas.length; i++) {
            if (preguntasImprimidas[i] == preguntaRandom) {
                valido = false;
            }
        }
        if (valido) {
            preguntasImprimidas[preguntaRandom] = preguntaRandom;
        }
        return valido;
    }

    /**
     * Calcula el porcentaje de respuestas correctas.
     * @param respuestasCorrectas La cantidad de respuestas correctas.
     * @param totalPreguntas El número total de preguntas jugadas.
     * @return El porcentaje de respuestas correctas.
     */
    private static double calculoPorcentaje(int respuestasCorrectas, double totalPreguntas) {
        double porcentaje = respuestasCorrectas/totalPreguntas * 100;
        return porcentaje ;
    }

    /**
     * Muestra un mensaje de finalización del juego junto con el porcentaje de respuestas correctas.
     * @param respuestasCorrectas La cantidad de respuestas correctas.
     * @param porcentaje El porcentaje de respuestas correctas.
     */
    private static void finalizacion(int respuestasCorrectas, double porcentaje) {
        System.out.println(azul+"Has terminado el juego!\nHas respondido bien : "+ respuestasCorrectas + " preguntas."+ reset);
        if (porcentaje < 50){
            System.out.println("Tu resultado es: " + rojo + porcentaje + " %" + reset);
        }else{
            System.out.println("Tu resultado es: " + verde + porcentaje + " %" + reset);
        }
    }

    /**
     * Guarda las estadísticas de la partida en un archivo de texto.
     * @param nombreUser El nombre del usuario que jugó la partida.
     * @param respuestasCorrectas El número de respuestas correctas en la partida.
     * @param respuestasIncorrectas El número de respuestas incorrectas en la partida.
     */
    private static void guardarEstadisticas(String nombreUser, int respuestasCorrectas, int respuestasIncorrectas) {
        try {
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHoraFormateada = fechaHoraActual.format(formatter);

            FileWriter fw = new FileWriter("../Quiz_Carla/src/resources/estadisticas.txt", true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println("Nombre de usuario: " + nombreUser);
            pw.println("Fecha y hora de finalización: " + fechaHoraFormateada);
            pw.println("Respuestas correctas: " + respuestasCorrectas);
            pw.println("Respuestas incorrectas: " + respuestasIncorrectas);
            pw.println();

            pw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar estadísticas: " + e.getMessage());
        }
    }
}




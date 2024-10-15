import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMonedas {

    private static final String CLAVE_API = "22f6a7ea564061317d1b62d0";  // Reemplaza con tu clave de API

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            // Menú de opciones
            System.out.println("Seleccione una opción de conversión:");
            System.out.println("1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Real brasileño => Dólar");
            System.out.println("4) Dólar => Real brasileño");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            opcion = scanner.nextInt();

            // Solicitar cantidad de dinero a convertir
            System.out.println("Ingrese la cantidad a convertir:");
            double cantidad = scanner.nextDouble();

            // Variables de monedas
            String monedaOrigen = "";
            String monedaDestino = "";

            // Definir monedas según la opción seleccionada
            switch (opcion) {
                case 1:
                    monedaOrigen = "USD";
                    monedaDestino = "ARS";
                    break;
                case 2:
                    monedaOrigen = "ARS";
                    monedaDestino = "USD";
                    break;
                case 3:
                    monedaOrigen = "BRL";
                    monedaDestino = "USD";
                    break;
                case 4:
                    monedaOrigen = "USD";
                    monedaDestino = "BRL";
                    break;
                case 5:
                    monedaOrigen = "USD";
                    monedaDestino = "COP";
                    break;
                case 6:
                    monedaOrigen = "COP";
                    monedaDestino = "USD";
                    break;
                default:
                    System.out.println("Opción no válida");
                    continue;  //se  devuelve al menú si la opción no es válida
            }

            // Realizar la conversión
            convertirMoneda(monedaOrigen, monedaDestino, cantidad);

            //se  Mostrara opciones después de la conversión
            System.out.println("¿Qué desea hacer ahora?");
            System.out.println("1) Convertir otra moneda");
            System.out.println("2) Salir");
            opcion = scanner.nextInt();

        } while (opcion != 2);  // Salir si el usuario selecciona 2

        System.out.println("Gracias por usar el conversor de monedas.");
        scanner.close();
    }

    public static void convertirMoneda(String monedaOrigen, String monedaDestino, double cantidad) {
        try {
            // Construir la URL para la API
            String urlStr = "https://v6.exchangerate-api.com/v6/" + CLAVE_API + "/pair/" + monedaOrigen + "/" + monedaDestino;
            URL url = new URL(urlStr);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            int codigoRespuesta = conexion.getResponseCode();
            if (codigoRespuesta == HttpURLConnection.HTTP_OK) {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String lineaEntrada;
                StringBuffer respuesta = new StringBuffer();

                while ((lineaEntrada = entrada.readLine()) != null) {
                    respuesta.append(lineaEntrada);
                }
                entrada.close();

                // Parsear la respuesta JSON
                JSONObject respuestaJson = new JSONObject(respuesta.toString());
                double tasaCambio = respuestaJson.getDouble("conversion_rate");
                double montoConvertido = cantidad * tasaCambio;

                // Mostrar los resultados de la conversión
                System.out.printf("Tasa de cambio: %.2f%n", tasaCambio);
                System.out.printf("Monto convertido: %.2f %s%n", montoConvertido, monedaDestino);
            } else {
                System.out.println("Error en la respuesta de la API. Código: " + codigoRespuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

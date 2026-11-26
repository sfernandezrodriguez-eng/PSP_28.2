import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class ServidorUDP {

    public static void main(String[] args) throws IOException {
        try (DatagramSocket socket = new DatagramSocket(5555)) {
            System.out.println("Servidor UDP iniciado en el puerto 5555");
            byte[] buffer = new byte[1024];
            int solucion = ThreadLocalRandom.current().nextInt(2, 101);
            while (true) {
                DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(recibido); // BLOQUEA
                String texto = new String(recibido.getData(), 0,
                        recibido.getLength(), StandardCharsets.UTF_8); // ¡solo los bytes válidos!
                System.out.println(recibido.getAddress() + ":" + recibido.getPort() + " -> " + texto);
                String respuesta = "";
                try {
                    int numero = Integer.parseInt(texto.trim());

                    if (numero >= 1 && numero <= 100) {
                        respuesta = compararNumero(numero,solucion);
                    } else {
                        respuesta ="Es un número, pero está fuera del rango (1-100).";
                    }

                } catch (NumberFormatException e) {
                    respuesta ="Err Comando no valido";
                }
                byte[] resp = respuesta.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(resp, resp.length,
                        recibido.getAddress(), recibido.getPort())); // respondo al remitente
            }
        }
    }

    private static String compararNumero(int numero,int solucion) {

        String respuesta = "";

        if (numero>solucion) {
            respuesta = "Menor";
        }
        else if (numero<solucion) {
            respuesta = "Mayor";
        }
        else{
            respuesta = "Acierto";
        }

        return respuesta;
    }
}
# Juego: Adivina el Número (Servidor TCP)

Implementación de una arquitectura **Cliente-Servidor** en Java mediante sockets orientados a conexión (**TCP**). El servidor genera un número secreto aleatorio entre `1` y `100` por cada sesión y guía al cliente mediante pistas hasta que acierta o decide finalizar la partida.

---

## Protocolo de Comunicación

La comunicación se realiza en **texto plano codificado en UTF-8**, siguiendo un esquema estricto de **petición-respuesta por línea** (cada línea enviada por el cliente recibe exactamente una línea de respuesta por parte del servidor).

| Entrada del Cliente | Respuesta del Servidor | Acción / Estado |
| :--- | :--- | :--- |
| `<número>` *(menor al secreto)* | `MAYOR` | Suma 1 intento y continúa la partida. |
| `<número>` *(mayor al secreto)* | `MENOR` | Suma 1 intento y continúa la partida. |
| `<número>` *(igual al secreto)* | `ACIERTO en N intentos` | Muestra el total de intentos y reinicia el número secreto. |
| `ADIOS` | `OK Hasta luego` | Cierra de forma limpia la conexión con el cliente. |
| *Cualquier otro texto* | `ERR Comando no válido` | No suma intentos; solicita una entrada válida. |

---

## Características Implementadas

* **Gestión limpia de desconexiones:** Si el cliente cierra la terminal abruptamente (`Ctrl+C`), el servidor detecta el fin de flujo (`null`) o captura la excepción de E/S sin volcar trazas de error en consola, quedando disponible para nuevos clientes.
* **Contador de intentos independiente:** Cada sesión lleva su propio registro de intentos hasta lograr el acierto.
* **Compatibilidad con clientes estándar:** Al usar flujos de texto delimitados por saltos de línea (`BufferedReader` / `PrintWriter`), es totalmente compatible tanto con `ClienteTCP.java` como con herramientas de red como **Netcat (`nc`)**.

---

## Partida de Ejemplo con `nc` (Netcat)

Con el servidor escuchando en el puerto `5555`, se establece una conexión desde la terminal ejecutando:

```bash
nc localhost 5555
```

### Transcripción de la sesión:

```text
hola
ERR Comando no válido
50
MAYOR
75
MENOR
62
MAYOR
68
ACIERTO en 4 intentos
ADIOS
OK Hasta luego
```

> **Nota:** Tras recibir `OK Hasta luego`, el servidor cierra el socket automáticamente y `nc` devuelve el control a la terminal.
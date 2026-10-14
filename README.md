# Pasatiempos Tranquilos

Prototipo Android en español: una sopa de letras pequeña, sin anuncios, cuentas ni conexión a internet. La pantalla usa texto grande, contraste alto, botones de 48 dp y descripciones para lectores de pantalla.

## Qué incluye

- Una cuadrícula de 5 × 5 con SOL, LUZ y PAZ.
- Selección por toque en la primera y la última letra; admite palabras horizontales y verticales.
- Mensajes de orientación y botón grande para empezar de nuevo.
- Flujo de GitHub Actions que compila un APK de prueba en la nube y lo conserva 14 días.

## Compilar sin Android Studio en el PC

1. Crea un repositorio en GitHub llamado `PasatiemposTranquilos`.
2. Sube el contenido de esta carpeta al repositorio, conservando las carpetas.
3. En GitHub, abre **Actions** y permite ejecutar los flujos si GitHub lo solicita.
4. La compilación comienza al subir archivos a la rama `main`. También puedes abrir **Actions**, seleccionar **Compilar app Android** y pulsar **Run workflow**.
5. Cuando termine, abre esa ejecución y descarga **Pasatiempos-Tranquilos-APK-prueba** en la sección **Artifacts**.

La primera compilación descarga Gradle y los paquetes del Android SDK, así que puede tardar varios minutos. El APK de prueba no está firmado para publicar en Google Play.

## Probar en un teléfono Android

1. Descomprime el artefacto descargado y localiza `app-debug.apk`.
2. Conecta el teléfono al PC con un cable USB y copia el APK a la carpeta **Descargas** del teléfono. También puedes transferirlo con un método de archivos que ya uses.
3. En el teléfono, abre el APK desde **Archivos** o **Descargas**. Android puede pedirte permiso para instalar una app desde esa fuente; concede el permiso solo a la aplicación desde la que abriste el archivo.
4. Instala y abre **Pasatiempos Tranquilos**. Prueba las palabras SOL, LUZ y PAZ, y activa TalkBack si quieres revisar la lectura de los botones.

Para depurar mediante USB desde herramientas Android, se activa **Opciones para desarrolladores > Depuración por USB** en el teléfono. Para instalar el APK descargado, normalmente basta con abrir el archivo en el propio teléfono; el controlador USB depende del fabricante si Windows no detecta el dispositivo.

## Versiones del proyecto

El proyecto usa Android Gradle Plugin 9.2.0, Gradle 9.4.1, Kotlin 2.3.10, Android SDK 37 y Compose BOM 2026.08.00. La compatibilidad se eligió según la documentación oficial de Android consultada el 14 de octubre de 2026.

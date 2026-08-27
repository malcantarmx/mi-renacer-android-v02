# Mi Renacer Android Demo v0.2

Aplicación Android nativa conectada a RAE Mobile API Gateway.

## Servidor configurado

`https://revendepro.com/finanzaspro/wp-json/rae-mobile/v1/`

## Incluido

- Inicio de sesión nativo.
- Token cifrado mediante Android Keystore (AES-GCM).
- Conexiones exclusivamente HTTPS.
- Dashboard con scores, progreso y conteos.
- Historial de scores.
- Lista de auditorías.
- Lista de documentos.
- Perfil y cierre de sesión.
- Manejo automático de sesiones vencidas.
- Identidad visual Renacer con tarjetas redondeadas y navegación activa.
- Indicadores de cambio de score por buró.
- Credit Journey con barra de progreso.
- Fechas en español.
- Detalle nativo de hallazgos de auditoría.
- Compilación automática incluida en `.github/workflows/build-android.yml`.

## Compilar en Android Studio

1. Instala Android Studio reciente y Android SDK 35.
2. Selecciona **Open** y abre la carpeta `mi-renacer-android-demo`.
3. Espera a que Gradle sincronice el proyecto.
4. Conecta un teléfono Android con depuración USB o abre un emulador.
5. Pulsa **Run** para probar.
6. Para generar APK: **Build > Build App Bundle(s) / APK(s) > Build APK(s)**.

## Compilar automáticamente en GitHub

Sube el contenido de esta carpeta a la raíz del repositorio. En **Actions**, abre
`Build Mi Renacer Android`, ejecuta **Run workflow** y descarga el artefacto
`mi-renacer-android-v0.2-debug`.

El APK de depuración aparecerá normalmente en:

`app/build/outputs/apk/debug/app-debug.apk`

## Alcance del demo

Esta versión confirma login y lectura real del expediente. La vista detallada de
auditorías, descarga/visor de PDF, gráficas, recursos, notificaciones y biometría
se agregarán después de validar el inicio de sesión y dashboard con el cliente demo.

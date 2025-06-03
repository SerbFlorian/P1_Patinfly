# Práctica 1 - PatinFly

## Descripción general

Esta práctica consiste en desarrollar una aplicación Android con interfaz en Jetpack Compose que permite visualizar una lista de bicicletas eléctricas disponibles para alquiler. La aplicación muestra información relevante de cada bicicleta, como la distancia al usuario, el estado de la batería y el tipo de bicicleta. Además, permite acceder a una pantalla con el detalle de cada bicicleta.

El objetivo es crear una interfaz moderna y eficiente que facilite la consulta rápida de las bicicletas más cercanas, proporcionando una experiencia de usuario sencilla y clara.

## Contexto de esta primera versión

En esta versión inicial de la práctica, los datos de las bicicletas y del usuario se cargan desde ficheros JSON locales (`bikes.json` y `user.json`). Una vez cargados los datos localmente, estos se guardan en la base de datos local y, cada vez que se hace una reserva o se renta una bicicleta, esta información se ve reflejada en la base de datos.

- `bikes.json`: contiene la información de las bicicletas disponibles, incluyendo identificador, ubicación, nivel de batería, tipo, etc.
- `user.json`: contiene los datos del usuario, principalmente para calcular distancias y personalizar la experiencia.

La estructura y formato de estos ficheros se mantiene simple para facilitar la integración y futuras mejoras.

## Estructura del proyecto

- `presentation.screen.BikeListScreen`: Composable que muestra la lista de bicicletas o un mensaje cuando no hay disponibles.
- `presentation.screen.BikeCard`: Composable que representa la tarjeta individual con la información de cada bicicleta y el botón para ver detalles.
- `presentation.detailBike.DetailBikeActivity`: Actividad que muestra la información detallada de una bicicleta específica.

## Próximos pasos

Las siguientes versiones contemplarán:

- Integración con fuentes de datos remotas (APIs).
- Mejoras en la interfaz y experiencia de usuario.

## Cómo ejecutar la aplicación

1. Clona este repositorio.
2. Abre el proyecto en Android Studio.
3. Asegúrate de que los archivos `bikes.json` y `user.json` estén ubicados en el directorio adecuado (`res/raw`).
4. Compila y ejecuta la aplicación en un emulador o dispositivo Android.
